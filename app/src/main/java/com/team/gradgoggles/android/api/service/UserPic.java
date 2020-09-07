package com.team.gradgoggles.android.api.service;


import com.team.gradgoggles.android.api.model.AccessToken;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserPic {

    @Multipart
    @POST(".")
    Call<AccessToken>upload(@Part("acl") RequestBody acl,
                            @Part("Content-Type") RequestBody content_type,
                            @Part("key")RequestBody key,
                            @Part("x-amz-algorithm")RequestBody xamzalgorithm,
                            @Part("x-amz-credential")RequestBody xamzcredential,
                            @Part("x-amz-date")RequestBody xamzdate,
                            @Part("policy")RequestBody policy,
                            @Part("x-amz-signature")RequestBody xamzsignature,
                            @Part MultipartBody.Part file);
}
