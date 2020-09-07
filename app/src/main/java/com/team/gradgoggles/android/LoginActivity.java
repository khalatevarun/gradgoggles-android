package com.team.gradgoggles.android;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.team.gradgoggles.android.api.model.AccessToken;
import com.team.gradgoggles.android.api.model.ApiError;
import com.team.gradgoggles.android.api.service.RetrofitBuilder;
import com.team.gradgoggles.android.api.service.UserClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
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

    UserClient userClient,userClient2,userClient3;
    Call<AccessToken> call,call2,call3;

    AwesomeValidation validator;
    TokenManager tokenManager;

    RelativeLayout rootLayout;

    CircularProgressButton btn;
    String token;

    String message;

    LinearLayout forgotPassword;
    int buttonanimation =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        rootLayout=(RelativeLayout)findViewById(R.id.container);

        ButterKnife.bind(this);
        userClient=RetrofitBuilder.createService(UserClient.class);
        userClient2=RetrofitBuilder.createService(UserClient.class);
        userClient3=RetrofitBuilder.createService(UserClient.class);


        tokenManager=TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));

        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        setupRules();


        if(tokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }


        TextView welcome = (TextView) findViewById(R.id.welcome_back_text);


        // Initialize a new ObjectAnimator
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(welcome, "alpha",  0.2f, 1f);
        fadeIn.setDuration(1000);

        ObjectAnimator textViewAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(LoginActivity.this, R.animator.slideup_welcome);
        textViewAnimator.setTarget(welcome);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(fadeIn,textViewAnimator);
        animatorSet.start();


        forgotPassword = (LinearLayout)findViewById(R.id.forgot_login);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot = new Intent(LoginActivity.this,ForgotDetails.class);
                        startActivity(forgot);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });

        LinearLayout redirect_to_signUp = (LinearLayout) findViewById(R.id.redirect_to_signup);
        // Set a click listener on that View

        redirect_to_signUp.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the signup is clicked on.
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(LoginActivity.this, SignUp1.class);

                // Start the new activity

                startActivity(loginIntent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });
         btn = (CircularProgressButton) findViewById(R.id.login_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });


    }


    void login(){


        String username= login_email.getEditText().getText().toString();
        String password=login_password.getEditText().getText().toString();

        login_email.setError(null);
        login_password.setError(null);

        validator.clear();
        if(validator.validate()){
            buttonanimation=1;
            btn.startAnimation();
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
                            if (buttonanimation == 1) {
                                btn.revertAnimation();
                            }
                        }
                        else {

                            tokenManager.saveToken(response.body());
                             token = "Bearer "+tokenManager.getAccess();
                           checkIsVerified();


                        }






                    } else {
                        handleErrors(response.errorBody());
                       if(buttonanimation==1) {
                           btn.revertAnimation();
                       }

                    }

                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {

                    Log.w(TAG, "onFailure: " + t.getMessage());
                    if(t instanceof IOException){
                        Snackbar snackbar = Snackbar.make(rootLayout,"Check your internet connection.",Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                  if(buttonanimation==1)
                  {
                   btn.revertAnimation();
                }
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
        validator.addValidation(this, R.id.login_password, "[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$\\\\&\\\\*\\\\#\\\\%\\\\^\\\\(\\\\)\\\\+\\\\-\\\\=\\\\.\\\\'\\\\:\\\\_\\\\<\\\\>\\\\?\\\\/]{6,}", R.string.login_err_password);
       // btn.stopAnimation();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call!=null ){
            call.cancel();
            call=null;
        }
        if(call2!=null){
            call2.cancel();
            call2=null;
        }
        if(call3!=null ){
            call3.cancel();
            call3=null;
        }
    }
    void checkIsVerified(){
        call2 = userClient.checkVerification(token);
        call2.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if(response.isSuccessful()) {

                    if(response.body().getMsg().equals("no"))
                    {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                        dialog.setMessage("Oops, looks like somebody hasn't verified their account. Click on Resend Email to send the verification email again.");
                        dialog.setTitle("Verification Pending");
                        dialog.setPositiveButton("RESEND MAIL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        resendMail();

                                        dialog.cancel();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                });
                        dialog.setNegativeButton("CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.cancel();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                });


                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();

                    }
                    else{
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                if(call.isCanceled()){
                    return;
                }
                else {
                    Snackbar snackbar = Snackbar.make(rootLayout,"Check your internet connection.",Snackbar.LENGTH_LONG);

                    snackbar.show();
                    if (buttonanimation == 1) {
                        btn.revertAnimation();
                    }
                }
            }
        });


    }
   /** void checkIsVerified()
    {
        call2 = userClient2.checkVerification(token);
        call2.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    if ((response.body().getMsg()).equals("no")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                        dialog.setMessage("Oops, looks like somebody hasn't verified their account. Click on Resend Email to send the verification email again.");
                        dialog.setTitle("Verification Pending");
                        dialog.setPositiveButton("Resend Email",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        resendMail();
                                        dialog.cancel();
                                    }
                                });
                        dialog.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();
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
                    btn.revertAnimation();
                }

            }
        });
    }
    **/
    void resendMail()
    {
        call3 = userClient3.resendMail(token);
        call3.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.w(TAG, "onResponse:ResendMail-- " + response.body());

            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                if (t instanceof IOException) {
                    Snackbar snackbar = Snackbar.make(rootLayout, "Check your internet connection.", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                if (buttonanimation == 1) {
                    btn.revertAnimation();
                }
            }
        });
    }
}