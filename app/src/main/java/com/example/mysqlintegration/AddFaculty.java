package com.example.mysqlintegration;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddFaculty extends AppCompatActivity {
    private ImageView facImage=null;
    private EditText nameValue=null;
    private EditText designationValue=null;
    private EditText departmentValue=null;
    private EditText cabinValue=null;
    private EditText emailValue=null;
    private String encodedImageString=null;
    private Bitmap bitmap=null;
    private String ImageUri=null;
    private CardView cardView=null;
    public String url="https://recapitulative-cake.000webhostapp.com/postData.php";
    public ActivityResultLauncher<String> requestStoragePermissionResult=
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result){
                        storageResultLauncher.launch("image/*");
                    }
                    else{
                        popUpAlertDialog("Storage access denied","Can't access photos");
                    }
                }
            });

    public ActivityResultLauncher<String> storageResultLauncher=
            registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    try {
                        if(result!=null) {
                            facImage.setImageURI(result);
                            ImageUri = result.toString();
                            InputStream inputStream = getContentResolver().openInputStream(result);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            encodeBitmapImage(bitmap);
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


    private void popUpAlertDialog(String title, String message) {
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_faculty);
        facImage=findViewById(R.id.facImage);
        nameValue=findViewById(R.id.nameValue);
        designationValue=findViewById(R.id.designationValue);
        departmentValue=findViewById(R.id.departmentValue);
        cabinValue=findViewById(R.id.cabinValue);
        emailValue=findViewById(R.id.emailValue);
        cardView=findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menus,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(facImage !=null && nameValue!=null  && designationValue!=null && departmentValue!=null && cabinValue!=null && emailValue !=null) {
            switch (item.getItemId()) {
                case R.id.save: {
                    saveToServer(nameValue.getText().toString(),designationValue.getText().toString(),departmentValue.getText().toString(),cabinValue.getText().toString(),emailValue.getText().toString(),encodedImageString);
                    Intent intent=new Intent(this,ProfileActivity.class);
                    intent.putExtra("facImage",ImageUri.toString())
                            .putExtra("facName",nameValue.getText().toString())
                            .putExtra("facDesignation",designationValue.getText().toString())
                            .putExtra("facCabin", cabinValue.getText().toString())
                            .putExtra("facEmail",emailValue.getText().toString())
                            .putExtra("facDepartment",departmentValue.getText().toString())
                            .putExtra("class","AddFaculty");

                    startActivity(intent);
                    finish();
//                    facImage.setImageResource(R.drawable.img);
//                    nameValue.setText("");
//                    designationValue.setText("");
//                    cabinValue.setText("");
//                    emailValue.setText("");
//                    designationValue.setText("");

                }
            }
        }
        else{
            Toast.makeText(this, "fill all the fields", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void saveToServer(String name, String  designationValue, String  departmentValue, String  cabinValue, String emailValue, String encodedImageString) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddFaculty.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddFaculty.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > hashMap=new HashMap<String,String>();
                hashMap.put("name",name);
                hashMap.put("designation",designationValue);
                hashMap.put("department",departmentValue);
                hashMap.put("cabinno",cabinValue);
                hashMap.put("emailId",emailValue);
                hashMap.put("image",encodedImageString);

                return hashMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}