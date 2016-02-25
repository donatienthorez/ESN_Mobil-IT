package org.esn.mobilit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public class ListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private RSSFeedParser feed;

    public ListAdapter(RSSFeedParser feed, LayoutInflater layoutInflater) {
        this.feed = feed;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return feed.getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        int pos = position;
        if (listItem == null) {
            listItem = layoutInflater.inflate(R.layout.list_item, null);
        }

        ImageView iv = (ImageView) listItem.findViewById(R.id.thumb);
        TextView tvTitle = (TextView) listItem.findViewById(R.id.title);
        TextView tvDate = (TextView) listItem.findViewById(R.id.date);

        Glide.with(MobilITApplication.getContext())
                .load(feed.getItem(pos).getImage())
                .placeholder(R.drawable.default_list_item)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);

        tvTitle.setText(feed.getItem(pos).getTitle());
        tvDate.setText(feed.getItem(pos).getDate());

        return listItem;
    }
}
