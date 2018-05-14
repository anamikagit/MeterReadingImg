package com.example.anamika.meterreadingimg;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("/posts")
    @FormUrlEncoded
    Call<List<ImageServerResponce>> sendingImage(@Field("compressedImageString") String temp);
}
