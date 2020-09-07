package com.team.gradgoggles.android.api.model;

import com.google.gson.annotations.SerializedName;

public class s3Fields{
    @SerializedName(value="acl")
    private String acl;
    @SerializedName(value="Content-Type")
    private String content_type;
    @SerializedName(value="key")
    private String key;
    @SerializedName(value="x-amz-algorithm")
    private String x_amz_algorithm;
    @SerializedName(value="x-amz-credential")
    private String x_amz_credential;
    @SerializedName(value="x-amz-date")
    private String x_amz_date;
    @SerializedName(value="policy")
    private String policy;
    @SerializedName(value="x-amz-signature")
    private String x_amz_signature;

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getX_amz_algorithm() {
        return x_amz_algorithm;
    }

    public void setX_amz_algorithm(String x_amz_algorithm) {
        this.x_amz_algorithm = x_amz_algorithm;
    }

    public String getX_amz_credential() {
        return x_amz_credential;
    }

    public void setX_amz_credential(String x_amz_credential) {
        this.x_amz_credential = x_amz_credential;
    }

    public String getX_amz_date() {
        return x_amz_date;
    }

    public void setX_amz_date(String x_amz_date) {
        this.x_amz_date = x_amz_date;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getX_amz_signature() {
        return x_amz_signature;
    }

    public void setX_amz_signature(String x_amz_signature) {
        this.x_amz_signature = x_amz_signature;
    }
}
