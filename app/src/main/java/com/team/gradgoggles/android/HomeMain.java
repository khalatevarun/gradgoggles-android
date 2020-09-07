package com.team.gradgoggles.android;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class HomeMain extends Fragment {


    public HomeMain() {
        // Required empty public constructor
    }

        LinearLayout aHome, aHangout, aStory, aFeeling;
        TextView tHome, tHangout,tStory,tFeeling;
        TextView count;
        TextView mssg;
        TextView mainText;
        TextView classof2020;

        ImageView mainImage;
        View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_home_main, container, false);
        mainImage = rootView.findViewById(R.id.mainImage);
        aHome = rootView.findViewById(R.id.aHome);
        aHangout=rootView.findViewById(R.id.aHangout);
        aStory = rootView.findViewById(R.id.aStory);
        aFeeling = rootView.findViewById(R.id.aFeeling);
        tHome = rootView.findViewById(R.id.tHome);
        tHangout = rootView.findViewById(R.id.tHangout);
        tStory = rootView.findViewById(R.id.tStory);
        tFeeling = rootView.findViewById(R.id.tFeeling);
        count = rootView.findViewById(R.id.countSeconds);
        mssg = rootView.findViewById(R.id.message);
        mainText=rootView.findViewById(R.id.mainText);
        classof2020=rootView.findViewById(R.id.classof2020);
        Animation zoomIn = AnimationUtils.loadAnimation(getActivity(),R.anim.zoom_in);
        classof2020.startAnimation(zoomIn);

        Animation image_In  = AnimationUtils.loadAnimation(getActivity(), R.anim.image_in);
        mainImage.startAnimation(image_In);










        Handler handler = new Handler();
        final  Runnable r= new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                DateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                Date dateobj = new Date();
                Calendar calendar=Calendar.getInstance();
                String currentDate = df.format(calendar.getTime());;
                String S_startTime = "08-01-2016 00:00:00";
                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                Date startTime = null;
                try {
                    dateobj = formatter.parse(currentDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    startTime = formatter.parse(S_startTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long diff = (dateobj.getTime() - startTime.getTime())/1000;
                String secondsF = String.valueOf(diff);
                count.setText(secondsF+" SECONDS");
            }
            };
        handler.postDelayed(r,0000);

        aHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aHome.setBackgroundColor(getResources().getColor(R.color.white));
                tHome.setTextColor(getResources().getColor(R.color.Primary));
                mainImage.setImageResource(R.drawable.ahome_bkg);
                mainImage.startAnimation(image_In);
                mssg.setText("LEAVE ONLY FOOTPRINTS");

                aHangout.setBackgroundColor(getResources().getColor(R.color.Primary));
                tHangout.setTextColor(getResources().getColor(R.color.white));

                aStory.setBackgroundColor(getResources().getColor(R.color.Primary));
                tStory.setTextColor(getResources().getColor(R.color.white));

                aFeeling.setBackgroundColor(getResources().getColor(R.color.Primary));
                tFeeling.setTextColor(getResources().getColor(R.color.white));

            }
        });

        aHangout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aHome.setBackgroundColor(getResources().getColor(R.color.Primary));
                tHome.setTextColor(getResources().getColor(R.color.white));

                aHangout.setBackgroundColor(getResources().getColor(R.color.white));
                tHangout.setTextColor(getResources().getColor(R.color.Primary));
                mainImage.setImageResource(R.drawable.ahangout_bkg);
                mainImage.startAnimation(image_In);

                mssg.setText("LEAVE ONLY LAUGHS");

                aStory.setBackgroundColor(getResources().getColor(R.color.Primary));
                tStory.setTextColor(getResources().getColor(R.color.white));

                aFeeling.setBackgroundColor(getResources().getColor(R.color.Primary));
                tFeeling.setTextColor(getResources().getColor(R.color.white));
            }
        });

        aStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aHome.setBackgroundColor(getResources().getColor(R.color.Primary));
                tHome.setTextColor(getResources().getColor(R.color.white));

                aHangout.setBackgroundColor(getResources().getColor(R.color.Primary));
                tHangout.setTextColor(getResources().getColor(R.color.white));

                aStory.setBackgroundColor(getResources().getColor(R.color.white));
                tStory.setTextColor(getResources().getColor(R.color.Primary));
                mainImage.setImageResource(R.drawable.astory_bkg);
                mainImage.startAnimation(image_In);
                mssg.setText("LEAVE ONLY THE JOURNEY");

                aFeeling.setBackgroundColor(getResources().getColor(R.color.Primary));
                tFeeling.setTextColor(getResources().getColor(R.color.white));

            }
        });

        aFeeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aHome.setBackgroundColor(getResources().getColor(R.color.Primary));
                tHome.setTextColor(getResources().getColor(R.color.white));

                aHangout.setBackgroundColor(getResources().getColor(R.color.Primary));
                tHangout.setTextColor(getResources().getColor(R.color.white));

                aStory.setBackgroundColor(getResources().getColor(R.color.Primary));
                tStory.setTextColor(getResources().getColor(R.color.white));

                aFeeling.setBackgroundColor(getResources().getColor(R.color.white));
                tFeeling.setTextColor(getResources().getColor(R.color.Primary));
                mainImage.setImageResource(R.drawable.afeeling_bkg);
                mainImage.startAnimation(image_In);
                mssg.setText("LEAVE ONLY MEMORIES");
            }
        });





        return rootView;
    }
}