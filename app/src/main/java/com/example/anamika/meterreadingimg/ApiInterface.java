package com.example.anamika.meterreadingimg;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("/posts")
    @FormUrlEncoded
    Call<Post> savePost(@Field("title") String title);
}
