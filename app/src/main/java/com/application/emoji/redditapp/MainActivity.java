package com.application.emoji.redditapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.application.emoji.redditapp.model.Feed;
import com.application.emoji.redditapp.model.entry.Entry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String BASE_URL = "https://www.reddit.com/r/";
    private Button btnRefreshFeed;
    private EditText mFeedName;
    private String currentFeed;

//    private RecyclerView recyclerView;
//    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRefreshFeed = (Button) findViewById(R.id.btnRefresh);
        mFeedName = (EditText) findViewById(R.id.etFeedName);

        init();

        btnRefreshFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedName = mFeedName.getText().toString();
                if(!feedName.equals("")){
                    currentFeed = feedName;
                    init();
                }
                else
                {
                    init();
                }
            }
        });

//        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(llm);

    }
    private void init(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        FeedAPI feedAPI = retrofit.create(FeedAPI.class);

        Call<Feed> call = feedAPI.getFeed(currentFeed);

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                //Log.d(TAG, "onResponse: feed: " + response.body().toString());
                Log.d(TAG, "onResponse: Server Response: " + response.toString());

                List<Entry> entries = response.body().getEntries();

                Log.d(TAG, "onResponse: entries: " + response.body().getEntries());

                ArrayList<Post> posts = new ArrayList<>();

                for (int i = 0; i < entries.size(); i++) {
                    ExtractXML extractXML1 = new ExtractXML(entries.get(i).getContent(), "<a href=");
                    List<String> postContent = extractXML1.start();

                    ExtractXML extractXML2 = new ExtractXML(entries.get(i).getContent(), "<img src=");

                    try {
                        postContent.add(extractXML2.start().get(0));
                    } catch (NullPointerException e) {
                        postContent.add(null);
                        Log.e(TAG, "onResponse: NullPointerException(thumbnail):" + e.getMessage());
                    } catch (IndexOutOfBoundsException e) {
                        postContent.add(null);
                        Log.e(TAG, "onResponse: IndexOutOfBoundsException(thumbnail):" + e.getMessage());
                    }

                    int lastPosition = postContent.size() - 1;

                    try{
                        posts.add(new Post(
                                entries.get(i).getTitle(),
                                entries.get(i).getAuthor().getName(),
                                entries.get(i).getUpdated(),
                                postContent.get(0),
                                postContent.get(lastPosition)
                        ));
                    }catch (NullPointerException e){
                        posts.add(new Post(
                                entries.get(i).getTitle(),
                                "None",
                                entries.get(i).getUpdated(),
                                postContent.get(0),
                                postContent.get(lastPosition)
                        ));
                        Log.e(TAG, "onResponse: NullPointerException: " + e.getMessage() );
                    }
                }

//                for(int j = 0; j < posts.size(); j++)
//                {
//                    Log.d(TAG, "onResponse: \n" +
//                                "PostURL: " + posts.get(j).getPostURL() + "\n" +
//                                "ThumbnailURL: " + posts.get(j).getThumbnailURL() + "\n" +
//                                "Title: " + posts.get(j).getTitle() + "\n" +
//                                "Author: " + posts.get(j).getAuthor() + "\n" +
//                                "Updated: " + posts.get(j).getDate_updated() + "\n" );
//                }

                ListView listView = (ListView) findViewById(R.id.listView);
                CustomListAdapter customListAdapter = new CustomListAdapter(MainActivity.this, R.layout.card_layout_main, posts);
                listView.setAdapter(customListAdapter);
//                adapter = new RecyclerAdapter(MainActivity.this, posts);
//                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to Retrieve RSS: " + t.getMessage());
                Toast.makeText(MainActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
