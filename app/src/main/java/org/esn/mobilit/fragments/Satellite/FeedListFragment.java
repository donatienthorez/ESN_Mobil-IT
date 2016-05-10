package org.esn.mobilit.fragments.Satellite;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

import org.esn.mobilit.activities.HomeActivity;
import org.esn.mobilit.adapters.ListAdapter;
import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.services.feeds.RSSFeedService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class FeedListFragment extends Fragment
{
    private RSSFeedParser feed;
    private RSSFeedService rssFeedService;
    private ListAdapter adapter;

    @Bind(R.id.swipe_refresh) protected SwipeRefreshLayout swipeRefreshLayoutListView;
    @Bind(R.id.list) protected ListView listView;
    @Bind(R.id.empty) protected TextView emptyListMessage;

    public FeedListFragment setService(RSSFeedService rssFeedService){
        this.rssFeedService = rssFeedService;
        this.feed = rssFeedService.getFromCache();
        return this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_feeds, container, false);
        ButterKnife.bind(this, view);

        adapter = new ListAdapter(feed, inflater);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listView == null || listView.getChildCount() == 0) ?
                                0 : listView.getChildAt(0).getTop();
                swipeRefreshLayoutListView.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((HomeActivity) getActivity()).replaceByDetailsFragment(feed.getItem(position), true);
            }
        });

        swipeRefreshLayoutListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent(true);
            }
        });
        refreshContent(false);
        return view;
    }

    private void refreshContent(final boolean showMessage){
        swipeRefreshLayoutListView.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        swipeRefreshLayoutListView.setRefreshing(true);
        Thread thread = (new Thread() {
            @Override
            public void run() {
                rssFeedService.getFromSite(new NetworkCallback<RSSFeedParser>() {
                    @Override
                    public void onSuccess(RSSFeedParser result) {
                        feed = result;
                        adapter.setFeed(feed);
                        swipeRefreshLayoutListView.setRefreshing(false);
                        emptyListMessage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNoAvailableData() {
                        feed = null;
                        adapter.setEmptyList();
                        swipeRefreshLayoutListView.setRefreshing(false);
                        emptyListMessage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(String error) {
                        swipeRefreshLayoutListView.setRefreshing(false);

                        if (showMessage) {
                            Toast.makeText(
                                    MobilITApplication.getContext(),
                                    getResources().getString(R.string.error_message_network),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                        emptyListMessage.setVisibility(feed == null ? View.VISIBLE : View.GONE);
                    }
                });
            }
        });
        thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        swipeRefreshLayoutListView.post(thread);
    }
}
