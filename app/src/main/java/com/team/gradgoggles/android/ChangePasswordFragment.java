package com.team.gradgoggles.android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.team.gradgoggles.android.api.model.AccessToken;
import com.team.gradgoggles.android.api.service.RetrofitBuilder;
import com.team.gradgoggles.android.api.service.UserClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class ChangePasswordFragment extends Fragment {
    private static final String TAG = "ChangePasswordFragment";


    TextInputLayout current_password;


    TextInputLayout new_password;

    UserClient userClient;
    Call<AccessToken> call;
    String token;
    TokenManager tokenManager;
    CircularProgressButton btn;
    int buttonAnimation =0;

    LinearLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        rootLayout = rootView.findViewById(R.id.rootLayout);
        userClient = RetrofitBuilder.createService(UserClient.class);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        current_password = rootView.findViewById(R.id.current_password);
        new_password=rootView.findViewById(R.id.new_password);

        btn = (CircularProgressButton)rootView.findViewById(R.id.change_password_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.startAnimation();
                buttonAnimation=1;
                changePassword();
            }
        });

        return rootView;
    }

    void changePassword() {
        String currentPassword = current_password.getEditText().getText().toString();
        String newPassword = new_password.getEditText().getText().toString();
        token = "Bearer " + tokenManager.getAccess();

        call = userClient.Password(token, currentPassword, newPassword);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.w(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    Log.w(TAG, "onResponse: " + response);

                    if (response.body().getMsg().equals("Password does not match")) {
                        Toast.makeText(getActivity(), "Password does not match", Toast.LENGTH_SHORT).show();
                        if (buttonAnimation == 1) {
                            btn.revertAnimation();
                        }
                    }
                    if (response.body().getMsg().equals("Password Changed")) {
                        Toast.makeText(getActivity(), "Password Changed", Toast.LENGTH_SHORT).show();
                        if (buttonAnimation == 1) {
                            btn.revertAnimation();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
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

        @Override
        public void onPause() {
            super.onPause();


            if(call!= null && call.isExecuted()) {
                call.cancel();
            }


        }

    }
