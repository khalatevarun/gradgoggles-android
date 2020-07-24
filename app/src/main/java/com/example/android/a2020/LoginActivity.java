package com.example.android.a2020;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.android.a2020.api.model.AccessToken;
import com.example.android.a2020.api.model.ApiError;
import com.example.android.a2020.api.service.RetrofitBuilder;
import com.example.android.a2020.api.service.UserClient;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {



    private static String TAG = "LoginAcitvity";

    @BindView(R.id.login_email)
    TextInputLayout login_email;

    @BindView(R.id.login_password)
    TextInputLayout login_password;

    UserClient userClient;
    Call<AccessToken> call;

    AwesomeValidation validator;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        ButterKnife.bind(this);
        userClient=RetrofitBuilder.createService(UserClient.class);
        tokenManager=TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));

        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        setupRules();

       /** if(tokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
            finish();
        }
        */

        TextView welcome = (TextView) findViewById(R.id.welcome_back_text);

        // Initialize a new ObjectAnimator
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(welcome, "alpha",  0.2f, 1f);
        fadeIn.setDuration(1000);

        ObjectAnimator textViewAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(LoginActivity.this, R.animator.slideup_welcome);
        textViewAnimator.setTarget(welcome);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(fadeIn,textViewAnimator);
        animatorSet.start();

        TextView redirect_to_signUp = (TextView)findViewById(R.id.redirect_to_signup);
        // Set a click listener on that View

        redirect_to_signUp.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the signup is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link SignUpActivity}
                Intent loginIntent = new Intent(LoginActivity.this, SignUpActivity.class);

                // Start the new activity

                startActivity(loginIntent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });

    }

    @OnClick(R.id.login_button)
    void login(){

        String username= login_email.getEditText().getText().toString();
        String password=login_password.getEditText().getText().toString();

        login_email.setError(null);
        login_password.setError(null);

        validator.clear();
        if(validator.validate()){
           call = userClient.login(username,password);


            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        Log.w(TAG, "onResponse: " + response.body());


                        String responseMessage = response.body().getErrorMessage().toString();
                        if(responseMessage.equals("Email or password does not match")) {
                            Toast.makeText(LoginActivity.this, "Email or password does not match", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            tokenManager.saveToken(response.body());
                            startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                            finish();
                        }






                    } else {
                        handleErrors(response.errorBody());


                    }

                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {

                    Log.w(TAG, "onFailure: " + t.getMessage());
                }
            });

        }
    }
    private void handleErrors(ResponseBody response){

        ApiError apiError = Utils.convertErrors(response);

        for(Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()){
            if(error.getKey().equals("username")) {
                login_email.setError(error.getValue().get(0));
            }
            if(error.getKey().equals("password")){
                login_password.setError(error.getValue().get(0));
            }
        }
    }
    public void setupRules(){
        validator.addValidation(this, R.id.login_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
        validator.addValidation(this, R.id.login_password, "[a-zA-Z0-9]{1,}", R.string.login_err_password);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call!=null ){
            call.cancel();
            call=null;
        }
    }
}