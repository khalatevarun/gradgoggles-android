package com.team.gradgoggles.android;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.team.gradgoggles.android.api.model.AccessToken;
import com.team.gradgoggles.android.api.service.RetrofitBuilder;
import com.team.gradgoggles.android.api.service.UserClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class StudentScrapsFragment extends Fragment {
    private static final String TAG = "StudentScrapsFragment";

    ListView listView;
    String token;
    TokenManager tokenManager;
    UserClient userClient,userClient3;
    Call<Object> call;
    StudentsScrapAdapter studentsScrapAdapter;
    List<Scrap> scrapClassList = new ArrayList<>();
    ImageView send;
    Call<AccessToken> call2,call3;
    UserClient userClient2;
    EditText Scrapcontent;
    String content;



    String studentID;
    View rootView;
    FrameLayout rootLayout;

    LinearLayout studentScraps;
    ImageView gcaps;
    TextView emptyText;
    LinearLayout sendScrapFeature;




    public StudentScrapsFragment(){}




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        Student activity = (Student) getActivity();
        studentID =     activity.getFinal_studentId();

        rootView = inflater.inflate(R.layout.fragment_student_scraps, container, false);
        rootLayout = rootView.findViewById(R.id.container);

        studentScraps=rootView.findViewById(R.id.student_scraps);
        studentScraps.setVisibility(View.INVISIBLE);
        gcaps=rootView.findViewById(R.id.gcaps);
        emptyText = rootView.findViewById(R.id.emptyText);
        emptyText.setVisibility(View.INVISIBLE);


        userClient = RetrofitBuilder.createService(UserClient.class);
        userClient2 = RetrofitBuilder.createService(UserClient.class);
        userClient3 = RetrofitBuilder.createService(UserClient.class);


        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        listView = (ListView)rootView.findViewById(R.id.student_scrap_list);
        Log.w(TAG,"In StudentScrapsFragment");
        InsertStudentScraps();


        sendScrapFeature = rootView.findViewById(R.id.send_scrap_feature);
        Scrapcontent = (EditText)rootView.findViewById(R.id.write_scrap);
        send = rootView.findViewById(R.id.send_scrap);



         content  = Scrapcontent.getEditableText().toString().trim();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                checkVerification();
            }
        });

        return rootView;
    }

    void InsertStudentScraps(){

        gcaps.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.blink));


        token="Bearer "+tokenManager.getAccess();
        Log.w(TAG,"token is stored");
        call=userClient.scraps(token,studentID,"1");
        Log.w(TAG,"call is initialised");
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    gcaps.clearAnimation();
                    gcaps.setVisibility(View.INVISIBLE);
                    studentScraps.setVisibility(View.VISIBLE);



                    Log.w(TAG, "onResponse: " + new Gson().toJson(response.body()));
                    try {
                        JSONObject baseJsonResponse = new JSONObject(new Gson().toJson(response.body()));
                        JSONArray scrapsArray = baseJsonResponse.getJSONArray("scraps");

                        for (int i = 0; i < scrapsArray.length(); i++) {
                            JSONObject currentScrap = scrapsArray.getJSONObject(i);
                              JSONObject currentScrapPostedBy = currentScrap.getJSONObject("posted_by");
                              String name = currentScrapPostedBy.getString("name");
                              String photo = currentScrapPostedBy.getString("photo");
                             Integer id = currentScrapPostedBy.getInt("id");

                            String content = currentScrap.getString("content");
                            String timestamp = currentScrap.getString("timestamp");

                            PostedBy postedBy = new PostedBy(name, photo,id);

                            Scrap scrapClass = new Scrap(content,id, timestamp,postedBy);

                            scrapClassList.add(scrapClass);

                        }

                        if(scrapsArray.length() ==0) {
                            emptyText.setVisibility(View.VISIBLE);





                            emptyText.setText(((Student)getActivity()).Toname+" has not made any of his scraps public yet. You can write a scrap for "+((Student)getActivity()).Toname+" here.");
                        }
                        else {
                            studentsScrapAdapter = new StudentsScrapAdapter(getActivity().getApplicationContext(), R.layout.scrap_item, scrapClassList);
                            listView.setAdapter(studentsScrapAdapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }




            @Override
            public void onFailure(Call<Object> call, Throwable t) {
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

    void checkVerification(){
        final int verified = 0;
        token = "Bearer "+tokenManager.getAccess();
        call3 = userClient3.checkVerification(token);
        call3.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if(response.isSuccessful()){
                    if(response.body().getMsg().equals("yes")){
                        SendScrap();
                    }
                    if(response.body().getMsg().equals("no")){
                        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                        dialog.setMessage("Please Verify Your Account. If you haven't Received Mail, please re-login after fifteen minutes, and resend email. Try again or contact your network administrator.");
                        dialog.setTitle("Something Went Wrong.");
                        dialog.setPositiveButton("CLOSE",
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



    void SendScrap(){
        emptyText.setVisibility(View.INVISIBLE);
        gcaps.setVisibility(View.VISIBLE);
        gcaps.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.blink));
        token="Bearer "+tokenManager.getAccess();
        content=Scrapcontent.getText().toString();
        Scrapcontent.setText("");
        call2 = userClient2.createScrap(token,studentID,content);
        call2.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()){

                    if(response.body().getMsg().equals("Scrap created successfully")) {
                        gcaps.clearAnimation();;
                        gcaps.setVisibility(View.INVISIBLE);
                        alertDialog();
                        emptyText.setVisibility(View.VISIBLE);


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
                    emptyText.setVisibility(View.VISIBLE);

                }

            }
        });




    }


    void alertDialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setMessage("Thank you for posting a scrap! We are sure Varun will be delighted to read your scrap. Your scrap will appear on their wall, once approved by them.");
        dialog.setTitle("Scrap Creation Successful!");
        dialog.setPositiveButton("CLOSE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                     dialog.cancel();
                    }
                });

        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(call2!= null && call2.isExecuted()) {
            call2.cancel();
        }
        if(call!= null && call.isExecuted()) {
            call.cancel();
       }
    }
}
