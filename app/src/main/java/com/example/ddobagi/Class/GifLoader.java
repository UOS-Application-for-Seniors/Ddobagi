package com.example.ddobagi.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ddobagi.R;

public class GifLoader {

    static public boolean needToLoadGif(String usingFragment, SharedPreferences share){
        if(share.getInt(usingFragment, 0) == 1){
            return true;
        }
        else{
            return false;
        }
    }

    static public void setSkipGif(String usingFragment, SharedPreferences share){
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(usingFragment, 0);
        editor.commit();
    }

    static public void initSkipGif(SharedPreferences share){
        SharedPreferences.Editor editor = share.edit();

        editor.putInt("drawClock", 1);
        editor.putInt("traceShape", 1);
        editor.putInt("paintShape", 1);

        editor.commit();
    }

    static public void loadGif(Context context, ImageView imgView, String usingFragment){
        switch (usingFragment){
            case "drawClock":
                Glide.with(context).load(R.raw.draw_clock).into(imgView);
                break;
            case "traceShape":
                Glide.with(context).load(R.raw.trace_shape).into(imgView);
                break;
            case "paintShape":
                Glide.with(context).load(R.raw.paint_shape).into(imgView);
                break;
            default:
                Log.d("loadGif", "잘못된 usingFragment");
                break;
        }
    }
}
