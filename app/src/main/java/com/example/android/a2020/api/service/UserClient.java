package com.example.android.a2020.api.service;

import com.example.android.a2020.api.model.Login;
import com.example.android.a2020.api.model.AccessToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;




public interface UserClient {
    @Headers({
            "Accept-Encoding: gzip, deflate, br",
            "Accept: */*"
    })

    @POST("login") Call<AccessToken> login(@Body Login login);

    @POST("register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("email")String email, @Field("fullName")String fullname, @Field("password")String password);




}