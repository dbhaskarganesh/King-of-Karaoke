package com.ganesh_project.kingofkaraoke.score;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ganesh_project.kingofkaraoke.R;
import com.ganesh_project.kingofkaraoke.databinding.ActivityScoreBinding;
import com.ganesh_project.kingofkaraoke.model.HighScoreResponse;
import com.ganesh_project.kingofkaraoke.model.QuestionInfo;
import com.ganesh_project.kingofkaraoke.model.ScoreResponse;
import com.ganesh_project.kingofkaraoke.results.ResultActivity;
import com.ganesh_project.kingofkaraoke.utils.RetrofitAPI;
import com.ganesh_project.kingofkaraoke.utils.SharedPreference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScoreActivity extends AppCompatActivity {

    private ActivityScoreBinding binding;
    ArrayList<QuestionInfo> questionList = new ArrayList<>();
    long remainingTime = 0;
    long usedTime = 180000;

    int correctCount = 0;
    int incorrectCount = 0;
    int hintsUsedCount = 0;
    float finalScore = 0;
    ScoreResponse scoreResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        scoreInfo();
        initUI();
    }

    private void updateScoreInfo(float finalScore) {
        SharedPreference sharedPreference = new SharedPreference(ScoreActivity.this);
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
        Call<ScoreResponse> call = retrofitAPI.updateHighScore(getResources().getString(R.string.score_bin_id), new ScoreResponse(new HighScoreResponse(sharedPreference.getEmail(), finalScore, usedTime)));
        call.enqueue(new Callback<ScoreResponse>() {
            @Override
            public void onResponse(Call<ScoreResponse> call, Response<ScoreResponse> response) {

                scoreResponse = response.body();
                Toast.makeText(ScoreActivity.this, "Score updated to server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ScoreResponse> call, Throwable t) {
                Toast.makeText(ScoreActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        call.enqueue(new Callback<ScoreResponse>() {
            @Override
            public void onResponse(Call<ScoreResponse> call, Response<ScoreResponse> response) {

                scoreResponse = response.body();
                if(scoreResponse.getScore().getScore() <= finalScore) {
                    if(scoreResponse.getScore().getScore() == finalScore && scoreResponse.getScore().getTiming() > usedTime) {
                        updateScoreInfo(finalScore);
                    } else if(scoreResponse.getScore().getScore() < finalScore) {
                        updateScoreInfo(finalScore);
                    }
                }
            }

            @Override
            public void onFailure(Call<ScoreResponse> call, Throwable t) {
                Toast.makeText(ScoreActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        questionList = getIntent().getParcelableArrayListExtra("QUESTION_LIST");
        remainingTime = getIntent().getLongExtra("TIME_REMAINING", 0);
        usedTime -= remainingTime;

        String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(usedTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(usedTime)),
                TimeUnit.MILLISECONDS.toSeconds(usedTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(usedTime)));

        for (int i = 0; i < questionList.size(); i++) {
            QuestionInfo question = questionList.get(i);

            if (question.isAnswerCorrect()) {
                correctCount += 1;
            } else {
                incorrectCount += 1;
            }

            if (question.isUser_hint()) {
                hintsUsedCount += 1;
            }
        }

        binding.correctAnswerCount.setText(String.valueOf(correctCount));
        binding.wrongAnswerCount.setText(String.valueOf(incorrectCount));
        binding.hintUsedCount.setText(String.valueOf(hintsUsedCount));

        finalScore = correctCount - (hintsUsedCount / 2);
        binding.finalScoreAppCompatTextView.setText(getResources().getString(R.string.final_score) + ": " + String.valueOf(finalScore));
        binding.usedTimingAppCompatTextView.setText(getResources().getString(R.string.time_used) + " " + hms);

        binding.backAppCompatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.viewResultsAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, ResultActivity.class);
                intent.putExtra("QUESTION_LIST", questionList);
                intent.putExtra("FINAL_SCORE", finalScore);
                startActivity(intent);
            }
        });
    }
}