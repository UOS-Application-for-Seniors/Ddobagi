package com.example.ddobagi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.example.ddobagi.Activity.*;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MAIN = 101;
    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button nextBtn  = findViewById(R.id.login_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest();
            }
        });

        Button playBtn = findViewById(R.id.play_btn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GameTestActivity.class);
                startActivity(intent);
            }
        });

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    public void makeRequest(){
        String url = "http://121.164.170.67:3000/users";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
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
        requestQueue.add(request);
        println("요청 보냄.");
    }

    public void println(String data){
        Log.d("MainActivity", data);
    }
}