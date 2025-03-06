package com.ganesh_project.kingofkaraoke.quiz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ganesh_project.kingofkaraoke.R;
import com.ganesh_project.kingofkaraoke.databinding.ActivityQuizBinding;
import com.ganesh_project.kingofkaraoke.model.MusicInfoResponse;
import com.ganesh_project.kingofkaraoke.model.QuestionInfo;
import com.ganesh_project.kingofkaraoke.quiz.view.QuizCustomViewPager;
import com.ganesh_project.kingofkaraoke.utils.RetrofitAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizActivity extends AppCompatActivity {

    private ActivityQuizBinding binding;
    private FragmentStateAdapter pagerAdapter;
    private long currentMilisecods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if(getIntent().hasExtra("IS_NETWORK")) {
            postData();
        } else {
            initUI();
            setTimer();
        }
        binding.backAppCompatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("musicInfo.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void initUI() {

        String jsonData = loadJSONFromAsset();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        MusicInfoResponse musicInfo = gson.fromJson(jsonData, MusicInfoResponse.class);

        ArrayList<QuestionInfo> musicList = musicInfo.getMusic_info();
        Collections.shuffle(musicList);
        pagerAdapter = new QuizCustomViewPager(this, musicList);
        binding.quizViewPager.setAdapter(pagerAdapter);

    }

    private void postData() {
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
        Call<MusicInfoResponse> call = retrofitAPI.getListOfBins(getResources().getString(R.string.bin_id));
        call.enqueue(new Callback<MusicInfoResponse>() {
            @Override
            public void onResponse(Call<MusicInfoResponse> call, Response<MusicInfoResponse> response) {

                MusicInfoResponse responseFromAPI = response.body();

                System.out.println("Music info: " + response.message());
                System.out.println("Music info: " + response.code());
                ArrayList<QuestionInfo> musicList = responseFromAPI.getMusic_info();
                System.out.println("Music info: " + response.body());

                Collections.shuffle(musicList);
                pagerAdapter = new QuizCustomViewPager(QuizActivity.this, musicList);
                binding.quizViewPager.setAdapter(pagerAdapter);
                setTimer();
            }

            @Override
            public void onFailure(Call<MusicInfoResponse> call, Throwable t) {
                Toast.makeText(QuizActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void changeViewPagerPosition(int position) {
        binding.quizViewPager.setCurrentItem(position);
    }

    public void setTimer() {
        new CountDownTimer(180000, 1000) {
            public void onTick(long millisUntilFinished) {
                currentMilisecods = millisUntilFinished;
                String hms = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                binding.timerAppCompatTextView.setText(hms);
            }

            public void onFinish() {
                currentMilisecods = 0;
                binding.timerAppCompatTextView.setText("done!");
                QuizFragment fragment = QuizFragment.getInstance();
                fragment.timeoutFinishQuiz();
            }
        }.start();
    }

    public long finalRemainingTime() {
        return currentMilisecods;
    }
}