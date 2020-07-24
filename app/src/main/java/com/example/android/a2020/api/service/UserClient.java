package com.example.android.a2020.api.service;

import com.example.android.a2020.api.model.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface UserClient {

    @Headers({
            "Accept-Encoding: gzip, deflate, br",
            "Accept: */*",

    })




    @POST("register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("email") String email, @Field("fullName")String fullname, @Field("password")String password);


    @POST("login")
    @FormUrlEncoded
    Call<AccessToken>login(@Field("email")String email, @Field("password")String password);


    @PUT("user")
    @FormUrlEncoded
    Call<AccessToken>update(@Header ("Authorization")String token,@Header("Content-Type")String ct, @Field("dob")String dob , @Field("GRNo")String grno, @Field("dept")String dept);




}