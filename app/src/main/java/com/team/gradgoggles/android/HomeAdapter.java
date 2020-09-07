package com.team.gradgoggles.android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class HomeAdapter extends ArrayAdapter<HomeClass> {


    public HomeAdapter(@NonNull Context context, int resource, @NonNull List<HomeClass> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.home_item,parent,false);

        }

        HomeClass currentObject = getItem(position);

        ImageView photo=(ImageView)listItemView.findViewById(R.id.item_pic);
        String objectPhoto = currentObject.getMphoto();


        Picasso.get().load(objectPhoto).transform(new CropCircleTransformation()).placeholder(R.drawable.progress_animation).into(photo);


        TextView name = (TextView)listItemView.findViewById(R.id.item_name);
        String objectName = currentObject.getMname();
        name.setText(objectName);


        TextView department = (TextView)listItemView.findViewById(R.id.item_department);
        String objectDepartment = currentObject.getMdepartment();
        department.setText(objectDepartment);


        TextView quote = (TextView)listItemView.findViewById(R.id.item_quote);
        String objectQuote = currentObject.getMquote();
        quote.setText(objectQuote);

        TextView id = (TextView)listItemView.findViewById(R.id.item_id);
        String Id = currentObject.getMid();

        id.setText(Id);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String STUDENT_ID = currentObject.getMid();
                String STUDENT_NAME = currentObject.getMname();
                Intent intent = new Intent(getContext(), Student.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("Student Id", STUDENT_ID);
                intent.putExtra("Student Name",STUDENT_NAME);
                getContext().startActivity(intent);
            }
        });

        return listItemView;

    }


}
