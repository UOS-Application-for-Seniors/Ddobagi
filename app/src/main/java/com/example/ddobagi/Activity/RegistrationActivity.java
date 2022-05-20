package com.example.ddobagi.Activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText UserID, UserPassword, UserPasswordCheck, address1, address2, userName,userBirthYear,userBirthMonth, userBirthDay, NOKName, NOKPhoneNumber, NOKNotificationDays;
    Button save_Btn, duplicate_check;
    int educationlevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        duplicate_check = findViewById(R.id.btn_duplicate);
        UserID = findViewById(R.id.userID);
        UserPassword = findViewById(R.id.userPassword);
        UserPasswordCheck = findViewById(R.id.userPasswordCheck);
        save_Btn = findViewById(R.id.btn_save);
        address1 = findViewById(R.id.userAdress1);
        address2 = findViewById(R.id.userAdress2);
        userName = findViewById(R.id.userName);
        Spinner userEducationLevel = (Spinner) findViewById(R.id.userEdu);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.education_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        userEducationLevel.setAdapter(adapter);
        userBirthYear = findViewById(R.id.userBirthYear);
        userBirthMonth = findViewById(R.id.userBirthMonth);
        userBirthDay = findViewById(R.id.userBirthDay);
        NOKName = findViewById(R.id.NOKName);
        NOKPhoneNumber = findViewById(R.id.NOKNumber);
        NOKNotificationDays = findViewById(R.id.NOKnotifi);

        userEducationLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                educationlevel = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                educationlevel = 0;
            }
        });

        duplicate_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, Communication.idCheckUrl, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        if(Integer.parseInt(s) != 0){
                            Toast.makeText(RegistrationActivity.this, "중복된 아이디입니다", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(RegistrationActivity.this, "사용하실 수 있습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegistrationActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", UserID.getText().toString());
                        // 추가 내용

                        return params;
                    }
                };

                Communication.requestQueue.add(request);
            }
        });

        Button exitBtn = findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userBirthDate = userBirthYear.getText().toString()+"-"+userBirthMonth.getText().toString()+"-"+userBirthDay.getText().toString();
                String address = address1.getText().toString()+","+address2.getText().toString();
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("name",userName.getText().toString());
                    jsonObject.put("id", UserID.getText().toString());
                    jsonObject.put("password",UserPassword.getText().toString());
                    jsonObject.put("userBirthDate", userBirthDate);
                    jsonObject.put("Address", address);
                    jsonObject.put("userEducationLevel", educationlevel);
                    jsonObject.put("NOKName", NOKName.getText().toString());
                    jsonObject.put("NOKPhoneNumber", NOKPhoneNumber.getText().toString().replace("-",""));
                    jsonObject.put("NOKNotificationDays", Integer.parseInt(NOKNotificationDays.getText().toString()));
                    jsonArray.put(jsonObject);
                    Log.i("jsonString", jsonObject.toString());
                }catch(Exception e){
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Communication.registerUrl, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(RegistrationActivity.this, "회원가입 성공", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error.Response", error.toString());
                                String json = null;
                                NetworkResponse response = error.networkResponse;
                                if(response != null && response.data != null){
                                    switch(response.statusCode){
                                        case 400:
                                            json = new String(response.data);
                                            System.out.println(json);
                                            break;
                                    }
                                    //Additional cases
                                }
                            }
                        }
                        );
                Communication.requestQueue.add(jsonObjectRequest);

                };

        });

    }
}