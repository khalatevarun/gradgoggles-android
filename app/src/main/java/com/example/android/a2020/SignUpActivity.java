package com.example.android.a2020;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
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


public class SignUpActivity extends AppCompatActivity  {



    private static final String TAG = "SignUpActivity";

    @BindView(R.id.til_email)
    TextInputLayout signUp_email;

    @BindView(R.id.til_name)
    TextInputLayout signUp_fullname;

    @BindView(R.id.til_password)
    TextInputLayout signUp_password;

    UserClient userClient;
    Call<AccessToken> call;

    AwesomeValidation validator;
    TokenManager tokenManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);






        ButterKnife.bind(this);

        userClient= RetrofitBuilder.createService(UserClient.class);

        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        setupRules();

       /** if(tokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(SignUpActivity.this, WelcomeActivity.class));
            finish();

        }
        **/

        TextView signUpText = (TextView) findViewById(R.id.signUp_text);

        // Initialize a new ObjectAnimator
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(signUpText, "alpha",  0.2f, 1f);
        fadeIn.setDuration(1000);

        ObjectAnimator textViewAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(SignUpActivity.this, R.animator.slideup_welcome);
        textViewAnimator.setTarget(signUpText);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(fadeIn,textViewAnimator);
        animatorSet.start();



        TextView redirect_to_login = (TextView)findViewById(R.id.redirect_to_login);
        // Set a click listener on that View

        redirect_to_login.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the signup is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link SignUpActivity}
                Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);

                // Start the new activity

                startActivity(loginIntent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });
    }






    @OnClick(R.id.signUp1_button)
      void signUp() {
          String fullname = signUp_fullname.getEditText().getText().toString();
          String email = signUp_email.getEditText().getText().toString();
          String password = signUp_password.getEditText().getText().toString();

          signUp_fullname.setError(null);
          signUp_email.setError(null);

          signUp_password.setError(null);

          validator.clear();
          if (validator.validate()) {
              call = userClient.register(email, fullname, password);





              call.enqueue(new Callback<AccessToken>() {
                  @Override
                  public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                      Log.w(TAG, "onResponse: " + response);

                      if (response.isSuccessful()) {
                          Log.w(TAG, "onResponse: " + response.body());


                            String responseMessage = response.body().getErrorMessage();
                            if(responseMessage.equals("User already exists")) {
                                Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                tokenManager.saveToken(response.body());
                                startActivity(new Intent(SignUpActivity.this, SignUp2Activity.class));
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

        for(Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()) {

            if(error.getKey().equals("email")){
                signUp_email.setError(error.getValue().get(0));
            }
            if(error.getKey().equals("name")){
                signUp_fullname.setError(error.getValue().get(0));
            }
            if(error.getKey().equals("password")){
                signUp_password.setError(error.getValue().get(0));
            }



        }

      }
    public void setupRules(){
        validator.addValidation(this, R.id.til_name, RegexTemplate.NOT_EMPTY, R.string.err_name);
        validator.addValidation(this, R.id.til_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
        validator.addValidation(this, R.id.til_password, "[a-zA-Z0-9]{6,}", R.string.err_password);

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