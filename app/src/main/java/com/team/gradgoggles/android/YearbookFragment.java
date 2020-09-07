package com.team.gradgoggles.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.gradgoggles.android.api.model.AccessToken;
import com.team.gradgoggles.android.api.service.RetrofitBuilder;
import com.team.gradgoggles.android.api.service.UserClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class YearbookFragment extends Fragment {
    private static final String TAG = "YearbookFragment";

    GridView gridView;
    String token;
    TokenManager tokenManager;
    UserClient userClient;
    UserClient userClient2;
    UserClient userClient3;
    UserClient userClient4;

    Call<List<HomeClass>> call;
    Call<List<HomeClass>> call2;
    Call<AccessToken> call3;
    Call<AccessToken> call4;
    HomeAdapter homeAdapter;
    List<HomeClass> Students;
    LinearLayout next, previous;
    TextView pageno;
    ImageView sliderButton;
    EditText searchText;
    String searchParam;

    View rootView;
    RelativeLayout rootLayout;
    NestedScrollView scrollView;

    LinearLayout list_footer;
    ImageView progress_animation;
    TextView noSearch;
    AutoCompleteTextView autoCompleteTextView;



    Globals g= Globals.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] department = { "Computer", "Elex","EnTC",
                "Chemical", "Industrial","Production","Mechanical","IT","Instrumentation","MCA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(),R.layout.autocomplete_dialog_layout, department);



        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        rootView = inflater.inflate(R.layout.fragment_yearbook2, container, false);
        rootLayout = rootView.findViewById(R.id.container);

        scrollView = rootView.findViewById(R.id.scrollView);
        list_footer = rootView.findViewById(R.id.list_footer);
        progress_animation = rootView.findViewById(R.id.progress_animation);
        gridView = rootView.findViewById(R.id.yearbook_grids);
        gridView.setNestedScrollingEnabled(false);
        noSearch = rootView.findViewById(R.id.nosearch);
        noSearch.setVisibility(View.INVISIBLE);

        autoCompleteTextView=rootView.findViewById(R.id.searchDept);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setVisibility(View.GONE);



        userClient = RetrofitBuilder.createService(UserClient.class);
        userClient2 = RetrofitBuilder.createService(UserClient.class);
        userClient3 = RetrofitBuilder.createService(UserClient.class);

        userClient4 = RetrofitBuilder.createService(UserClient.class);



        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        gridView = (GridView) rootView.findViewById(R.id.yearbook_grids);
        next = (LinearLayout) rootView.findViewById(R.id.next);
        previous = (LinearLayout) rootView.findViewById(R.id.previous);
        pageno = (TextView) rootView.findViewById(R.id.page_no);
        pageno.setText(Integer.toString(g.getCurrentYearbookPage()));

        sliderButton = (ImageView) rootView.findViewById(R.id.sliderBtn);
        searchText = (EditText) rootView.findViewById(R.id.searchText);


        final String[] fonts = {
                "Department", "Name",
        };


        sliderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Search By");
                builder.setItems(fonts, new DialogInterface.OnClickListener() {@
                        Override
                public void onClick(DialogInterface dialog, int which) {
                    if ("Department".equals(fonts[which])) {
                        searchText.setVisibility(View.GONE);
                        autoCompleteTextView.setVisibility(View.VISIBLE);
                        g.setSearchByDept(true);
                        g.setSearchByName(false);
                        g.setSearchByYearbook(false); ;
                    } else if ("Name".equals(fonts[which])) {
                        autoCompleteTextView.setVisibility(View.GONE);
                        searchText.setVisibility(View.VISIBLE);
                        g.setSearchByName(true);
                        g.setSearchByDept(false);
                        g.setSearchByYearbook(false);


                    }
                }
                });
                builder.show();

            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH) {
                    g.setSearchParam(searchText.getText().toString());
                    g.setSearchByYearbook(false);
                    g.setSearchByDept(false);
                    g.setCurrentDeptPage(1);
                    g.setCurrentYearbookPage(1);
                    g.setSearchByName(true);
                    g.setCurrentNamePage(1);
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    noSearch.setVisibility(View.INVISIBLE);

                    searchUser();
                }
                return true;
            }
        });

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH) {

                    g.setSearchParam(autoCompleteTextView.getText().toString());
                    g.setCurrentNamePage(1);
                    g.setCurrentDeptPage(1);
                    g.setCurrentYearbookPage(1);
                    g.setSearchByYearbook(false);
                    g.setSearchByDept(true);
                    g.setSearchByName(false);



                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    noSearch.setVisibility(View.INVISIBLE);

                    searchUser();
                }
                return true;
            }
        });






      /**  sliderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                searchUser();
            }
        });
**/

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (g.getSearchByYearbook() == true) {
                    int page= g.getCurrentYearbookPage()+1;
                    g.setCurrentYearbookPage(page);
                    g.setSearchByName(false);
                    g.setSearchByDept(false);
                    pageno.setText(Integer.toString(page));
                    scrollView.fullScroll(ScrollView.FOCUS_UP);

                    InsertUsers();
                }
                if (g.getSearchByName() == true) {
                    int page= g.getCurrentNamePage()+1;
                    g.setCurrentNamePage(page);
                    g.setSearchByDept(false);
                    g.setSearchByYearbook(false);
                    pageno.setText(Integer.toString(page));
                    scrollView.fullScroll(ScrollView.FOCUS_UP);

                    searchUser();
                }
                if (g.getSearchByDept() == true) {
                    int page= g.getCurrentDeptPage()+1;
                    g.setCurrentDeptPage(page);
                    g.setSearchByYearbook(false);
                    g.setSearchByName(false);
                    pageno.setText(Integer.toString(page));
                    scrollView.fullScroll(ScrollView.FOCUS_UP);

                    searchUser();
                }

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (g.getSearchByYearbook() == true) {
                    int page= g.getCurrentYearbookPage()-1;
                    if(page==0){
                        page=1;
                    }
                    g.setCurrentYearbookPage(page);
                    g.setSearchByName(false);
                    g.setSearchByDept(false);
                    pageno.setText(Integer.toString(page));
                    scrollView.fullScroll(ScrollView.FOCUS_UP);

                    InsertUsers();
                }
                if (g.getSearchByName() == true) {
                    int page= g.getCurrentNamePage()-1;
                    if(page==0){
                        page=1;
                    }
                    g.setCurrentNamePage(page);
                    g.setSearchByDept(false);
                    g.setSearchByYearbook(false);
                    pageno.setText(Integer.toString(page));
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    searchUser();
                }
                if (g.getSearchByDept() == true) {

                    int page= g.getCurrentDeptPage()-1;
                    if(page==0){
                        page=1;
                    }
                    g.setCurrentDeptPage(page);
                    g.setSearchByYearbook(false);
                    g.setSearchByName(false);
                    pageno.setText(Integer.toString(page));
                    scrollView.fullScroll(ScrollView.FOCUS_UP);

                    searchUser();
                }
            }
        });

        if(g.getSearchByYearbook()==true) {
            InsertUsers();
        }
        if(g.getSearchByYearbook()==false){
            searchUser();
        }

        return rootView;
    }

    void InsertUsers() {

        gridView.setVisibility(View.INVISIBLE);
        list_footer.setVisibility(View.INVISIBLE);

        progress_animation.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.blink));

        token = "Bearer " + tokenManager.getAccess();
        call = userClient.users(token, g.getCurrentYearbookPage());
        call.enqueue(new Callback<List<HomeClass>>() {
            @Override
            public void onResponse(Call<List<HomeClass>> call, Response<List<HomeClass>> response) {
                if (response.isSuccessful()) {
                    Log.w(TAG, "onResponse: " + response.body());
                    noSearch.setVisibility(View.INVISIBLE);
                    progress_animation.clearAnimation();
                    progress_animation.setVisibility(View.INVISIBLE);
                    gridView.setVisibility(View.VISIBLE);
                    list_footer.setVisibility(View.VISIBLE);

                    Students = response.body();
                    homeAdapter = new HomeAdapter(getActivity().getApplicationContext(), R.layout.home_item, Students);
                    gridView.setAdapter(homeAdapter);
                    if (homeAdapter.isEmpty()) {
                        progress_animation.clearAnimation();
                        progress_animation.setVisibility(View.INVISIBLE);
                        noSearch.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void onFailure(Call<List<HomeClass>> call, Throwable t) {
                if(call.isCanceled()){
                    return;
                }
                else {
                    Snackbar snackbar = Snackbar.make(rootLayout, "Check your internet connection.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });

    }

    void searchUser() {


        gridView.setVisibility(View.INVISIBLE);
        list_footer.setVisibility(View.VISIBLE);



        progress_animation.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.blink));


        gridView.setAdapter(null);
        token = "Bearer " + tokenManager.getAccess();
        if(g.getSearchByDept()==true) {
            call2 = userClient2.thisdept(token, g.getSearchParam(), g.getCurrentDeptPage());
            pageno.setText(Integer.toString(g.getCurrentDeptPage()));
        }
        else if(g.getSearchByName()==true) {
            call2 = userClient2.thisuser(token, g.getSearchParam(),g.getCurrentNamePage());
            pageno.setText(Integer.toString(g.getCurrentNamePage()));
        }
        call2.enqueue(new Callback<List<HomeClass>>() {
            @Override
            public void onResponse(Call<List<HomeClass>> call, Response<List<HomeClass>> response) {
                if (response.isSuccessful()) {
                    Log.w(TAG, "onResponse: " + response.body());

                    noSearch.setVisibility(View.INVISIBLE);
                    progress_animation.clearAnimation();
                    progress_animation.setVisibility(View.INVISIBLE);
                    gridView.setVisibility(View.VISIBLE);


                    Students = response.body();
                    homeAdapter = new HomeAdapter(getActivity().getApplicationContext(), R.layout.home_item, Students);
                    gridView.setAdapter(homeAdapter);
                    if (homeAdapter.isEmpty()) {
                        noSearch.setVisibility(View.VISIBLE);
                        list_footer.setVisibility(View.INVISIBLE);
                    }

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ((MainActivity)getActivity()).prevFrag =1;
                            HomeClass currentObject = homeAdapter.getItem(position);
                            String STUDENT_ID = currentObject.getMid();
                            Intent intent = new Intent(getActivity(), Student.class);
                            intent.putExtra("Student Id", STUDENT_ID);
                            startActivity(intent);


                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<HomeClass>> call, Throwable t) {



                if(call.isCanceled()){
                    return;
                }
                else {
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
        if (call2 != null && call2.isExecuted()) {
            call2.cancel();
              }

        }
    }
