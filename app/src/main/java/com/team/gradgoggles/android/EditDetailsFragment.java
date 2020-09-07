package com.team.gradgoggles.android;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.team.gradgoggles.android.api.model.AccessToken;
import com.team.gradgoggles.android.api.model.ProfilePic;
import com.team.gradgoggles.android.api.service.RetrofitBuilder;
import com.team.gradgoggles.android.api.service.RetrofitBuilder2;
import com.team.gradgoggles.android.api.service.UserClient;
import com.team.gradgoggles.android.api.service.UserPic;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class EditDetailsFragment extends Fragment {





    private static final String TAG = "EditDetailsFragment";

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";


    private Gson gson;
    String filepath;

    int dpchanged=0;


    CircularProgressButton btn;
    int buttonAnimation =0;

    TextInputLayout editQuoteParent;
    TextInputEditText editEmail;
    TextInputEditText editFullname;
    TextInputEditText editQuote;
    EditText editDob;
    Spinner editDept;
    UserClient userClient;
    UserPic userPic;
    UserClient userClient2;
    TokenManager tokenManager;
    DatePickerDialog dob;
    String Email, newEmail;
    String Fullname, newFullname;
    String Quote, newQuote;
    String Dob, newDob;
    String Dept, newDept;
    String token, newToken;

    HomeClass currentDetails;
    ImageView editPic;
    List<String> categories =  new ArrayList<>();


    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String filetype;
    String filname;
    String newProfilePicUrl;


    Call<HomeClass> call;
    Call<AccessToken> call1;

    Call<HomeClass> call2;
    Call<String> call3;
    Call<AccessToken>call4;
    ProfilePic pp = new ProfilePic();


    LinearLayout editDetails;
    ImageView gcaps;


    RelativeLayout rootLayout;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_details, container, false);

        rootLayout = rootView.findViewById(R.id.rootLayout);
        editDetails = rootView.findViewById(R.id.edit_details);
        editDetails.setVisibility(View.INVISIBLE);
        gcaps = rootView.findViewById(R.id.gcaps);



        userClient = RetrofitBuilder.createService(UserClient.class);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        newProfilePicUrl=tokenManager.getPhoto();

        btn = rootView.findViewById(R.id.save_edit);

        editPic=rootView.findViewById(R.id.edit_profile_image);
        editEmail=rootView.findViewById(R.id.edit_email);
        editFullname=rootView.findViewById(R.id.edit_fullname);
        editQuote=rootView.findViewById(R.id.edit_quote);
        editQuoteParent = rootView.findViewById(R.id.edit_quote_parent);
        editDept=rootView.findViewById(R.id.edit_department);
        editDob = (EditText)rootView.findViewById(R.id.edit_dob);


        if((tokenManager.getis2020()).equals("no")){
            editQuote.setVisibility(View.GONE);
            editQuoteParent.setVisibility(View.GONE);
        }


        editDob.setInputType(InputType.TYPE_NULL);
        editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"dob clicked");


                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                dob = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if(monthOfYear<10 && dayOfMonth<10) {
                                    editDob.setText("0"+dayOfMonth + "-" +"0"+ (monthOfYear + 1) + "-" + year);

                                }
                                else if(dayOfMonth<10){
                                    editDob.setText("0"+dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
                                }
                                else if(monthOfYear<10)
                                {
                                    editDob.setText(dayOfMonth + "-" +"0"+ (monthOfYear + 1) + "-" + year);

                                }
                                if(dayOfMonth>10 && monthOfYear>10) {
                                    editDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }
                            }
                        }, year, month, day);
                dob.show();
            }
        });

        InsertDetails();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfilePic();



            }
        });

        editPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });




return rootView;    }

