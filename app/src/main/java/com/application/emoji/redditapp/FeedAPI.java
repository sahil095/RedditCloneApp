package com.application.emoji.redditapp;

import com.application.emoji.redditapp.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Sahil on 27-12-2017.
 */

public interface FeedAPI {

    @GET("earthporn/.rss")
    Call<Feed> getFeed();
}
