package com.team.gradgoggles.android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.gradgoggles.android.api.service.RetrofitBuilder;
import com.team.gradgoggles.android.api.service.UserClient;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class StudentProfileFragment extends Fragment {
    private static final String TAG = "StudentProfileFragment";

    String token;
    TokenManager tokenManager;
    UserClient userClient;
    ImageView studentPic;
    TextView studentName;
    TextView studentDepartment;
    TextView studentQuote;
    TextView studentEmail;
    TextView studentDob;

    Call<HomeClass> call;
    HomeClass currentUser;
    String studentID;
    View rootView;
    FrameLayout rootLayout;

    RelativeLayout studentDetails;
    ImageView gcaps;

    public StudentProfileFragment(){}





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Student activity = (Student) getActivity();
         studentID =     activity.getFinal_studentId();


         rootView = inflater.inflate(R.layout.fragment_student_profile, container, false);
         rootLayout = rootView.findViewById(R.id.container);

         studentDetails = rootView.findViewById(R.id.student_details);
         studentDetails.setVisibility(View.INVISIBLE);
         gcaps  = rootView.findViewById(R.id.gcaps);



        userClient = RetrofitBuilder.createService(UserClient.class);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        studentPic = (ImageView)rootView.findViewById(R.id.student_image);
        studentName=(TextView)rootView.findViewById(R.id.student_name);

        studentDepartment=(TextView)rootView.findViewById(R.id.student_department);
        studentQuote=(TextView)rootView.findViewById(R.id.student_quote);
        studentEmail=(TextView)rootView.findViewById(R.id.student_mail);
        studentDob=(TextView)rootView.findViewById(R.id.student_dob);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        InsertStudent();

        return rootView;

    }




    void InsertStudent(){

        gcaps.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.blink));
        token="Bearer "+tokenManager.getAccess();
        call = userClient.currentUser(token,studentID,1,1,1,1,1,1);
        call.enqueue(new Callback<HomeClass>() {
            @Override
            public void onResponse(Call<HomeClass> call, Response<HomeClass> response) {
                if (response.isSuccessful()){
                    Log.w(TAG, "onResponse: " + response.body());

                    gcaps.clearAnimation();
                    gcaps.setVisibility(View.INVISIBLE);
                    studentDetails.setVisibility(View.VISIBLE);


                    currentUser=response.body();

                    Picasso.get().load(currentUser.getMphoto()).transform(new CropCircleTransformation()).placeholder(R.drawable.progress_animation).into(studentPic);
                    studentName.setText(currentUser.getMname());
                    studentDepartment.setText(currentUser.getMdepartment());
                    studentQuote.setText(currentUser.getMquote());
                    studentEmail.setText(currentUser.getMemail());
                    String rdob = currentUser.getMdob().split(" ")[0];
                    try {
                        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(rdob);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String strDate= formatter.format(date1);
                        studentDob.setText(strDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }




                }
            }

            @Override
            public void onFailure(Call<HomeClass> call, Throwable t) {
                if(call.isCanceled()){
                    return;
                }
                else {
                    Snackbar snackbar = Snackbar.make(rootLayout,"Check your internet connection.",Snackbar.LENGTH_LONG);

                    snackbar.show();
                }

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        if(call!= null && call.isExecuted()) {
            call.cancel();
        }
    }
}
