package com.team.gradgoggles.android.api.model;

import com.google.gson.annotations.SerializedName;

public class ProfilePic {

    @SerializedName(value = "data")
    s3Data data;
    @SerializedName(value= "url")
    String url;

    public String getDataUrl(){
        return url;
    }
    public String gets3Url(){
        return data.url;
    }
    public String getAcl(){
        return data.fields.getAcl();
    }
    public String getContentType(){
        return data.fields.getContent_type();
    }
    public String getKey(){
        return data.fields.getKey();
    }
    public String getxamzalgorithm(){
        return data.fields.getX_amz_algorithm();
    }
    public String getxamzcredential(){
        return data.fields.getX_amz_credential();
    }
    public String getxamzdate(){
        return data.fields.getX_amz_date();
    }
    public String getpolicy(){
        return data.fields.getPolicy();
    }
    public String getxamzsignature(){
        return data.fields.getX_amz_signature();
    }



}
