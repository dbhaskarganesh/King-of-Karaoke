package com.ganesh_project.kingofkaraoke.utils;

import com.ganesh_project.kingofkaraoke.model.HighScoreResponse;
import com.ganesh_project.kingofkaraoke.model.MusicInfoResponse;
import com.ganesh_project.kingofkaraoke.model.ScoreResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitAPI {

    @GET("b/{bin}")
    Call<MusicInfoResponse> getListOfBins(@Path("bin") String binId);


    @GET("b/{bin}/latest")
    Call<ScoreResponse> getHighScore(@Path("bin") String binId);

    @PUT("b/{bin}")
    Call<ScoreResponse> updateHighScore(@Path("bin") String binId, @Body ScoreResponse highScoreResponse);
}
