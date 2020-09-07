package com.team.gradgoggles.android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class StudentsScrapAdapter extends ArrayAdapter<Scrap> {




    public StudentsScrapAdapter(@NonNull Context context, int resource, @NonNull List<Scrap> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.student_scrap_item,parent,false);

        }
        Scrap currentObject = getItem(position);
        ImageView photo=(ImageView)listItemView.findViewById(R.id.scrap_user_pic);
       String objectPhoto = currentObject.postedBy.getPhoto();
       Picasso.get().load(objectPhoto).transform(new CropCircleTransformation()).placeholder(R.drawable.progress_animation).into(photo);

        TextView name = (TextView)listItemView.findViewById(R.id.scrap_user_name);
        String objectName = currentObject.postedBy.getName();
       name.setText(objectName);

        TextView timestamp = (TextView)listItemView.findViewById(R.id.scrap_time_stamp);
        String objectTimeStamp = currentObject.getTimestamp();

        DateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSSSSS");


        Date currentTime =  new Date();
        String stringCurrentTime =df.format(currentTime);
        Date temp=null,temp2=null;
        String final_time=null;

        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSSSSS");



        try {
            temp = formatter.parse(objectTimeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            temp2 = formatter.parse(stringCurrentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = temp2.getTime()-temp.getTime();
        long second = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minute = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hour   = TimeUnit.MILLISECONDS.toHours(diff);
        long day  = TimeUnit.MILLISECONDS.toDays(diff);

        if (second < 60) {
            if(second ==1)
            final_time = second + " Second " + "ago";
            else
                final_time = second + " Seconds " + "ago";



        } else if (minute < 60) {
            if(minute==1)
            final_time = minute + " Minute "+"ago";
            else
                final_time = minute + " Minutes "+"ago";

        } else if (hour < 24) {
            if(hour==1)
            final_time = hour + " Hour "+"ago";
            else
                final_time = hour + " Hours "+"ago";

        } else if (day >= 7) {
            if (day > 360) {
                if(day/360 ==1)
                final_time = (day / 360) + " Year " + "ago";
                else
                    final_time = (day / 360) + " Years " + "ago";

            } else if (day > 30) {
                if(day/30 == 1)
                final_time = (day / 30) + " Month " + "ago";
                else
                    final_time = (day / 30) + " Months " + "ago";

            } else {
                if(day/7 ==1)
                final_time = (day / 7) + " Week " + "ago";
                else
                    final_time = (day / 7) + " Weeks " + "ago";

            }
        } else if (day < 7) {
            if(day ==1)
            final_time = day+" Day "+"ago";
            else
                final_time = day+" Days "+"ago";

        }

        timestamp.setText(final_time);

        TextView content = (TextView)listItemView.findViewById(R.id.scrap_content);
        String objectContent = currentObject.getContent();
        content.setText(objectContent);


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals g= Globals.getInstance();

                String STUDENT_ID = Integer.toString(currentObject.postedBy.getId());
                g.setPostedbyStduent_id(STUDENT_ID);

                Intent intent = new Intent(getContext(),Student.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String STUDENT_ID = Integer.toString(currentObject.postedBy.getId());
                Globals g= Globals.getInstance();
                g.setPostedbyStduent_id(STUDENT_ID);

                Intent intent = new Intent(getContext(),Student.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

            }
        });




        return listItemView;

    }

}
