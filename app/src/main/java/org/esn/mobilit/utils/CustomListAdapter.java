package org.esn.mobilit.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.esn.mobilit.R;
import org.esn.mobilit.utils.image.ImageLoader;
import org.esn.mobilit.utils.parser.RSSFeed;
import org.esn.mobilit.utils.parser.RSSItem;

import java.util.List;

/**
 * Created by Spider on 13/01/15.
 */

public class CustomListAdapter extends BaseAdapter {

    Context context;
    List<RSSItem> rssItems;
    ImageLoader imageLoader;

    public CustomListAdapter(Context context, RSSFeed feed) {
        this.context = context;
        this.rssItems = feed.getList();
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {

        return rssItems.size();
    }

    @Override
    public Object getItem(int position) {

        return rssItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return rssItems.indexOf(getItem(position));
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        if (convertView == null) {
            //LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.list_item, null);
        }

        // Initialize the views in the layout
        ImageView iv = (ImageView) convertView.findViewById(R.id.thumb);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
        TextView tvDate = (TextView) convertView.findViewById(R.id.date);

        // Set the views in the layout
        imageLoader.DisplayImage(rssItems.get(pos).getImage(), iv);
        tvTitle.setText(rssItems.get(pos).getTitle());
        tvDate.setText(rssItems.get(pos).getDate());

        return convertView;

    }

}