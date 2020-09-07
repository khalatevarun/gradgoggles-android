package com.team.gradgoggles.android;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.team.gradgoggles.android.api.model.AccessToken;
import com.team.gradgoggles.android.api.model.ProfilePic;
import com.team.gradgoggles.android.api.service.RetrofitBuilder;
import com.team.gradgoggles.android.api.service.RetrofitBuilder2;
import com.team.gradgoggles.android.api.service.UserClient;
import com.team.gradgoggles.android.api.service.UserPic;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUp3Activity extends AppCompatActivity {


    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    private Gson gson;

    private static final String TAG = "SignUp3Activity";

    private ImageView imageView;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String filetype;
    String filname;
    EditText eQuote;
    CheckBox is2020;
    String filepath;
    String sQuote;
    String token;
    String ct;
    String graduate2020 = "false";

    UserClient userClient;
    UserPic userPic;
    Call<String> call;
    Call<AccessToken> call2;
    Call<AccessToken> call1;

    CircularProgressButton nextbutton;

    TokenManager tokenManager;
    ProfilePic pp = new ProfilePic();
    int buttonanimation = 0;

    RelativeLayout rootLayout;

    String fullname;
    String email;
    String password;
    String dob;
    String dept;
    String grno;

    int dpUploaded = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        fullname = getIntent().getStringExtra("SIGNUP_fullname");
        email = getIntent().getStringExtra("SIGNUP_email");
        password = getIntent().getStringExtra("SIGNUP_password");
        dob = getIntent().getStringExtra("SIGNUP_DOB");
        grno = getIntent().getStringExtra("SIGNUP_GRNO");
        dept = getIntent().getStringExtra("SIGNUP_DEPT");


        rootLayout = (RelativeLayout) findViewById(R.id.container);


        eQuote = (EditText) findViewById(R.id.signUp_quote);
        eQuote.setEnabled(false);
        eQuote.setEnabled(false);
        eQuote.setInputType(InputType.TYPE_NULL);

        imageView = (ImageView) findViewById(R.id.signUp_pic);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        userClient = RetrofitBuilder.createService(UserClient.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        is2020 = (CheckBox) findViewById(R.id.is2020);
        is2020.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is2020.isChecked()) {
                    eQuote.setEnabled(true);
                    eQuote.setFocusable(true);
                    eQuote.setInputType(InputType.TYPE_CLASS_TEXT);
                    graduate2020 = "true";
                } else {
                    eQuote.setEnabled(false);
                    eQuote.setEnabled(false);
                    eQuote.setInputType(InputType.TYPE_NULL);
                    graduate2020 = "false";

                }

            }
        });


        nextbutton = (CircularProgressButton) findViewById(R.id.signUp3_button);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonanimation = 1;
                nextbutton.startAnimation();
                signUp();
            }
        });

    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (SignUp3Activity.this.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            filetype = getContentResolver().getType(imageUri);
            filepath = getRealPathFromUri(imageUri);
            dpUploaded = 1;
            imageView.setImageURI(imageUri);
        }
    }

    public void signUp() {
        sQuote = eQuote.getEditableText().toString().trim();
        register();


    }

    public void profileupload() {
        if (dpUploaded == 1){
            token = "Bearer " + tokenManager.getAccess();
        ct = "application/x-www-form-urlencoded";
        filname = getRandomString(10);
        call = userClient.profilepic(token, filname, filetype);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    Log.w(TAG, "onResponse: " + response.body());
                    Gson gson = new Gson();
                    pp = gson.fromJson(response.body(), ProfilePic.class);

                    File file = new File(filepath);    // create new file on device
                    RequestBody reqFile = RequestBody.create(file, MediaType.parse("image/*"));

                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", filname, reqFile);

                    RequestBody acl = RequestBody.create(pp.getAcl(), MediaType.parse("multipart/form-data"));
                    RequestBody contentType = RequestBody.create(pp.getContentType(), MediaType.parse("multipart/form-data"));
                    RequestBody key = RequestBody.create(pp.getKey(), MediaType.parse("multipart/form-data"));
                    RequestBody algo = RequestBody.create(pp.getxamzalgorithm(), MediaType.parse("multipart/form-data"));
                    RequestBody cred = RequestBody.create(pp.getxamzcredential(), MediaType.parse("multipart/form-data"));
                    RequestBody date = RequestBody.create(pp.getxamzdate(), MediaType.parse("multipart/form-data"));
                    RequestBody policy = RequestBody.create(pp.getpolicy(), MediaType.parse("multipart/form-data"));
                    RequestBody sign = RequestBody.create(pp.getxamzsignature(), MediaType.parse("multipart/form-data"));


                    userPic = RetrofitBuilder2.createService(UserPic.class);

                    call1 = userPic.upload(acl, contentType, key, algo, cred, date, policy, sign, body);
                    call1.enqueue(new Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call1, Response<AccessToken> response) {

                            if (response.isSuccessful()) {
                                Log.w(TAG, "onResponse: " + response.body());

                                startActivity(new Intent(SignUp3Activity.this, MainActivity.class));


                            }

                        }

                        @Override
                        public void onFailure(Call<AccessToken> call, Throwable t) {
                            if (call.isCanceled()) {
                                return;
                            } else {
                                Snackbar snackbar = Snackbar.make(rootLayout, "Check your internet connection.", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            }
                            if (buttonanimation == 1) {
                                nextbutton.revertAnimation();
                            }

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
                if (buttonanimation == 1) {
                    nextbutton.revertAnimation();
                }
            }
        });
    }
        else
            startActivity(new Intent(SignUp3Activity.this, MainActivity.class));
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nextbutton.dispose();
        if(call!=null ){
            call.cancel();
            call=null;
        }
    }



    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private String getRealPathFromUri(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor=loader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnIndex);
        cursor.close();
        return result;
    }



    private void register()
    {
        if(graduate2020.equals(false)){ sQuote=null;}

        call2 = userClient.register(email,fullname, password, graduate2020, dob, dept, grno, sQuote);
        call2.enqueue(new Callback<AccessToken>(){

            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    Log.w(TAG, "onResponse: " + response.body());
                    if (response.body().getErrorMessage().equals("User already exists")) {
                        Snackbar snackbar = Snackbar.make(rootLayout, "User already exists.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        if (buttonanimation == 1) {
                            nextbutton.revertAnimation();
                        }
                    } else {
                        tokenManager.saveToken(response.body());
                        tokenManager.setPhoto(response.body().getPhotoUrl());
                        profileupload();


                    }
                }
            }



            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                if(t instanceof IOException){
                    Snackbar snackbar = Snackbar.make(rootLayout,"Check your internet connection.",Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                if(buttonanimation==1)
                {
                    nextbutton.revertAnimation();
                }

            }
        });
    }


}