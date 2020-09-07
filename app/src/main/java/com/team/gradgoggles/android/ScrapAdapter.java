package com.team.gradgoggles.android;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.team.gradgoggles.android.api.model.AccessToken;
import com.team.gradgoggles.android.api.service.RetrofitBuilder;
import com.team.gradgoggles.android.api.service.UserClient;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ScrapAdapter extends ArrayAdapter<Scrap> {
    private static final String TAG = "ScrapAdapter";


    TokenManager tokenManager;

    String scrapId;
    String token;
    Boolean visibilty;

    Call<AccessToken> call1;
    UserClient userClient1;





    public ScrapAdapter(@NonNull Context context, int resource, @NonNull List<Scrap> objects) {
        super(context, resource, objects);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.scrap_item,parent,false);

        }
        tokenManager = TokenManager.getInstance(getContext().getSharedPreferences("prefs", MODE_PRIVATE));
        token="Bearer "+tokenManager.getAccess();
        userClient1 = RetrofitBuilder.createService(UserClient.class);



      //  final ImageView scrapVisible =(ImageView) listItemView.findViewById(R.id.scrap_visibility);
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
            if(second==1)
            {
                final_time = second + " Second " + "ago";
            }
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
            else if(hour!=1)
                final_time = hour + " Hour "+"ago";


        } else if (day >= 7) {
            if (day > 360) {

                    final_time = (day / 360) + " Years " + "ago";


            } else if (day > 30) {
                if(day/30==1)
                final_time = (day / 30) + " Month " + "ago";
                else if(day/30!=1)
                    final_time = (day / 30) + " Months " + "ago";

            } else {
                if(day/7==1)
                final_time = (day / 7) + " Week " + "ago";
                else if(day/7!=1)
                    final_time = (day / 7) + " Weeks " + "ago";


            }
        } else if (day < 7) {
            if(day==1)
            final_time = day+" Day "+"ago";
            else if (day!=1)
                final_time = day+" Days "+"ago";

        }

        timestamp.setText(final_time);

        TextView content = (TextView)listItemView.findViewById(R.id.scrap_content);
        String objectContent = currentObject.getContent();
        content.setText(objectContent);

        ImageView scrap_visibility_icon = (ImageView)listItemView.findViewById(R.id.scrap_visibility);
        TextView scrapContent = (TextView) listItemView.findViewById(R.id.scrap_content);

        if(currentObject.getVisibility() == true)
        {

            scrap_visibility_icon.setImageResource(R.drawable.visibility);

            scrapContent.setBackgroundResource(R.drawable.scrap_text_round_bg);
        }


        photo.setOnClickListener(new View.OnClickListener() {
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

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                scrapId = currentObject.getId().substring(0, 3);
                Boolean visibilityFlag = currentObject.getVisibility();

                toggleScrap();
                if (visibilityFlag == false) {
                    currentObject.setVisibility(true);
                    scrap_visibility_icon.setImageResource(R.drawable.visibility);
                    content.setBackgroundResource(R.drawable.scrap_text_round_bg);
                    //   Toast.makeText(getActivity(),"Scrap is visible now",Toast.LENGTH_SHORT).show();
                }  if (visibilityFlag == true) {
                    currentObject.setVisibility(false);
                    scrap_visibility_icon.setImageResource(R.drawable.hide_scrap);
                    content.setBackgroundResource(R.drawable.grey_overlay);
                    //  Toast.makeText(getActivity(),"Scrap is hidden now",Toast.LENGTH_SHORT).show();

                }

            }




        });

        scrap_visibility_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrapId = currentObject.getId().substring(0, 3);
                Boolean visibilityFlag = currentObject.getVisibility();

                toggleScrap();
                if (visibilityFlag == false) {
                    currentObject.setVisibility(true);
                    scrap_visibility_icon.setImageResource(R.drawable.visibility);
                    content.setBackgroundResource(R.drawable.scrap_text_round_bg);
                    //   Toast.makeText(getActivity(),"Scrap is visible now",Toast.LENGTH_SHORT).show();
                }  if (visibilityFlag == true) {
                    currentObject.setVisibility(false);
                    scrap_visibility_icon.setImageResource(R.drawable.hide_scrap);
                    content.setBackgroundResource(R.drawable.grey_overlay);
                    //  Toast.makeText(getActivity(),"Scrap is hidden now",Toast.LENGTH_SHORT).show();

                }

            }
        });




        return listItemView;


        /**  scrapVisible.setOnClickListener(new View.OnClickListener() {
               int visible=0;

              @Override
              public void onClick(View v) {
                  if(visible==1) {
                      content.setBackground(scrapVisible.getResources().getDrawable(R.drawable.grey_overlay));
                      visible=0;
                       hideScrap();
                  }
                  else if(visible==0){
                      content.setBackground(scrapVisible.getResources().getDrawable(
                              R.drawable.scrap_text_round_bg));
                      visible=1;
                       showScrap();
                  }

              }
          });



      }

      void hideScrap(){
          call=userClient.ScrapVisibility(token,id);
          call.enqueue(new Callback<AccessToken>() {
              @Override
              public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                  Log.w(TAG, "onResponse: "+(response.body()));

                  if (response.isSuccessful())
                  {
                      if(response.body().getMsg().equals("Updation successfu"))
                      {
                          Toast.makeText(getContext(),"Updation successful",Toast.LENGTH_LONG)
                                  .show();
                      }
                  }
              }

              @Override
              public void onFailure(Call<AccessToken> call, Throwable t) {

              }
          });




      }
      void showScrap(){
          call=userClient.ScrapVisibility(token,id);

          call.enqueue(new Callback<AccessToken>() {
              @Override
              public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                  Log.w(TAG, "onResponse: "+(response.body()));

                  if (response.isSuccessful())
                  {
                      if(response.body().getMsg().equals("Updation successfu"))
                      {
                          Toast.makeText(getContext(),"Updation successful",Toast.LENGTH_LONG)
                                  .show();
                      }
                  }
              }

              @Override
              public void onFailure(Call<AccessToken> call, Throwable t) {

              }
          });

      }
              **/

}
    void toggleScrap(){
        call1=userClient1.ScrapVisibility(token,scrapId);
        call1.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                if (response.isSuccessful())
                {
                    if(response.body().getMsg().equals("Updation successful"))
                    {
                        Toast.makeText(getContext(),"Updation successful",Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                if(call.isCanceled()){
                    return;
                }
                else {
                    Toast.makeText(getContext(),"Check your internet connection.",Toast.LENGTH_LONG).show();

                }

            }
        });



    }
}
