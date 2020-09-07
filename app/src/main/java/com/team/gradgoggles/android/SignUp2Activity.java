package com.team.gradgoggles.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class SignUp2Activity extends AppCompatActivity {



    private static final String TAG = "SignUp2Activity";

    String[] department = {"Computer", "Chemical", "Elex", "Industrial", "Instrumentation", "IT", "Mechanical", "Production"};
    DatePickerDialog dob;
    EditText dob_text;
    EditText grno_text;
    Spinner dept;
    String final_dept;
    CircularProgressButton nextbutton;
    String fullname;
    String email;
    String password;
    String grno_string;
    String dob_string;


  // UserClient userClient;
  //  Call<AccessToken> call;

   // TokenManager tokenManager;
   // int buttonanimation =0;

   // RelativeLayout rootLayout;






    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        fullname = getIntent().getStringExtra("SIGNUP_fullname");
        email = getIntent().getStringExtra("SIGNUP_email");
        password = getIntent().getStringExtra("SIGNUP_password");

       ///  rootLayout=(RelativeLayout)findViewById(R.id.container);


      //  userClient = RetrofitBuilder.createService(UserClient.class);
       // tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        nextbutton = (CircularProgressButton)findViewById(R.id.signUp2_button);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     buttonanimation=1;
           //     nextbutton.startAnimation();
                signUp();
            }
        });





        dept = (Spinner) findViewById(R.id.signUp_department);
        List<String> categories =  new ArrayList<>();
        categories.add("Computer");
        categories.add("IT");
        categories.add("Elex");
        categories.add("EnTC");
        categories.add("Chemical");
        categories.add("Instrumentation");
        categories.add("Mechanical");
        categories.add("Industrial");
        categories.add("MCA");

        ArrayAdapter<String> departmentAdapter = new ArrayAdapter(SignUp2Activity.this, R.layout.spinner_item,categories);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept.setAdapter(departmentAdapter);

        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    final_dept = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        dob_text = (EditText) findViewById(R.id.signUp_dob);
        dob_text.setInputType(InputType.TYPE_NULL);
        dob_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"dob clicked");


                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                dob = new DatePickerDialog(SignUp2Activity.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if(monthOfYear<10 && dayOfMonth<10) {
                                    dob_text.setText("0"+dayOfMonth + "-" +"0"+ (monthOfYear + 1) + "-" + year);

                                }
                                else if(dayOfMonth<10){
                                    dob_text.setText("0"+dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
                                }
                                else if(monthOfYear<10)
                                {
                                    dob_text.setText(dayOfMonth + "-" +"0"+ (monthOfYear + 1) + "-" + year);

                                }
                                if(dayOfMonth>10 && monthOfYear>10) {
                                    dob_text.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }
                            }
                        }, year, month, day);
                dob.show();
            }
        });

        grno_text = (EditText) findViewById(R.id.signUp_grno);



    }





    void signUp() {
          Log.d(TAG,"button has been clicked");



          grno_string = grno_text.getEditableText().toString().trim();

          dob_string =dob_text.getText().toString().trim();

        Intent intent = new Intent(SignUp2Activity.this,SignUp3Activity.class);
        intent.putExtra("SIGNUP_fullname",fullname);
        intent.putExtra("SIGNUP_email",email);
        intent.putExtra("SIGNUP_password",password);
        intent.putExtra("SIGNUP_DOB",dob_string);
        intent.putExtra("SIGNUP_GRNO",grno_string);
        intent.putExtra("SIGNUP_DEPT",final_dept);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out,R.anim.slide_in);
        finish();





       // String token="Bearer "+tokenManager.getAccess();
       //  call = userClient.update(token,dob_string, grno_string, final_dept,null,null,null);


        /** call.enqueue(new Callback<AccessToken>() {
        @Override
        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {


        if (response.isSuccessful()) {
        Log.w(TAG, "onResponse: " + response.body());

        String responseMessage = response.body().getMsg();
        if (responseMessage.equals("Updation Successful")) {
        startActivity(new Intent(SignUp2Activity.this, SignUp3Activity.class));
           // overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
        finish();
        } else {
        Toast.makeText(SignUp2Activity.this, "Check entries", Toast.LENGTH_SHORT).show();
        if(buttonanimation==1) {
            nextbutton.stopAnimation();
        }

        }
        } else {
        // handleErrors(response.errorBody());
        }
        }

        @Override
        public void onFailure(Call<AccessToken> call, Throwable t) {
        Log.w(TAG, "onFailure: " + t.getMessage());
            if(t instanceof IOException){
                Snackbar snackbar = Snackbar.make(rootLayout,"Check your internet connection.",Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        if(buttonanimation==1) {
            nextbutton.revertAnimation();
        }

        }
        });
         }
         **/



         /**  Log.d(TAG,"button has been clicked");


            String grno_string = grno_text.getEditableText().toString().trim();

            String dob_string = dob.toString();





            call = userClient.update(dob_string, grno_string, final_dept);


            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        Log.w(TAG, "onResponse: " + response.body());

                        String responseMessage = response.body().getMsg();
                        if (responseMessage.equals("Updation Successful")) {
                            startActivity(new Intent(SignUp2Activity.this, SignUp3Activity.class));
                            finish();
                        } else {
                            Toast.makeText(SignUp2Activity.this, "Check data types", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        // handleErrors(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                }
            });


        }
        **/





/**
    @Override
    protected void onDestroy() {
        super.onDestroy();
        nextbutton.dispose();
        if(call!=null ){
            call.cancel();
            call=null;
        }
    }
    **/

}
}