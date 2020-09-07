package com.team.gradgoggles.android;

import com.squareup.moshi.Json;

public class HomeClass {


    @Json(name="id")
    private String mid;
    @Json(name = "name")
    private String mname;
    @Json(name = "dept")
    private String mdepartment;
    @Json(name = "quote")
    private String mquote;
    @Json(name = "email")
    private String memail;
    @Json(name="gr")
    private String mgr;
    @Json(name="dob")
    private String mdob;
    @Json(name="photo")
    private String mphoto;

    public HomeClass(String photo,String name, String department, String quote){
        mphoto=photo;
        mname=name;
        mdepartment=department;
        mquote=quote;
    }
    public HomeClass(String id,String photo, String name, String department, String quote,String dob, String email){
        mid=id;
        mphoto=photo;
        mname=name;
        mdepartment=department;
        mquote=quote;
        mdob=dob;
        memail=email;
    }
    public String getMid(){return mid;}
    public String getMphoto(){return mphoto;}
    public String getMname(){return mname;}
    public String getMdepartment(){return mdepartment;}
    public String getMquote(){return mquote;}
    public String getMdob(){return mdob;}
    public String getMemail(){return memail;}
}
