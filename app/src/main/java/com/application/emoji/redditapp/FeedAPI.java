package com.application.emoji.redditapp;

import com.application.emoji.redditapp.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Sahil on 27-12-2017.
 */

public interface FeedAPI {

    String BASE_URL = "https://www.reddit.com/r/";

    //Dynamic
    @GET("{feed_name}/.rss")
    Call<Feed> getFeed(@Path("feed_name") String feed_name);

    //Static
//    @GET("earthporn/.rss")
//    Call<Feed> getFeed();
}
