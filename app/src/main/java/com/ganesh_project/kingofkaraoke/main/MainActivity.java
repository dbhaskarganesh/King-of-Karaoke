package com.ganesh_project.kingofkaraoke.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.UserProfile;
import com.ganesh_project.kingofkaraoke.R;
import com.ganesh_project.kingofkaraoke.databinding.ActivityMainBinding;
import com.ganesh_project.kingofkaraoke.login.LoginActivity;
import com.ganesh_project.kingofkaraoke.model.ScoreResponse;
import com.ganesh_project.kingofkaraoke.quiz.QuizActivity;
import com.ganesh_project.kingofkaraoke.score.ScoreActivity;
import com.ganesh_project.kingofkaraoke.utils.RetrofitAPI;
import com.ganesh_project.kingofkaraoke.utils.SharedPreference;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Auth0 auth0;
    public SharedPreference sharedPreference;
    public String IS_NETWORK = "IS_NETWORK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth0 = new Auth0(this);
        sharedPreference = new SharedPreference(MainActivity.this);

        scoreInfo();
        initUI();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        scoreInfo();
    }

    private void scoreInfo() {
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("X-Master-Key", getResources().getString(R.string.master_key))
                                        .header("X-Access-Key", getResources().getString(R.string.access_key))
                                        .header("X-Bin-Meta", String.valueOf(false))
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.jsonbin.io/v3/")
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<ScoreResponse> call = retrofitAPI.getHighScore(getResources().getString(R.string.score_bin_id));
        call.enqueue(new retrofit2.Callback<ScoreResponse>() {
            @Override
            public void onResponse(Call<ScoreResponse> call, Response<ScoreResponse> response) {

                ScoreResponse scoreResponse = response.body();
                binding.highScoreAppCompatTextView.setText(getResources().getString(R.string.final_score)+ ": " + String.valueOf(scoreResponse.getScore().getScore()));

            }

            @Override
            public void onFailure(Call<ScoreResponse> call, Throwable t) {
                System.out.println("t: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initUI() {
        showUserProfile(sharedPreference.getAccessToken());
        binding.startQuizAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Main start quiz");

                ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null){
                    startActivity(new Intent(MainActivity.this, QuizActivity.class));
                }else{
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.dialog_title))
                            .setMessage(getResources().getString(R.string.dialog_description))
                            .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                                    intent.putExtra(IS_NETWORK, true);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(MainActivity.this, QuizActivity.class));
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher_foreground)
                            .show();
                }
            }
        });

        binding.logoutAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void showUserProfile(String accessToken) {
        AuthenticationAPIClient client = new AuthenticationAPIClient(new Auth0(getResources().getString(R.string.com_auth0_client_id), getResources().getString(R.string.com_auth0_domain)));

        client.userInfo(accessToken).start(new Callback<UserProfile, AuthenticationException>() {
            @Override
            public void onSuccess(UserProfile userProfile) {
                sharedPreference.saveEmail(userProfile.getEmail());
                sharedPreference.saveName(userProfile.getName());

                binding.highScoreAppCompatTextView.setText(userProfile.getName() + " " + userProfile.getEmail());
            }

            @Override
            public void onFailure(@NonNull AuthenticationException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void logout() {
        WebAuthProvider.logout(auth0)
                .withScheme("demo")
                .start(this, new Callback<Void, AuthenticationException>() {
                    @Override
                    public void onSuccess(@Nullable Void payload) {
                        sharedPreference.saveToken(null);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull AuthenticationException error) {
                        //Log out canceled, keep the user logged in
                    }
                });
    }
}