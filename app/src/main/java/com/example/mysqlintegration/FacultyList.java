package com.example.mysqlintegration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Intent;
import android.os.Bundle;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FacultyList extends AppCompatActivity {

    public RecyclerView recyclerView=null;
    private ArrayList<Faculty> facultyList= new ArrayList<Faculty>();
    private String url="https://recapitulative-cake.000webhostapp.com/getFaculty.php";
    private String deleteUrl="https://recapitulative-cake.000webhostapp.com/deleteItem.php";
    private FloatingActionButton fab=null;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout=null;
    public MyAdapter2 myAdapter;
    public MenuItem delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_list);
        recyclerView=findViewById(R.id.recyclerView);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        fab=findViewById(R.id.fab);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListFromServer();
                //stop refreshing when the task is completed
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FacultyList.this, AddFaculty.class);
                startActivity(intent);
            }
        });
        getListFromServer();


    }
    public void getListFromServer(){
        ArrayList<Faculty> faculties= new ArrayList<Faculty>();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                    try {
                        for(int i=0;i<response.length();i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Faculty faculty=new Faculty(jsonObject.getString("image"),jsonObject.getString("name"),jsonObject.getString("designation"),
                                    jsonObject.getString("department"),jsonObject.getString("CabinNo"),
                                    jsonObject.getString("emailId"));
                            faculties.add(faculty);
                        }
                        facultyList.removeAll(facultyList);

                        facultyList.addAll(faculties);
                        myAdapter=new MyAdapter2(FacultyList.this,facultyList,delete);
                        recyclerView=findViewById(R.id.recyclerView);
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FacultyList.this));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(FacultyList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FacultyList.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);


    }
    @Override
    protected void onResume() {
        super.onResume();
        getListFromServer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.home_menus,menu);
        delete=menu.findItem(R.id.delete);
        delete.setVisible(false);
        MenuItem menuItem=menu.findItem(R.id.search);
        searchView=(SearchView) menuItem.getActionView();


//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Faculty> arrayList=new ArrayList<Faculty>();
                for (int i=0;i<facultyList.size();i++){
                    if (facultyList.get(i).name.toLowerCase().contains(newText.toLowerCase())) {
                        arrayList.add(facultyList.get(i));
                    }
                }
                MyAdapter2 myAdapter2=new MyAdapter2(FacultyList.this,arrayList,delete);
                recyclerView.setAdapter(myAdapter2);
                recyclerView.setLayoutManager(new LinearLayoutManager(FacultyList.this));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:{
                if(facultyList.size()>1)
                    facultyList.removeAll(myAdapter.updatedList);
                else if(facultyList.size()==1)
                    facultyList.clear();
                MyAdapter2 newMyAdapter = new MyAdapter2(FacultyList.this, facultyList,delete);
                recyclerView.setAdapter(newMyAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                for(int i=0;i<myAdapter.updatedList.size();i++){
                    deleteFromServer(myAdapter.updatedList.get(i));
                }
                delete.setVisible(false);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteFromServer(Faculty faculty) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(FacultyList.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FacultyList.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<String ,String>();
                hashMap.put("user",faculty.email.toString().trim());
                return hashMap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }
}