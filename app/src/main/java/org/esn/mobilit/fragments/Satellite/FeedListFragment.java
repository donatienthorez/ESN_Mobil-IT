package org.esn.mobilit.fragments.Satellite;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

public class FeedListFragment extends ListFragment
{
    private RSSFeedParser feed;
    private RSSFeedService rssFeedService;
    private ListAdapter adapter;

    @Bind(R.id.swipe_refresh) protected SwipeRefreshLayout swipeRefreshLayoutListView;

    public FeedListFragment setService(RSSFeedService rssFeedService){
        this.rssFeedService = rssFeedService;
        this.feed = rssFeedService.getFromCache();

        return this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_feeds, container, false);
        ButterKnife.bind(this, view);

        adapter = new ListAdapter(feed, inflater);
        this.setListAdapter(adapter);

        swipeRefreshLayoutListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent(true);
            }
        });
        refreshContent(false);
        return view;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        ((HomeActivity) getActivity()).replaceByDetailsFragment(feed.getItem(position), true);
    }

    private void refreshContent(final boolean showMessage){
        swipeRefreshLayoutListView.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        swipeRefreshLayoutListView.setRefreshing(true);
        swipeRefreshLayoutListView.post(new Runnable() {
            @Override
            public void run() {
                rssFeedService.getFromSite(new NetworkCallback<RSSFeedParser>() {
                    @Override
                    public void onSuccess(RSSFeedParser result) {
                        feed = result;
                        adapter.setFeed(feed);
                        swipeRefreshLayoutListView.setRefreshing(false);
                    }

                    @Override
                    public void onNoAvailableData() {
                        swipeRefreshLayoutListView.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(String error) {
                        swipeRefreshLayoutListView.setRefreshing(false);
                        if (showMessage) {
                            Toast.makeText(
                                    MobilITApplication.getContext(),
                                    getResources().getString(R.string.info_message_no_network),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
            }
        });
    }
}
