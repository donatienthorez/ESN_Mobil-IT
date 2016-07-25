package org.esn.mobilit.fragments.Satellite;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.activities.HomeActivity;
import org.esn.mobilit.adapters.FeedListAdapter;
import org.esn.mobilit.services.feeds.RSSFeedService;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedListFragment extends Fragment
{
    private RSSFeedParser feed;
    private RSSFeedService rssFeedService;
    private FeedListAdapter adapter;

    @Bind(R.id.swipe_refresh) protected SwipeRefreshLayout swipeRefreshLayoutListView;
    @Bind(R.id.recyclerViewFeedList) protected RecyclerView recyclerView;
    @Bind(R.id.empty) protected TextView emptyListMessage;

    public FeedListFragment setService(RSSFeedService rssFeedService){
        this.rssFeedService = rssFeedService;
        this.feed = rssFeedService.getFromCache();
        return this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FeedListAdapter(feed);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FeedListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ((HomeActivity) getActivity()).loadDetailsFragment(feed.getItem(position), true);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                super.onScrollStateChanged(recyclerView, scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayoutListView.setEnabled(topRowVerticalPosition == 0);
            }
        });

        swipeRefreshLayoutListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isConnected()) {
                    refreshContent();
                } else {
                    Toast.makeText(
                            MobilITApplication.getContext(),
                            getResources().getString(Utils.isConnected() ?
                                    R.string.error_message_network :
                                    R.string.info_message_no_network),
                            Toast.LENGTH_SHORT
                    ).show();
                    swipeRefreshLayoutListView.setRefreshing(false);
                }
            }
        });
        refreshContent();
        return view;
    }

    /**
     * Refreshes content of the listView by calling the Retrofit Feed provider.
     */
    private void refreshContent(){
        swipeRefreshLayoutListView.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        swipeRefreshLayoutListView.setRefreshing(true);
        Thread thread = (new Thread() {
            @Override
            public void run() {
                rssFeedService.getFromSite(new NetworkCallback<RSSFeedParser>() {
                    @Override
                    public void onSuccess(RSSFeedParser result) {
                        if (feed.getItemCount() != result.getItemCount()) {
                            feed = result;
                            adapter.setFeed(feed);
                        }
                        swipeRefreshLayoutListView.setRefreshing(false);
                        emptyListMessage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNoAvailableData() {
                        if (feed == null) {
                            adapter.setEmptyList();
                            swipeRefreshLayoutListView.setRefreshing(false);
                            emptyListMessage.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        swipeRefreshLayoutListView.setRefreshing(false);
                        emptyListMessage.setVisibility(feed == null ? View.VISIBLE : View.GONE);
                    }
                });
            }
        });
        thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        swipeRefreshLayoutListView.post(thread);
    }
}
