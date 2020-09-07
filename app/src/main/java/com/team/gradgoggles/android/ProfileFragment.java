package com.team.gradgoggles.android;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    String token;
    TokenManager tokenManager;
    UserClient userClient;
    ImageView profilePic;
    TextView profileName;
    TextView profileDepartment;
    TextView profileQuote;
    TextView profileEmail;
    TextView profileDob;
    TextView profileEdit;
    Call<HomeClass> call;
    HomeClass currentUser;
    View rootView;
    FrameLayout rootLayout;
    int netConnection =1;
    ImageView loadIndicator;
    LinearLayout details;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        loadIndicator=rootView.findViewById(R.id.load_indicator);
        details=rootView.findViewById(R.id.details);
        details.setVisibility(View.INVISIBLE);

        rootLayout = rootView.findViewById(R.id.container);
        userClient = RetrofitBuilder.createService(UserClient.class);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        profilePic = (ImageView)rootView.findViewById(R.id.profile_pic_image);
        profileName=(TextView)rootView.findViewById(R.id.profile_name);
        profileDepartment=(TextView)rootView.findViewById(R.id.profile_department);
        profileQuote=(TextView)rootView.findViewById(R.id.profile_quote);
        profileEmail=(TextView)rootView.findViewById(R.id.profile_mail);
        profileEdit=(TextView)rootView.findViewById(R.id.profile_edit);
        profileDob=(TextView)rootView.findViewById(R.id.profile_dob);

        InsertProfile();

        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(netConnection==1) {
                    startActivity(new Intent(getActivity(), EditProfile.class));
                    ((MainActivity)getActivity()).prevFrag =2;
                }

            }
        });

        return rootView;
    }
    void InsertProfile(){

        loadIndicator.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.blink));


        token="Bearer "+tokenManager.getAccess();
        call = userClient.currentUser(token,null,1,1,1,1,1,1);
        call.enqueue(new Callback<HomeClass>() {
            @Override
            public void onResponse(Call<HomeClass> call, Response<HomeClass> response) {
                if (response.isSuccessful()){
                    loadIndicator.clearAnimation();
                    loadIndicator.setVisibility(View.INVISIBLE);
                    details.setVisibility(View.VISIBLE);
                    Log.w(TAG, "onResponse: " + response.body());
                    currentUser=response.body();


                    Picasso.get().load(currentUser.getMphoto()).transform(new CropCircleTransformation()).placeholder(R.drawable.progress_animation).into(profilePic);
                    profileName.setText(currentUser.getMname());
                    profileDepartment.setText(currentUser.getMdepartment());
                    profileQuote.setText(currentUser.getMquote());
                    profileEmail.setText(currentUser.getMemail());
                    String rdob = currentUser.getMdob().split(" ")[0];
                    try {
                        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(rdob);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String strDate= formatter.format(date1);
                        profileDob.setText(strDate);
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
                    netConnection=0;
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