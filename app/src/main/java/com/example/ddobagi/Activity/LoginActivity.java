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

import java.util.Date;
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

                        //{"Access_token":"a","Access_token_expiration":"b","Refresh_token":"c","Refresh_token_expiration":"d"}

                        String[] tmp = s.split(",");
                        String[] access_tmp = tmp[0].split(":");
                        String access_token = access_tmp[1].substring(1, access_tmp[1].length()-1);
                        access_tmp = tmp[1].split(":");
                        String access_token_expiration = access_tmp[1].substring(1, access_tmp[1].length()-1);
                        String[] refresh_tmp = tmp[2].split(":");
                        String refresh_token = refresh_tmp[1].substring(1, refresh_tmp[1].length()-1);
                        refresh_tmp = tmp[3].split(":");
                        String refresh_token_expiration = refresh_tmp[1].substring(1, refresh_tmp[1].length()-2);

                        Communication.println("응답 --> " + access_token + " " + access_token_expiration + " " + refresh_token + " " + refresh_token_expiration);

                        share = getSharedPreferences("PREF", MODE_PRIVATE);

                        Date date = new Date();

                        edit = share.edit();
                        edit.putString("Access_token", access_token);
                        edit.putLong("Access_token_expiration", Integer.parseInt(access_token_expiration));
                        edit.putLong("Access_token_time", date.getTime());
                        edit.putString("Refresh_token", refresh_token);
                        edit.putLong("Refresh_token_expiration", Integer.parseInt(refresh_token_expiration));
                        edit.putLong("Refresh_token_time", date.getTime());
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