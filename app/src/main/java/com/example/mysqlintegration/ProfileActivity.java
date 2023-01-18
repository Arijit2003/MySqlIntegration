package com.example.mysqlintegration;

import static android.provider.CalendarContract.CalendarCache.URI;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private ImageView facImage=null;
    private TextView facName=null;
    private TextView facDesignation=null;
    private TextView cabinNoValue=null;
    private TextView emailValue=null;
    private TextView departmentValue=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        facImage=findViewById(R.id.facImage);
        facName=findViewById(R.id.facName);
        facDesignation=findViewById(R.id.facDesignation);
        cabinNoValue=findViewById(R.id.cabinNoValue);
        emailValue=findViewById(R.id.emailValue);
        departmentValue=findViewById(R.id.departmentValue);



        if(Objects.equals(getIntent().getStringExtra("class"), "AddFaculty")) {
            facImage.setImageURI(Uri.parse(getIntent().getStringExtra("facImage")));
            facName.setText(getIntent().getStringExtra("facName"));
            facDesignation.setText(getIntent().getStringExtra("facDesignation"));
            cabinNoValue.setText(getIntent().getStringExtra("facCabin"));
            emailValue.setText(getIntent().getStringExtra("facEmail"));
            departmentValue.setText(getIntent().getStringExtra("facDepartment"));
        }
        else if(Objects.equals(getIntent().getStringExtra("class"), "MyAdapter2")) {

            Glide.with(this)

                    .load(getIntent().getStringExtra("facImage"))

                    .into(facImage);

                    //.error(R.drawable.imagenotfound);


            //facImage.setImageBitmap(getBitmapFromURL(getIntent().getStringExtra("facImage")));
                facName.setText(getIntent().getStringExtra("facName"));
                facDesignation.setText(getIntent().getStringExtra("facDesignation"));
                cabinNoValue.setText(getIntent().getStringExtra("facCabin"));
                emailValue.setText(getIntent().getStringExtra("facEmail"));
                departmentValue.setText(getIntent().getStringExtra("facDepartment"));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}