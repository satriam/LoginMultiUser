package com.example.kembarlaundryapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

        EditText username, password;
        Button login, register;
        ProgressDialog progressDialog;
        private String level;


        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

                username = (EditText) findViewById(R.id.edt_usernameLogin);
                password = (EditText) findViewById(R.id.edt_passwordLogin);
                login = (Button) findViewById(R.id.btn_loginLogin);
                register = (Button) findViewById(R.id.btn_registerLogin);
                progressDialog = new ProgressDialog(LoginActivity.this);
                login.setOnClickListener(this);

                register.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent registerIntent = new Intent(LoginActivity.this, SignupActivity.class);
                                startActivity(registerIntent);
                        }
                });

        }

        public void onClick(View v) {

                String ur=username.getText().toString().trim();
                String pw=password.getText().toString().trim();
                if (ur.equals("")) {
                        username.setError("Belum diisi");
                        username.requestFocus();
                } else if (pw.equals("")) {
                        password.setError("Belum diisi");
                        password.requestFocus();
                } else {
                        StringRequest simpan = new StringRequest(Request.Method.POST, DbContract.login,
                                new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                                Log.d("Response",""+response);
                                                try {
                                                if (response.contains("1")) {
                                                        if (response.contains("pegawai")) {

                                                                JSONObject jsonObject = new JSONObject(response);
                                                                JSONArray hasil = jsonObject.getJSONArray("login");

                                                                for (int i = 0; i < hasil.length(); i++) {
                                                                        JSONObject c = hasil.getJSONObject(i);
                                                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                                                        finish();
//                                                                        Toast.makeText(LoginActivity.this,"PEGAWAI", Toast.LENGTH_LONG).show();

                                                                }
                                                        }else if(response.contains("pelanggan")){
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                JSONArray hasil = jsonObject.getJSONArray("login");

                                                                for (int i = 0; i < hasil.length(); i++) {
                                                                        JSONObject c = hasil.getJSONObject(i);
//                                                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
//                                                                        finish();
                                                                        Toast.makeText(LoginActivity.this,"PELANGGAN", Toast.LENGTH_LONG).show();
                                                                }
                                                        }
                                                }else   if (response.contains("0")) {

                                                        Toast.makeText(LoginActivity.this,"GAGAL LOGIN", Toast.LENGTH_LONG).show();
                                                }
                                        }catch (JSONException e) {
                                                e.printStackTrace();
                                        }

                                        }
                                }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                        Log.e("ERROR", "onErrorResponse: " + error.getMessage());
                                }
                        }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> param = new HashMap<>();
                                        param.put(DbContract.username, ur);
                                        param.put(DbContract.password, pw);
//
                                        return param;
                                }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(simpan);
                }
        }


}
