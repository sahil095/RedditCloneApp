package com.application.emoji.redditapp.Account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.application.emoji.redditapp.FeedAPI;
import com.application.emoji.redditapp.MainActivity;
import com.application.emoji.redditapp.R;
import com.application.emoji.redditapp.model.Feed;
import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    public static final String BASE_URL = "https://www.reddit.com/api/login/";
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnLogin = findViewById(R.id.btn_Login);
        mPassword = findViewById(R.id.input_password);
        mUsername = findViewById(R.id.input_username);
        final ProgressBar mProgressBar = findViewById(R.id.loginProgressBar);
        mProgressBar.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if(!username.equals("") && !password.equals("")){
                    mProgressBar.setVisibility(View.VISIBLE);
                    login(username, password);
                }
            }
        });

    }

    private void login(String username, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FeedAPI feedAPI = retrofit.create(FeedAPI.class);

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-type", "application/json");
        Call<CheckLogin> call = feedAPI.signIn(headerMap,username, username, password, "json");

        call.enqueue(new Callback<CheckLogin>() {
            @Override
            public void onResponse(Call<CheckLogin> call, Response<CheckLogin> response) {
                Log.d(TAG, "onResponse: feed: " + response.body().toString());
                Log.d(TAG, "onResponse: Server Response: " + response.toString());
            }

            @Override
            public void onFailure(Call<CheckLogin> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to Retrieve RSS: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "An Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
