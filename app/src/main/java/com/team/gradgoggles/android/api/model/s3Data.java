package com.team.gradgoggles.android.api.model;

import com.google.gson.annotations.SerializedName;

public class s3Data {

    @SerializedName(value="url")
   public String url;
    @SerializedName(value="fields")
   public s3Fields fields;

        }
