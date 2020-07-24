package com.example.android.a2020.api.model;

import com.squareup.moshi.Json;

public class AccessToken {
   @Json(name="name")
   String name;

   @Json(name = "access_token")
   String accessToken;


   @Json(name="refresh_token")
   String refreshToken;


   @Json(name="error")
   String errorMessage;

   @Json(name="msg")
   String msg;


   public  String getErrorMessage(){
      return errorMessage;
   }

   public String getMsg(){
      return msg;
   }
   public String getAccessToken(){
      return accessToken;

   }

   public String getRefreshToken(){

      return refreshToken;
   }

   public String getName(){
      return name;
   }
   public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
   }


   public void setRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
   }

}