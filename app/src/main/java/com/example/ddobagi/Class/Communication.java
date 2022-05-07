package com.example.ddobagi.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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
import com.example.ddobagi.Activity.LoginActivity;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Communication {
    public static String url = "http://121.164.170.67:3000/users";
    public static String loginUrl = "http://121.164.170.67:3000/auth/login";
    public static String refreshUrl = "http://121.164.170.67:3000/refresh";
    public static String gameListUrl = "http://121.164.170.67:3000/quiz/games";
    public static RequestQueue requestQueue;
    public static SharedPreferences share;
    public static SharedPreferences.Editor edit;

    public static void init(Context context){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public static void handleVolleyError(VolleyError error){
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

    public static void println(String data){
        Log.d("communication", data);
    }

    public static void refreshToken(Context context) {

        Date date = new Date();

        share = context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
        println(share.getString("Refresh_token", null));
        if(share.getString("Refresh_token", null) != null) {
            StringRequest request = new StringRequest(Request.Method.GET, Communication.refreshUrl, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {

                    String[] tmp = s.split(",");
                    String[] access_tmp = tmp[0].split(":");
                    String access_token = access_tmp[1].substring(1, access_tmp[1].length()-1);
                    access_tmp = tmp[1].split(":");
                    String access_token_expiration = access_tmp[1].substring(1, access_tmp[1].length()-2);

                    Communication.println("응답 --> " + access_token);

                    edit = share.edit();
                    edit.putString("Access_token", access_token);
                    edit.putLong("Access_token_expiration", Integer.parseInt(access_token_expiration));
                    edit.putLong("Access_token_time", date.getTime());
                    edit.commit();
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, "Refresh Token Error", Toast.LENGTH_LONG).show();;
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + share.getString("Refresh_token", ""));

                    return params;
                }
            };
            request.setShouldCache(false);
            Communication.requestQueue.add(request);
        }

    }
}

