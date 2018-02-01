package com.application.emoji.redditapp.Comments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.application.emoji.redditapp.ExtractXML;
import com.application.emoji.redditapp.FeedAPI;
import com.application.emoji.redditapp.R;
import com.application.emoji.redditapp.model.Feed;
import com.application.emoji.redditapp.model.entry.Entry;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Sahil on 27-01-2018.
 */

public class CommentsActivity extends AppCompatActivity {
    private static final String TAG = "CommentsActivity";
    private static final String BASE_URL = "https://www.reddit.com/r/";

    private static String postURL;
    private static String postThumbnailURL;
    private static String postAuthor;
    private static String postTitle;
    private static String postUpdated;
    private int defaultImage;

    private ProgressBar mProgressBar;
    private TextView progressText;

    private ListView mListView;
    private ArrayList<Comment> mComments;
    private String currentFeed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        mProgressBar = (ProgressBar)findViewById(R.id.commentProgressBar);
        //mProgressBar.setVisibility(View.VISIBLE);
        progressText = (TextView) findViewById(R.id.progressText);

        setupImageLoader();
        initPost();
        init();

    }

    private void init()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedAPI feedAPI = retrofit.create(FeedAPI.class);
        Call<Feed> call = feedAPI.getFeed(currentFeed);

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                mComments = new ArrayList<>();
                List<Entry> entries = response.body().getEntries();
                for(int i = 0; i < entries.size(); i++){
                    ExtractXML extract = new ExtractXML(entries.get(i).getContent(), "<div class=\"md\"><p>", "</p>");
                    List<String> commentDetails = extract.start();

                    try{
                        mComments.add(new Comment(
                                commentDetails.get(0),
                                entries.get(i).getAuthor().getName(),
                                entries.get(i).getUpdated(),
                                entries.get(i).getId()
                            ));
                    }catch (IndexOutOfBoundsException e){
                        mComments.add(new Comment(
                                "Error reading comment",
                                "NONE",
                                "NONE",
                                "NONE"
                        ));
                        Log.e(TAG, "onResponse: IndexOutOfBoundsException" + e.getMessage() );
                    }
                    catch (NullPointerException e){
                        mComments.add(new Comment(
                                commentDetails.get(0),
                                "NONE",
                                entries.get(i).getUpdated(),
                                entries.get(i).getId()
                        ));;
                        Log.e(TAG, "onResponse: NullPointerException" + e.getMessage() );
                    }
                }
                mListView = (ListView)findViewById(R.id.commentsListView);
                CommentsListAdapter commentsListAdapter = new CommentsListAdapter(CommentsActivity.this, R.layout.comments_layout, mComments);
                mListView.setAdapter(commentsListAdapter);
                //mProgressBar.setVisibility(View.GONE);
                progressText.setText("");
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to Retrieve RSS: " + t.getMessage());
                Toast.makeText(CommentsActivity.this, "An Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initPost(){
        Intent incomingIntent = getIntent();
        postURL = incomingIntent.getStringExtra("@string/posts_url");
        postThumbnailURL = incomingIntent.getStringExtra("@string/posts_thumbnail");
        postAuthor = incomingIntent.getStringExtra("@string/posts_author");
        postTitle = incomingIntent.getStringExtra("@string/posts_title");
        postUpdated = incomingIntent.getStringExtra("@string/posts_updated");

        TextView title = (TextView) findViewById(R.id.postTitle);
        TextView author = (TextView) findViewById(R.id.postAuthor);
        TextView updated = (TextView) findViewById(R.id.postUpdated);
        ImageView thumbnail = (ImageView) findViewById(R.id.postThumbnail);
        Button btnReply = (Button) findViewById(R.id.btnPostReply);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.postLoadingProgressBar);

        title.setText(postTitle);
        author.setText(postAuthor);
        updated.setText(postUpdated);
        displayImage(postThumbnailURL, thumbnail, progressBar);

        String[] splitURL = postURL.split(BASE_URL);
        currentFeed = splitURL[1];
    }

    private void displayImage(String imageURL, ImageView imageView, final ProgressBar progressBar){

        ImageLoader imageLoader = ImageLoader.getInstance();

        //create display options
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        //download and display image from url
        imageLoader.displayImage(imageURL, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(view.VISIBLE);
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(view.GONE);
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(view.GONE);
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(view.GONE);
            }
        });
    }
    private void setupImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                CommentsActivity.this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        defaultImage = CommentsActivity.this.getResources().getIdentifier("@drawable/image_failed",null,CommentsActivity.this.getPackageName());
    }
}
