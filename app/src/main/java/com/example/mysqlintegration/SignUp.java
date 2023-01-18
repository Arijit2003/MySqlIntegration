package com.example.mysqlintegration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class SignUp extends AppCompatActivity {
    private Button loginBack=null;
    private TextInputEditText signUpName=null;
    private TextInputEditText signUpEmail=null;
    private TextInputEditText signUpPassword=null;
    private Button createAccount=null;
    private String url="http://192.168.29.116/StudentGuidance/signup.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        loginBack=findViewById(R.id.loginBack);
        signUpName=findViewById(R.id.signUpName);
        signUpEmail=findViewById(R.id.signUpEmail);
        signUpPassword=findViewById(R.id.signUpPassword);
        createAccount=findViewById(R.id.createAccount);
        loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,LogIn.class);
                startActivity(intent);
                finish();
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Objects.requireNonNull(signUpName.getText()).toString().equals("") && !Objects.requireNonNull(signUpEmail.getText()).toString().equals("") &&!Objects.requireNonNull(signUpPassword.getText()).toString().equals("")) {
                    postInServer();
                }
                else{
                    Toast.makeText(SignUp.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
                signUpName.setText("");
                signUpEmail.setText("");
                signUpPassword.setText("");
            }
        });
    }

    private void postInServer() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(SignUp.this, response.toString(), Toast.LENGTH_SHORT).show();
                if(Objects.equals(response, "inserted")){
                    Intent intent=new Intent(SignUp.this,FacultyList.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap= new HashMap<String,String>();
                hashMap.put("name",signUpName.getText().toString());
                hashMap.put("email",signUpEmail.getText().toString());
                hashMap.put("password",signUpPassword.getText().toString());

                return hashMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}