package com.example.ddobagi.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.PlayResultAdapter;
import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.Class.QuizResult;
import com.example.ddobagi.Class.Record;
import com.example.ddobagi.Class.RecordAdapter;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recyclerView = findViewById(R.id.record_recycler);
        Button exitBtn = findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecordAdapter();
        recyclerView.setAdapter(adapter);

        getRecord();
    }

    private void getRecord(){
        SharedPreferences share = getSharedPreferences("PREF", MODE_PRIVATE);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Communication.recordUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Communication.println("응답 --> " + response);
                        onGetRecordResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(!String.valueOf(error).equals("com.android.volley.TimeoutError")){
                            if(error.networkResponse.statusCode==401) {
                                Communication.refreshToken(getApplicationContext());
                            }
                        }
                        else{
                            Communication.handleVolleyError(error);
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + share.getString("Access_token", ""));

                return params;
            }
        };

        request.setShouldCache(false);
        Communication.requestQueue.add(request);
        Communication.println("gameList 요청 보냄");
    }

    private void onGetRecordResponse(String response){
        Gson gson = new Gson();
        Record[] tmp = gson.fromJson(response, Record[].class);
        if(tmp == null){
            Log.d("warning","quizList is null");
            return;
        }

        for(int i=0;i<tmp.length;i++){
            adapter.addItem(tmp[i]);
        }
        adapter.notifyDataSetChanged();
    }
}