void InsertDetails(){

    gcaps.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.blink));
    token="Bearer "+tokenManager.getAccess();
    call = userClient.currentUser(token,null,1,1,1,1,1,1);
    call.enqueue(new Callback<HomeClass>() {
        @Override
        public void onResponse(Call<HomeClass> call, Response<HomeClass> response) {
            Log.w(TAG, "onResponse-InsertDetails: " + response.body());

            if (response.isSuccessful()) {

                gcaps.clearAnimation();
                gcaps.setVisibility(View.INVISIBLE);
                editDetails.setVisibility(View.VISIBLE);

                currentDetails=response.body();

                Picasso.get().load(currentDetails.getMphoto()).placeholder(R.drawable.progress_animation).into(editPic);
                editFullname.setText(currentDetails.getMname());
                categories.add(currentDetails.getMdepartment());
               // editDept.setText(currentDetails.getMdepartment());
                categories.add("Computer");
                categories.add("IT");
                categories.add("Elex");
                categories.add("EnTC");
                categories.add("Chemical");
                categories.add("Instrumentation");
                categories.add("Mechanical");
                categories.add("Industrial");
                categories.add("MCA");
                ArrayAdapter<String> departmentAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item_edit_details,categories);
                departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editDept.setAdapter(departmentAdapter);
                editDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        Dept = parent.getItemAtPosition(position).toString();


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                editQuote.setText(currentDetails.getMquote());
                editEmail.setText(currentDetails.getMemail());
                String rdob = currentDetails.getMdob().split(" ")[0];

                try {
                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(rdob);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String strDate= formatter.format(date1);
                    editDob.setText(strDate);
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
                if (buttonAnimation == 1) {
                    btn.revertAnimation();
                }

                    Snackbar snackbar = Snackbar.make(rootLayout, "Check your internet connection.", Snackbar.LENGTH_LONG);

                    snackbar.show();

            }
        }
    });
}





        /**
        btn.startAnimation();
        buttonAnimation=1;


        token="Bearer "+tokenManager.getAccess();
        newFullname = editFullname.getText().toString();
        newQuote = editQuote.getText().toString();
        newDob = editDob.getText().toString();
        newDept = Dept;


        call4 = userClient.update(token,newFullname,newDept,newDob,newQuote,newProfilePicUrl);
        call4.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.w(TAG, "onResponse-UpdateDetails: " + response.body());
                if(response.isSuccessful()){
                    if(response.body().getMsg().equals("Updation Successful")){
                        tokenManager.setPhoto(newProfilePicUrl);
                        tokenManager.setName(newFullname);
                        if (buttonAnimation==1){
                            btn.revertAnimation();
                            buttonAnimation=0;
                        }
                        else{
                            if(buttonAnimation==1)
                            {
                                btn.revertAnimation();
                                buttonAnimation=0;
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure-UpdateDetails: " + t);
                if(call.isCanceled()){
                    return;
                }
                else {
                    if (buttonAnimation == 1) {
                        btn.revertAnimation();
                    }
                    Snackbar snackbar = Snackbar.make(rootLayout, "Check your internet connection.", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }
        });




**/



    void UpdateProfilePic(){
        btn.startAnimation();
        buttonAnimation=1;
        token="Bearer "+tokenManager.getAccess();
        if(dpchanged==1) {
            filname = getRandomString(10);
            call3 = userClient.profilepic(token, filname, filetype);
            call3.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.w(TAG, "onResponse-UpdateProfilePic: " + response.body());

                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        pp = gson.fromJson(response.body(), ProfilePic.class);

                        File file = new File(filepath);    // create new file on device
                        RequestBody reqFile = RequestBody.create(file, MediaType.parse("image/*"));

                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", filname, reqFile);

                        RequestBody acl = RequestBody.create(pp.getAcl(), MediaType.parse("multipart/form-data"));
                        RequestBody contentType = RequestBody.create(pp.getContentType(), MediaType.parse("multipart/form-data"));
                        RequestBody key = RequestBody.create(pp.getKey(), MediaType.parse("multipart/form-data"));
                        RequestBody algo = RequestBody.create(pp.getxamzalgorithm(), MediaType.parse("multipart/form-data"));
                        RequestBody cred = RequestBody.create(pp.getxamzcredential(), MediaType.parse("multipart/form-data"));
                        RequestBody date = RequestBody.create(pp.getxamzdate(), MediaType.parse("multipart/form-data"));
                        RequestBody policy = RequestBody.create(pp.getpolicy(), MediaType.parse("multipart/form-data"));
                        RequestBody sign = RequestBody.create(pp.getxamzsignature(), MediaType.parse("multipart/form-data"));

                        userPic = RetrofitBuilder2.createService(UserPic.class);

                        call1 = userPic.upload(acl, contentType, key, algo, cred, date, policy, sign, body);

                        call1.enqueue(new Callback<AccessToken>() {

                            @Override
                            public void onResponse(Call<AccessToken> call1, Response<AccessToken> response) {
                                Log.w(TAG, "onResponse-UploadProfiePic: " + response.body());

                                if (response.isSuccessful()) {
                                    newProfilePicUrl = pp.getDataUrl();
                                    tokenManager.setPhoto(newProfilePicUrl);

                                    //UPDATE DETAILS
                                    btn.startAnimation();
                                    buttonAnimation = 1;


                                    token = "Bearer " + tokenManager.getAccess();
                                    newFullname = editFullname.getText().toString();
                                    newQuote = editQuote.getText().toString();
                                    newDob = editDob.getText().toString();
                                    newDept = Dept;


                                    call4 = userClient.update(token, newFullname, newDept, newDob, newQuote, newProfilePicUrl);
                                    call4.enqueue(new Callback<AccessToken>() {
                                        @Override
                                        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                                            Log.w(TAG, "onResponse-UpdateDetails: " + response.body());
                                            if (response.isSuccessful()) {
                                                if (response.body().getMsg().equals("Updation Successful")) {
                                                    Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_SHORT).show();
                                                    tokenManager.setPhoto(newProfilePicUrl);
                                                    tokenManager.setName(newFullname);
                                                    if (buttonAnimation == 1) {
                                                        btn.revertAnimation();
                                                        buttonAnimation = 0;
                                                    } else {
                                                        if (buttonAnimation == 1) {
                                                            btn.revertAnimation();
                                                            buttonAnimation = 0;
                                                        }
                                                    }

                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<AccessToken> call, Throwable t) {
                                            Log.w(TAG, "onFailure-UpdateDetails: " + t);
                                            if (call.isCanceled()) {
                                                return;
                                            } else {
                                                if (buttonAnimation == 1) {
                                                    btn.revertAnimation();
                                                }
                                                Snackbar snackbar = Snackbar.make(rootLayout, "Check your internet connection.", Snackbar.LENGTH_LONG);

                                                snackbar.show();
                                            }
                                        }
                                    });


                                    //DONE


                                }

                            }

                            @Override
                            public void onFailure(Call<AccessToken> call, Throwable t) {
                                Log.w(TAG, "onFailure-UploadProfilePic: " + t);
                                if (call.isCanceled()) {
                                    return;
                                } else {

                                    if (buttonAnimation == 1) {
                                        btn.revertAnimation();
                                        ;
                                        buttonAnimation = 0;
                                    }
                                    if (t instanceof NetworkErrorException) {
                                        Snackbar snackbar = Snackbar.make(rootLayout, "Check your internet connection.", Snackbar.LENGTH_LONG);

                                        snackbar.show();
                                    }
                                }

                            }
                        });


                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.w(TAG, "onFailure-UpdateProfilePic: " + t);
                    if (call.isCanceled()) {
                        return;
                    } else {
                        if (buttonAnimation == 1) {
                            btn.revertAnimation();
                        }
                        Snackbar snackbar = Snackbar.make(rootLayout, "Check your internet connection.", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }

                }
            });
        }
        else{
            token = "Bearer " + tokenManager.getAccess();
            newFullname = editFullname.getText().toString();
            newQuote = editQuote.getText().toString();
            newDob = editDob.getText().toString();
            newDept = Dept;
            btn.startAnimation();
            buttonAnimation=1;
            call4 = userClient.update(token, newFullname, newDept, newDob, newQuote, newProfilePicUrl);
            call4.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.w(TAG, "onResponse-UpdateDetails: " + response.body());
                    if (response.isSuccessful()) {
                        if (response.body().getMsg().equals("Updation Successful")) {
                            Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_SHORT).show();

                            tokenManager.setName(newFullname);
                            if (buttonAnimation == 1) {
                                btn.revertAnimation();
                                buttonAnimation = 0;
                            } else {
                                if (buttonAnimation == 1) {
                                    btn.revertAnimation();
                                    buttonAnimation = 0;
                                }
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFailure-UpdateDetails: " + t);
                    if (call.isCanceled()) {
                        return;
                    } else {
                        if (buttonAnimation == 1) {
                            btn.revertAnimation();
                        }
                        Snackbar snackbar = Snackbar.make(rootLayout, "Check your internet connection.", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                }
            });
        }

    }

    private void openGallery() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        if(getContext().checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

            startActivityForResult(gallery, PICK_IMAGE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            filetype = getActivity().getContentResolver().getType(imageUri);
            filepath=getRealPathFromUri(imageUri);
            dpchanged=1;
            Picasso.get().load(imageUri).into(editPic);

        }
    }

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private String getRealPathFromUri(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor=loader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnIndex);
        cursor.close();
        return result;
    }

    @Override
    public void onPause() {
        super.onPause();


        if(call!= null && call.isExecuted()) {
            call.cancel();
        }
        if (call2 != null && call2.isExecuted()) {
            call2.cancel();
        }

    }





}