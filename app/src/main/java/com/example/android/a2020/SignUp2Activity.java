package com.example.android.a2020;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.android.a2020.api.model.AccessToken;
import com.example.android.a2020.api.model.ApiError;
import com.example.android.a2020.api.service.RetrofitBuilder;
import com.example.android.a2020.api.service.UserClient;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp2Activity extends AppCompatActivity {



    private static final String TAG = "SignUp2Activity";

    String[] department = {"Computer", "Chemical", "Elex", "Industrial", "Instrumentation", "IT", "Mechanical", "Production"};
    DatePickerDialog dob;
    EditText dob_text;
    EditText grno_text;
    Spinner dept;
    String final_dept;
    Button nextbutton;


   UserClient userClient;
    Call<AccessToken> call;

    TokenManager tokenManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        userClient = RetrofitBuilder.createService(UserClient.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        nextbutton = (Button)findViewById(R.id.signUp2_button);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });





        dept = (Spinner) findViewById(R.id.signUp_department);
        List<String> categories =  new ArrayList<>();
        categories.add(0,"Department");
        categories.add("Computer");
        categories.add("IT");
        categories.add("Elex");
        categories.add("Chemical");
        categories.add("Instrumentation");
        categories.add("Mechanical");
        categories.add("Industrial");

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
                                if(monthOfYear<10){
                                    dob_text.setText(dayOfMonth + "-" +"0"+ (monthOfYear + 1) + "-" + year);

                                }
                                if(dayOfMonth<10){
                                    dob_text.setText("0"+dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
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



         String grno_string = grno_text.getEditableText().toString().trim();

         String dob_string =dob_text.getText().toString().trim();




        String ct =  "application/x-www-form-urlencoded";
        String token="Bearer "+tokenManager.getAccess();
         call = userClient.update(token,ct,dob_string, grno_string, final_dept);


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






    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call!=null ){
            call.cancel();
            call=null;
        }
    }

}