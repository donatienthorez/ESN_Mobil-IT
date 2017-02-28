package org.esn.mobilit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.esn.mobilit.R;
import org.esn.mobilit.models.Node;
import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.inject.InjectUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideListAdapter extends RecyclerView.Adapter<GuideListAdapter.ViewHolderItem> {
    @ForApplication
    @Inject
    Context context;

    OnItemClickListener itemClickListener;

    private List<Node> nodes;
    private Node currentNode;

    final int DEFAULT_CATEGORY_ITEM = 0;
    final int DEFAULT_CATEGORY_DETAILS = 1;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.itemClickListener = mItemClickListener;
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.image)
        ImageView imageView;

        @Bind(R.id.title)
        TextView textView;

        public ViewHolderItem(View v) {
            super(v);
            ButterKnife.bind(v);
            v.setOnClickListener(this);
        }

        public void setTitle(String title) {
            textView.setText(title);
        }

        public void setImage(String imageLink) {
            if (imageLink != null && !imageLink.isEmpty()) {
                Glide.with(context)
                        .load(imageLink)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            } else {
                imageView.setBackgroundResource(R.color.white);
            }
        }

        public void setDescription(String resume) {}

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public class ViewHolderItemDetailed extends ViewHolderItem{
        WebView resumeView;
        TextView categoryLabel;
        public ViewHolderItemDetailed(View v) {
            super(v);
            resumeView  = (WebView) v.findViewById(R.id.webview);
            categoryLabel  = (TextView) v.findViewById(R.id.categoryLabel);
        }

        @Override
        public void setDescription(String resume) {
            if (resume != null && !resume.isEmpty()) {
                resumeView.loadData(resume, "text/html; charset=UTF-8", null);
                resumeView.setScrollContainer(false);

                WebSettings webSettings = resumeView.getSettings();
                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                webSettings.setJavaScriptEnabled(true);
            } else {
                resumeView.setVisibility(View.GONE);
            }
            if (currentNode.getNodes().size() == 0) {
                categoryLabel.setVisibility(View.GONE);
            }
        }
    }

    public void setNodes(List<Node> nodes, Node currentNode) {
        if (this.nodes != null) {
            this.nodes.clear();
            this.nodes.addAll(nodes);
        } else {
            this.nodes = new ArrayList<>();
        }
        this.currentNode = currentNode;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        InjectUtil.component().inject(this);

        if (viewType == DEFAULT_CATEGORY_DETAILS) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_guide_item_detailled, parent, false);
            return new ViewHolderItemDetailed(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_guide, parent, false);
            return new ViewHolderItem(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && currentNode != null) {
            return DEFAULT_CATEGORY_DETAILS;
        } else {
            return DEFAULT_CATEGORY_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
        Node currentNode = getNode(position);
        holder.setTitle(currentNode.getTitle());
        holder.setImage(currentNode.getImage());
        holder.setDescription(currentNode.getContent());
    }

    public Node getNode(int position){
        if (position == 0 && currentNode != null) {
            return currentNode;
        } else {
            if (currentNode != null) {
                return nodes.get(position-1);
            }
            return nodes.get(position);
        }
    }

    @Override
    public int getItemCount() {
        return currentNode != null ? nodes.size() + 1 : nodes.size();
    }
}
