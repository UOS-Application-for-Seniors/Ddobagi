package com.example.ddobagi.Class;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

public class Communication {
    public static String url = "http://121.164.170.67:3000/users";
    public static String loginUrl = "http://121.164.170.67:3000/auth/login";
    public static RequestQueue requestQueue;

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
}
