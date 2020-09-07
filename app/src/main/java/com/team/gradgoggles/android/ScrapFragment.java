package com.team.gradgoggles.android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.content.Context.MODE_PRIVATE;


public class ScrapFragment extends Fragment {
    private static final String TAG = "ScrapFragment";
    private static final String TAG2 = "ScrapToggle";


    ListView listView;
    String token;
    TokenManager tokenManager;
    UserClient userClient;
    Call<Object> call;
  ScrapAdapter scrapAdapter;
    ImageView scrapVisible;
    String currentScrapId;

    String scrapId;
    ImageView gcaps;


    List<Scrap> scrapClassList = new ArrayList<>();

    int visible=0;
    TextView scrapContent;

    View rootView;
    FrameLayout rootLayout;

    TextView emptyText;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_scrap, container, false);
         gcaps=rootView.findViewById(R.id.gcaps);
         emptyText=rootView.findViewById(R.id.emptyText);
         emptyText.setVisibility(View.INVISIBLE);
         rootLayout = rootView.findViewById(R.id.container);
        userClient = RetrofitBuilder.createService(UserClient.class);




        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        listView = (ListView)rootView.findViewById(R.id.scrap_list);
        listView.setVisibility(View.INVISIBLE);

        InsertScraps();

        scrapContent = rootView.findViewById(R.id.scrap_content);
       // scrapVisible = rootView.findViewById(R.id.scrap_visibility);
       



        return rootView;
}

    private void InsertScraps() {


        gcaps.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.blink));
        token="Bearer "+tokenManager.getAccess();
        call=userClient.scraps(token,null,"1");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    gcaps.clearAnimation();
                    gcaps.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);

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
                            currentScrapId = currentScrap.getString("id");

                            String content = currentScrap.getString("content");
                            String timestamp = currentScrap.getString("timestamp");
                            Boolean visibility = currentScrap.getBoolean("visibility");

                            PostedBy postedBy = new PostedBy(name, photo,id);

                            Scrap scrapClass = new Scrap(content, currentScrapId, timestamp, postedBy,visibility);

                            scrapClassList.add(scrapClass);
                        }

                            if(scrapsArray.length()==0)
                            {
                                emptyText.setVisibility(View.VISIBLE);
                                emptyText.setText("No scraps yet, check back soon! We will notify you if someone posts a scrap on your profile!");
                            }
                            else {

                                scrapAdapter = new ScrapAdapter(getActivity().getApplicationContext(), R.layout.scrap_item, scrapClassList);
                                listView.setAdapter(scrapAdapter);

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


    @Override
    public void onPause() {
        super.onPause();


        if(call!= null && call.isExecuted()) {
            call.cancel();
        }


    }
}