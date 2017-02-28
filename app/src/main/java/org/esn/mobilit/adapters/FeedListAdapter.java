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

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

    private ArrayList<RSSItem> rssItemList;
    OnItemClickListener itemClickListener;

    @ForApplication
    @Inject
    Context context;

    public FeedListAdapter() {
        InjectUtil.component().inject(this);
        rssItemList = new ArrayList<>();
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.itemClickListener = mItemClickListener;
    }

    public RSSItem getItem(int position) {
        if (position < rssItemList.size()) {
            return rssItemList.get(position);
        }
        // Should never enter here
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.image)
        ImageView imageView;

        @Bind(R.id.title)
        TextView textView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
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

    public void setRSSItemList(ArrayList<RSSItem> rssItemList) {
        this.rssItemList = rssItemList == null ? new ArrayList<RSSItem>() : rssItemList;
        this.notifyDataSetChanged();
    }

    public void setEmptyList() {
        setRSSItemList(null);
    }

    @Override
    public FeedListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_feeds, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RSSItem currentFeed = rssItemList.get(position);
        holder.setTitle(currentFeed.getTitle());
        holder.setImage(currentFeed.getImage());
    }

    @Override
    public int getItemCount() {
        return rssItemList.size();
    }
}
