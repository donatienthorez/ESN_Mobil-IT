package org.esn.mobilit.fragments.Satellite;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.esn.mobilit.R;
import org.esn.mobilit.activities.HomeActivity;
import org.esn.mobilit.adapters.FeedListAdapter;
import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.services.cache.CacheService;
import org.esn.mobilit.services.feeds.FeedService;
import org.esn.mobilit.services.feeds.FeedType;
import org.esn.mobilit.services.feeds.RSSItemListHelper;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.inject.InjectUtil;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedListFragment extends Fragment implements NetworkCallback<ArrayList<RSSItem>>
{
    @ForApplication
    @Inject
    Context context;
    @Inject
    Utils utils;
    @Inject
    FeedService feedService;
    @Inject
    CacheService cacheService;
    @Inject
    RSSItemListHelper rssItemListHelper;

    @Bind(R.id.swipe_refresh)
    protected SwipeRefreshLayout swipeRefreshLayoutListView;

    @Bind(R.id.recyclerViewFeedList)
    protected RecyclerView recyclerView;

    @Bind(R.id.empty)
    protected TextView emptyListMessage;

    private FeedListAdapter adapter;
    private FeedType feedType;

    public FeedListFragment setType(FeedType feedType){
        this.feedType = feedType;
        return this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        ButterKnife.bind(this, view);
        InjectUtil.component().inject(this);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);

        adapter = new FeedListAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FeedListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ((HomeActivity) getActivity()).loadDetailsFragment(adapter.getItem(position), true);
            }
        });

        /**
         * Workaround to not have the onrefresh listener acting when scrolling up
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                super.onScrollStateChanged(recyclerView, scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayoutListView.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        swipeRefreshLayoutListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (utils.isConnected()) {
                    refreshContent();
                } else {
                    Toast.makeText(
                            context,
                            getResources().getString(utils.isConnected() ?
                                    R.string.error_message_network :
                                    R.string.info_message_no_network),
                            Toast.LENGTH_SHORT
                    ).show();
                    swipeRefreshLayoutListView.setRefreshing(false);
                }
            }
        });
        adapter.setRSSItemList(cacheService.getFeed(feedType.getCacheableString()));
        refreshContent();

        return view;
    }

    /**
     * Refreshes content of the listView by calling the Retrofit Feed provider.
     */
    private void refreshContent(){
        swipeRefreshLayoutListView.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        swipeRefreshLayoutListView.setRefreshing(true);
        feedService.getFromSite(feedType, this);
    }

    @Override
    public void onNoConnection(ArrayList<RSSItem> rssItemList) {
        if (adapter.getItemCount() == 0) {
            adapter.setRSSItemList(rssItemList);
        }
        swipeRefreshLayoutListView.setRefreshing(false);
    }

    @Override
    public void onSuccess(ArrayList<RSSItem> rssItemList) {
        if (rssItemListHelper.needsAdapterUpdate(feedType, rssItemList)) {
            adapter.setRSSItemList(rssItemList);
        }
        swipeRefreshLayoutListView.setRefreshing(false);
        emptyListMessage.setVisibility(View.GONE);
    }

    @Override
    public void onNoAvailableData() {
        if (adapter.getItemCount() == 0) {
            adapter.setEmptyList();
            swipeRefreshLayoutListView.setRefreshing(false);
            emptyListMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(String error) {
        swipeRefreshLayoutListView.setRefreshing(false);
        if (adapter.getItemCount() == 0) {
            emptyListMessage.setVisibility(View.VISIBLE);
        }
    }
}
