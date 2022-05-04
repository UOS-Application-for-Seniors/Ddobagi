package com.example.ddobagi.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button buttonRegistration, buttonLogin, buttonExit;
    EditText UserID, Password;
    SharedPreferences share;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        buttonLogin = findViewById(R.id.btn_login);
        UserID = findViewById(R.id.input_id);
        Password = findViewById(R.id.input_password);
        buttonRegistration = findViewById(R.id.btn_registrarion);
        buttonExit = findViewById(R.id.login_exit_btn);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, Communication.loginUrl, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {

                        String[] tmp = s.split(":");
                        String tmp2 = tmp[1].substring(1, tmp[1].length()-2);
                        Toast.makeText(LoginActivity.this, tmp2 , Toast.LENGTH_LONG).show();

                        share = getSharedPreferences("PREF", MODE_PRIVATE);

                        edit = share.edit();
                        edit.putString("Access_token", tmp2);
                        edit.commit();

                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(LoginActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", UserID.getText().toString());
                        params.put("password", Password.getText().toString());
                        return params;
                    }
                };

                Communication.requestQueue.add(request);
            }
        });
        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }
}