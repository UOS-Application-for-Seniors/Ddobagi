package com.example.ddobagi.Class;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Request {
    public static RequestQueue requestQueue;

    public static void init(Context context){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
    }
}
