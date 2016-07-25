package org.esn.mobilit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.models.Guide;
import org.esn.mobilit.models.Node;

public class GuideListAdapter extends RecyclerView.Adapter<GuideListAdapter.ViewHolder> {
    private Guide guide;

    OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.itemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView titleView;
        WebView resumeView;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            titleView = (TextView) v.findViewById(R.id.title);
//            resumeView = (WebView) v.findViewById(R.id.resume);
            v.setOnClickListener(this);
        }
//
//        public void setDescription(String resume) {
//
//            if (resume != null && !resume.isEmpty()) {
//                resumeView.loadData(resume, "text/html; charset=UTF-8", null);
//                resumeView.setScrollContainer(false);
//
//                resumeView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        return (event.getAction() == MotionEvent.ACTION_MOVE);
//                    }
//                });
//
//                WebSettings webSettings = resumeView.getSettings();
//                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//                webSettings.setJavaScriptEnabled(true);
//            } else {
//                resumeView.setVisibility(View.GONE);
//            }
//        }

        public void setTitle(String title) {
            titleView.setText(title);
        }

        public void setImage(String imageLink) {
            if (imageLink != null && !imageLink.isEmpty()) {
                Glide.with(MobilITApplication.getContext())
                        .load(imageLink)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            } else {
                imageView.setBackgroundResource(R.color.white);
            }
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public GuideListAdapter(Guide guide) {
        this.guide = guide != null ? guide : new Guide();
    }

    public void setGuide(Guide guide) {
        this.guide = guide != null ? guide : new Guide();
        this.notifyDataSetChanged();
    }

    public void setEmptyGuide() {
        setGuide(null);
    }

    @Override
    public GuideListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_guide, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Node currentNode = guide.getNode(position);
        holder.setTitle(currentNode.getTitle());
        holder.setImage(currentNode.getImage());
//        holder.setDescription(currentNode.getContent());
    }

    @Override
    public int getItemCount() {
        return guide.getSize();
    }
}
