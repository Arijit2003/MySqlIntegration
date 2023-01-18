package com.example.mysqlintegration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {
    public  Context context;
    public ArrayList<Faculty> facultyList;
    public MyAdapter2(Context context, ArrayList<Faculty> facultyList){
        this.context=context;
        this.facultyList=facultyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context)

                .load(facultyList.get(position).image)

                .into(holder.FacultyImage);

        holder.FacultyName.setText(facultyList.get(position).name);
        holder.FacultyDesignation.setText(facultyList.get(position).designation);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("facImage",facultyList.get(position).image.toString())
                        .putExtra("facName",facultyList.get(position).name)
                        .putExtra("facDesignation",facultyList.get(position).designation)
                        .putExtra("facCabin", facultyList.get(position).cabinNo)
                        .putExtra("facEmail",facultyList.get(position).email)
                        .putExtra("facDepartment",facultyList.get(position).department)
                        .putExtra("class","MyAdapter2");

                ContextCompat.startActivity(context,intent,null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return facultyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView FacultyImage;
        TextView FacultyName;
        TextView FacultyDesignation;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            FacultyImage=itemView.findViewById(R.id.FacultyImage);
            FacultyName=itemView.findViewById(R.id.FacultyName);
            FacultyDesignation=itemView.findViewById(R.id.FacultyDesignation);
            linearLayout=itemView.findViewById(R.id.linearLayout);



        }
    }
}
