package com.team.gradgoggles.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Student extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private StudentProfileFragment studentProfileFragment;
    private StudentScrapsFragment studentScrapsFragment;
    String studentId;
    String postedById;
  public static  String Toname;
  Globals g = Globals.getInstance();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Intent intent = getIntent();

        studentId = intent.getStringExtra("Student Id");
        Toname = intent.getStringExtra("Student Name");

        if(studentId==null){
            studentId=g.getPostedbyStduent_id();
        }





        toolbar = findViewById(R.id.student_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        studentProfileFragment = new StudentProfileFragment();
        studentScrapsFragment = new StudentScrapsFragment();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);

        viewPager = findViewById(R.id.student_view_pager);

        viewPagerAdapter.addFragment(studentProfileFragment, "Profile");
        viewPagerAdapter.addFragment(studentScrapsFragment, "Scraps");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout=findViewById(R.id.student_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

















    }


    private class  ViewPagerAdapter extends FragmentPagerAdapter  {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmenttitle = new ArrayList<>();
        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmenttitle.add(title);


        }



        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmenttitle.get(position);
        }


    }

    String getFinal_studentId(){
        return  studentId;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toname = null;
    }
}