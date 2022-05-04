package com.example.ddobagi.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.R;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import com.example.ddobagi.Class.*;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MAIN = 101;
    final int PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //통신 requestQueue 초기화
        Communication.init(this);

        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        setButton();
    }

    private void setButton(){
        Button testBtn = findViewById(R.id.main_test_btn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TestPlayActivity.class);
                startActivity(intent);
            }
        });

        Button recommendPlayBtn = findViewById(R.id.main_recommend_play_btn);
        recommendPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("type", "recommend");
                startActivity(intent);
            }
        });

        Button selectPlayBtn = findViewById(R.id.main_select_play_btn);{
            selectPlayBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                    intent.putExtra("type", "select");
                    startActivity(intent);
                }
            });
        }
        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void makeRequest(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Communication.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답 --> " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if(error instanceof ServerError && response != null){
                            try{
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers,"utf-8"));
                                println(res);
                            }catch (UnsupportedEncodingException e1){
                                e1.printStackTrace();
                            }
                        }
                        println("onErrorResponse: " + String.valueOf(error));
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{// id, name, email, password, token
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", "testID");
                params.put("name", "testName");
                params.put("email", "testEmail");
                params.put("password", "testPwd");
                params.put("token", "testToken");

                return params;
            }
        };
        request.setShouldCache(false);
        Communication.requestQueue.add(request);
        println("요청 보냄.");
    }

    public void println(String data){
        Log.d("MainActivity", data);
    }
}