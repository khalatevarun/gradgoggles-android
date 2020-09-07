package com.team.gradgoggles.android.api.model;

import com.squareup.moshi.Json;

public class AccessToken {
   @Json(name="name")
   String name;

   @Json(name = "access_token")
   String accessToken;

   @Json(name = "photo")
   String photoUrl;

   @Json(name = "is2020")
   Boolean is2020;

   @Json(name = "isVerified")
   Boolean isVerified;





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
   public String getAccessToken(){ return accessToken; }
   public String getPhotoUrl(){ return photoUrl;}
   public String getName(){
      return name;
   }
   public Boolean getIs2020(){return is2020;}
   public Boolean getIsVerified(){return isVerified;}
   public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
   }
   public void setName(String name){this.name=name;}
   public void setPhoto(String photoUrl){this.photoUrl=photoUrl;}


}