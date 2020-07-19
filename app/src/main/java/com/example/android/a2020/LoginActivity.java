package com.example.android.a2020;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
}