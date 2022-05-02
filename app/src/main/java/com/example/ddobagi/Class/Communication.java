package com.example.ddobagi.Class;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Communication {
    public static String url = "http://121.164.170.67:3000/users";
    public static String loginUrl = "http://121.164.170.67:3000/auth/login";
    public static RequestQueue requestQueue;

    public static void init(Context context){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
    }
}
