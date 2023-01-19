package com.example.mysqlintegration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LogIn extends AppCompatActivity {
    private Button createNewAccount=null;
    private TextInputEditText logInEmail=null;
    private TextInputEditText logInPassword=null;
    private Button login=null;
    private String url="http://192.168.29.116/StudentGuidance/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        createNewAccount=findViewById(R.id.createNewAccount);
        logInEmail=findViewById(R.id.logInEmail);
        logInPassword=findViewById(R.id.logInPassword);
        login=findViewById(R.id.login);
        checkExistingRecord();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.requireNonNull(logInEmail.getText()).toString().equals("") && !Objects.requireNonNull(logInPassword.getText()).toString().equals("")) {
                    checkInServer();
                }
                else{
                    Toast.makeText(LogIn.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogIn.this,SignUp.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void checkInServer() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(Objects.equals(response, "found")){
                    SharedPreferences sharedPreferences=getSharedPreferences("credentials", MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("email", Objects.requireNonNull(logInEmail.getText()).toString());
                    editor.putString("password", Objects.requireNonNull(logInPassword.getText()).toString());
                    editor.apply();
                    startActivity(new Intent(LogIn.this,FacultyList.class));
                    logInEmail.setText("");
                    logInPassword.setText("");
                    finish();

                }
                else{
                    Toast.makeText(LogIn.this, Objects.requireNonNull(logInEmail.getText())+" does not exist",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LogIn.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<String,String>();
                hashMap.put("email", Objects.requireNonNull(logInEmail.getText()).toString().trim());
                hashMap.put("password",Objects.requireNonNull(logInPassword.getText()).toString().trim());
                return hashMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void checkExistingRecord() {
        SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
        if(sp.contains("email")){
            logInEmail.setText(sp.getString("email",""));
            logInPassword.setText(sp.getString("password",""));
        }
        else{
            Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
        }
    }
}