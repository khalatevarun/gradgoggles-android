package com.team.gradgoggles.android.api.service;

import com.team.gradgoggles.android.HomeClass;
import com.team.gradgoggles.android.api.model.AccessToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface UserClient {

    @Headers({
            "Accept-Encoding: gzip, deflate, br",
            "Accept: */*",

    })




    @POST("register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("email") String email, @Field("fullName")String fullname, @Field("password")String password, @Field("is2020") String is2020, @Field("dob")String dob,@Field("dept")String dept,@Field("grno")String grno,@Field("quote")String quote);


    @POST("login")
    @FormUrlEncoded
    Call<AccessToken>login(@Field("email")String email, @Field("password")String password);



    @GET("check_verification")
    Call<AccessToken>checkVerification(@Header("Authorization")String token);



    @GET("resend_email")
    Call<AccessToken>resendMail(@Header("Authorization")String token);


    @PUT("user")
    @FormUrlEncoded
    Call<AccessToken>update(@Header ("Authorization")String token,@Field("name")String name,@Field("dept")String dept, @Field("dob")String dob, @Field("quote")String quote ,  @Field("photo")String url  );

    @GET("sign_s3")
    Call<String>profilepic(@Header("Authorization")String token, @Query("file_name") String file_name, @Query("file_type") String file_type );

    @GET("usersandroid")
   Call<List<HomeClass>> users(@Header("Authorization") String token, @Query("page") int pageno);

    @GET("user")
    Call<Object>scraps(@Header("Authorization")String token, @Query("id")String id, @Query("scraps")String scraps);

    @GET("searchPaginated")
    Call<List<HomeClass>>thisuser(@Header("Authorization")String token, @Query("query")String name,@Query("page")int pageno);

    @GET("users")
    Call<List<HomeClass>>thisdept(@Header("Authorization")String token,@Query("dept")String dept, @Query("page")int pageno);

    @GET("user")
    Call<HomeClass>currentUser(@Header("Authorization")String token,@Query("id")String id,@Query("photo")int photo,@Query("name")int name,@Query("dept")int department,@Query("quote")int quote,@Query("email")int email,@Query("dob")int dob);

    @POST("forgotPasswordSendMail")
    @FormUrlEncoded
    Call<AccessToken>forgotPassword(@Field("email")String email);

    @POST("createScrap")
    @FormUrlEncoded
    Call<AccessToken>createScrap(@Header("Authorization")String token, @Field("posted_to_id") String id,@Field("content") String content);


    @POST("change_password")
    @FormUrlEncoded
    Call<AccessToken>Password(@Header("Authorization")String token, @Field("current_password") String currentPassword, @Field("new_password") String newPassword);

    @PUT("toggleScrapVisibility")
    Call<AccessToken>ScrapVisibility(@Header("Authorization")String token, @Query("id") String id);
}