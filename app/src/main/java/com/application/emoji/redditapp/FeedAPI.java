package com.application.emoji.redditapp;

import com.application.emoji.redditapp.Account.CheckLogin;
import com.application.emoji.redditapp.Comments.CheckComment;
import com.application.emoji.redditapp.model.Feed;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    //To check login credentials
    @POST("{user}")
    Call<CheckLogin> signIn(

            @HeaderMap Map <String, String> headers,
            @Path("user") String username,
            @Query("user") String user,
            @Query("passwd") String password,
            @Query("api_type") String type
    );

    // to post comment
    @POST("{comment}")
    Call<CheckComment> submitComment(
            @HeaderMap Map<String, String> headers,
            @Path("comment") String comment,
            @Query("parent") String parent,
            @Query("amp;text") String text
    );

}
