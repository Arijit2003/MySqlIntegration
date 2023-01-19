package com.example.mysqlintegration;

import static android.provider.CalendarContract.CalendarCache.URI;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private ImageView facImage=null;
    private TextView facName=null;
    private TextView facDesignation=null;
    private TextView cabinNoValue=null;
    private TextView emailValue=null;
    private TextView departmentValue=null;
    private Toolbar toolbar = null;
    private Button saveBtn=null;
    private String oldEmail=null;
    private Bitmap bitmap;
    private String encodedImageString;
    private String url="https://recapitulative-cake.000webhostapp.com/update.php";
    private String imageChange="false";


    private ActivityResultLauncher<String> storageResultLauncher= registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {

        @Override
        public void onActivityResult(Uri result) {

            try {
                if(result!=null) {
                    facImage.setImageURI(result);
                    InputStream inputStream = getContentResolver().openInputStream(result);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    encodeBitmapImage(bitmap);
                    imageChange="true";
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    });

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        Bitmap resized;
        if(bitmap.getWidth()>300 && bitmap.getHeight()>300) {
            resized = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        }
        else{
            resized=bitmap;
        }
        resized.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);

        byte[] bytesOfImage=byteArrayOutputStream.toByteArray();
        encodedImageString=android.util.Base64.encodeToString(bytesOfImage, Base64.DEFAULT);
    }

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
        toolbar=findViewById(R.id.toolbar);
        saveBtn=findViewById(R.id.saveBtn);
        saveBtn.setVisibility(View.INVISIBLE);
        setSupportActionBar(toolbar);
        facImage.setClickable(false);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInServer();
                facName.setEnabled(false);
                facDesignation.setEnabled(false);
                cabinNoValue.setEnabled(false);
                emailValue.setEnabled(false);
                departmentValue.setEnabled(false);
                facImage.setClickable(false);
                facImage.setOnClickListener(null);
                saveBtn.setVisibility(View.INVISIBLE);
                
            }
        });



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

    private void updateInServer() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://recapitulative-cake.000webhostapp.com/update.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ProfileActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > hashMap = new HashMap<String,String>();
                hashMap.put("name",facName.getText().toString().trim());
                hashMap.put("designation",facDesignation.getText().toString().trim());
                hashMap.put("department",departmentValue.getText().toString().trim());
                hashMap.put("cabinNo",cabinNoValue.getText().toString().trim());
                hashMap.put("emailId",emailValue.getText().toString().trim());
                hashMap.put("imageChange",imageChange);
                hashMap.put("oldEmail",oldEmail);
                if(Objects.equals(imageChange, "true")) {
                    hashMap.put("image", encodedImageString);
                }


                return hashMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:{
                onBackPressed();
            }
            case R.id.edit:{
                facName.setEnabled(true);
                facDesignation.setEnabled(true);
                cabinNoValue.setEnabled(true);
                emailValue.setEnabled(true);
                departmentValue.setEnabled(true);
                saveBtn.setVisibility(View.VISIBLE);
                oldEmail=emailValue.getText().toString();
                facImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        storageResultLauncher.launch("image/*");
                    }
                });
            }
        }
        return true;
    }
}