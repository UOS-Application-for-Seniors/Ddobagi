package com.example.ddobagi.Fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.LoadImage;

import java.util.HashMap;
import java.util.Map;

public abstract class GameFragment extends Fragment {
    int gameID, quizID;
    String gameName;
    String gameField;
    String detail;
    Bitmap pictures[];
    String helpdata;

    abstract public int commit();
    abstract void onHelp();
    abstract public void loadGame(int gameID, int quizID);

    void getGameData(){
        String url = "http://121.164.170.67:3000/quiz/" + Integer.toString(gameID) + "/" + Integer.toString(quizID);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Communication.println("응답 --> " + response);
                        onGetGameDataResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Communication.handleVolleyError(error);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("path", "/file/asd/test.png");
//                params.put("name", "1");
//                params.put("fn", "test.jpg");

                return params;
            }
        };
        request.setShouldCache(false);
        Communication.requestQueue.add(request);
        Communication.println("요청 보냄.");
    }

    abstract public void onGetGameDataResponse(String response);

    public void setImageOnButton(String url, Button button, int bound) {
        LoadImage loadImage = new LoadImage((bitmap) -> {
            Drawable drawable;

            drawable = new BitmapDrawable(bitmap);
            if (drawable != null) {
                drawable.setBounds(0, 0, bound, bound);
                button.setCompoundDrawables(null, drawable, null, null);
            }
        });
        loadImage.execute(url);
    }

    // setImage method works on ImageView
    public void setImageOnImageView(String url, ImageView view, int bound){
        LoadImage loadImage = new LoadImage((bitmap) -> {
            Drawable drawable;

            drawable = new BitmapDrawable(bitmap);
            if(drawable != null){
                drawable.setBounds( 0, 0, bound, bound);
                view.setImageDrawable(drawable);
            }
        });
        loadImage.execute(url);
    }
}
