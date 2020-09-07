package com.team.gradgoggles.android;

import android.content.SharedPreferences;

import com.team.gradgoggles.android.api.model.AccessToken;

public class TokenManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private String is2020;
    private String isverified;

    private static TokenManager INSTANCE = null;
    String atoken;

    private TokenManager (SharedPreferences prefs){
        this.prefs=prefs;
        this.editor=prefs.edit();
    }

    static synchronized TokenManager getInstance(SharedPreferences prefs){
        if (INSTANCE == null){
            INSTANCE=new TokenManager(prefs);

        }
        return INSTANCE;
    }

    public void saveToken(AccessToken token){
        editor.putString("ACCESS_TOKEN",token.getAccessToken()).commit();
        editor.putString("NAME",token.getName()).commit();
        editor.putString("PHOTO",token.getPhotoUrl()).commit();
        if (token.getIs2020() == true)
        {
         is2020="yes";
        }
        else{
            is2020="no";
        }
        editor.putString("IS2020",is2020).commit();
        if(token.getIsVerified() == true)
        {
            isverified = "yes";
        }
        else
        {
            isverified = "no";
        }
        editor.putString("ISVERIFIED",isverified).commit();
    }

    public void deleteToken(){
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("NAME").commit();
        editor.remove("PHOTO").commit();
    }

    public AccessToken getToken(){
        AccessToken token = new AccessToken();
        token.setAccessToken(prefs.getString("ACCESS_TOKEN", null));
        token.setName(prefs.getString("NAME", null));
        token.setPhoto(prefs.getString("PHOTO",null));
        return token;
    }
    public String getAccess(){
        AccessToken token = new AccessToken();
        return prefs.getString("ACCESS_TOKEN",null);
    }
    public String getName(){
        AccessToken token = new AccessToken();
        return prefs.getString("NAME",null);
    }
    public String getPhoto(){
        AccessToken token = new AccessToken();
        return prefs.getString("PHOTO",null);
    }
    public void setPhoto(String photoUrl){
        editor.putString("PHOTO",photoUrl).commit();
    }
    public void setName(String name){editor.putString("NAME",name).commit();}

    public String getis2020(){
        AccessToken token = new AccessToken();
        return prefs.getString("IS2020",null);
    }
    public String getIsverified(){
        AccessToken token = new AccessToken();
        return prefs.getString("ISVERIFIED",null);
    }



}
