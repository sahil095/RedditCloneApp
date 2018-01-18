package com.application.emoji.redditapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.application.emoji.redditapp.model.Feed;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import java.util.ArrayList;

import retrofit2.Callback;

/**
 * Created by Sahil on 17-01-2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    View view;
    ViewHolder holder;
    private ArrayList<Post> list_members = new ArrayList<>();
    private int mResource;
    private Context mContext;


    public RecyclerAdapter(Context mContext, ArrayList<Post> list_members) {
        this.mContext = mContext;
        this.list_members = list_members;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_main, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.title.setText(list_members.get(position).getTitle());
        holder.author.setText(list_members.get(position).getAuthor());
        holder.date_updated.setText(list_members.get(position).getDate_updated());
    }

    @Override
    public int getItemCount() {
        return list_members.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView author;
        TextView date_updated;
        ProgressBar mProgressBar;
        ImageView thumbnailURL;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.cardTitle);
            author = (TextView) itemView.findViewById(R.id.cardAuthor);
            date_updated = (TextView) itemView.findViewById(R.id.cardUpdated);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.cardProgressDialogue);
            thumbnailURL = (ImageView) itemView.findViewById(R.id.cardImage);
        }
    }
}