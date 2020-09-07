package com.team.gradgoggles.android;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputLayout;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUp1 extends AppCompatActivity {

    @BindView(R.id.til_email)
    TextInputLayout signUp_email;

    @BindView(R.id.til_name)
    TextInputLayout signUp_fullname;

    @BindView(R.id.til_password)
    TextInputLayout signUp_password;

    AwesomeValidation validator;


    CircularProgressButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);


        ButterKnife.bind(this);


        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        setupRules();
        TextView signUpText = (TextView) findViewById(R.id.signUp_text);

        // Initialize a new ObjectAnimator
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(signUpText, "alpha", 0.2f, 1f);
        fadeIn.setDuration(1000);

        ObjectAnimator textViewAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(SignUp1.this, R.animator.slideup_welcome);
        textViewAnimator.setTarget(signUpText);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeIn, textViewAnimator);
        animatorSet.start();


        LinearLayout redirect_to_login = (LinearLayout) findViewById(R.id.redirect_to_login);
        // Set a click listener on that View

        redirect_to_login.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the signup is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link SignUpActivity}
                Intent loginIntent = new Intent(SignUp1.this, LoginActivity.class);

                // Start the new activity

                startActivity(loginIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });
        btn = (CircularProgressButton) findViewById(R.id.signUp1_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUp();
            }
        });
    }

    void signUp() {
        String fullname = signUp_fullname.getEditText().getText().toString();
        String email = signUp_email.getEditText().getText().toString();
        String password = signUp_password.getEditText().getText().toString();

        signUp_fullname.setError(null);
        signUp_email.setError(null);

        signUp_password.setError(null);

        validator.clear();
        if (validator.validate()) {
            Intent intent = new Intent(SignUp1.this,SignUp2Activity.class);
            intent.putExtra("SIGNUP_fullname",fullname);
            intent.putExtra("SIGNUP_email",email);
            intent.putExtra("SIGNUP_password",password);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            finish();
        }
    }

    public void setupRules() {
        validator.addValidation(this, R.id.til_name, RegexTemplate.NOT_EMPTY, R.string.err_name);
        validator.addValidation(this, R.id.til_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
        validator.addValidation(this, R.id.til_password, "[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$\\\\&\\\\*\\\\#\\\\%\\\\^\\\\(\\\\)\\\\+\\\\-\\\\=\\\\.\\\\'\\\\:\\\\_\\\\<\\\\>\\\\?\\\\/]{6,}", R.string.err_password);

    }
}