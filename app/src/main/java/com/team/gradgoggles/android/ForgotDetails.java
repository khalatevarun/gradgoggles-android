package com.team.gradgoggles.android;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team.gradgoggles.android.api.model.AccessToken;
import com.team.gradgoggles.android.api.service.RetrofitBuilder;
import com.team.gradgoggles.android.api.service.UserClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotDetails extends AppCompatActivity {
    private static String TAG = "ForgotDetails";


    @BindView(R.id.forgot_email)
    TextInputLayout email;

    UserClient userClient;
    CircularProgressButton btn;
    Call<AccessToken> call;
    int buttonAnimation =0;
    RelativeLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        userClient= RetrofitBuilder.createService(UserClient.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_details);
        rootLayout = findViewById(R.id.container);
        ButterKnife.bind(this);


        TextView forgotPassword = (TextView) findViewById(R.id.forgot_password);


        // Initialize a new ObjectAnimator
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(forgotPassword, "alpha",  0.2f, 1f);
        fadeIn.setDuration(1000);

        ObjectAnimator textViewAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(ForgotDetails.this, R.animator.slideup_welcome);
        textViewAnimator.setTarget(forgotPassword);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(fadeIn,textViewAnimator);
        animatorSet.start();
        btn = (CircularProgressButton)findViewById(R.id.send_mail);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.startAnimation();
                buttonAnimation=1;
                sendMail();
            }
        });
    }

    void sendMail(){
        String mailAddress= email.getEditText().getText().toString();
        email.setError(null);

            call = userClient.forgotPassword(mailAddress);
            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        String responseMessage = response.body().getMsg().toString();
                        if(responseMessage.equals("email does not exist, please sign up")) {
                            Toast.makeText(ForgotDetails.this, "email does not exist, please sign up", Toast.LENGTH_SHORT).show();
                            if(buttonAnimation==1) {
                                btn.revertAnimation();
                            }
                        }
                        else{
                            Toast.makeText(ForgotDetails.this, "mail has been send", Toast.LENGTH_SHORT).show();


                        }

                    }
                    else {

                        if(buttonAnimation==1) {
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
                    if(buttonAnimation==1) {
                        btn.revertAnimation();
                    }

                }
            });
        }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        btn.dispose();

        if(call!=null ){
            call.cancel();
            call=null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent forgot = new Intent(ForgotDetails.this,LoginActivity.class);
        startActivity(forgot);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}