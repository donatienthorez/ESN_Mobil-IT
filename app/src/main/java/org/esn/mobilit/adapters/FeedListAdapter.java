package org.esn.mobilit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.esn.mobilit.R;
import org.esn.mobilit.models.RSS.RSSItem;
import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.inject.InjectUtil;
import org.esn.mobilit.utils.parser.RSSFeedParser;

import javax.inject.Inject;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

    private RSSFeedParser feed;
    OnItemClickListener itemClickListener;

    @ForApplication
    @Inject
    Context context;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.itemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            textView = (TextView) v.findViewById(R.id.title);
            v.setOnClickListener(this);
        }

        public void setTitle(String title) {
            textView.setText(title);
        }

        public void setImage(String imageLink) {
            Glide.with(context)
                    .load(imageLink)
                    .placeholder(R.drawable.default_list_item)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public FeedListAdapter(RSSFeedParser feed) {
        this.feed = feed != null ? feed : new RSSFeedParser();
    }

    public void setFeed(RSSFeedParser feed) {
        this.feed = feed != null ? feed : new RSSFeedParser();
        this.notifyDataSetChanged();
    }

    public void setEmptyList() {
        setFeed(null);
    }

    @Override
    public FeedListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_feeds, parent, false);

        InjectUtil.component().inject(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RSSItem currentFeed = feed.getList().get(position);
        holder.setTitle(currentFeed.getTitle());
        holder.setImage(currentFeed.getImage());
    }

    @Override
    public int getItemCount() {
        return feed.getItemCount();
    }
}
