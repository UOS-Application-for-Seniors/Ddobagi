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
    int gameID, quizID, difficulty;
    String quizTTS;
    String gameName;
    String gameField;
    String detail = "문제 설명";
    Bitmap pictures[];
    String helpdata;

    public String getQuizTTS() {
        return quizTTS;
    }

    boolean isSTTAble;

    abstract public int commit();
    abstract void onHelp();
    abstract public void receiveSTTResult(String voice);
    public void init(){
        quizTTS = "";
    }

    public void loadGame(int gameID, int quizID, int difficulty){
        this.gameID = gameID;
        this.quizID = quizID;
        this.difficulty = difficulty;
        getGameData();
    }

    void getGameData(){
        String url = Communication.getQuizUrl + Integer.toString(gameID) + "/" + Integer.toString(quizID);
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
            if(bitmap != null){
                Drawable drawable;
                drawable = new BitmapDrawable(bitmap);
                drawable.setBounds(0, 0, bound, bound);
                button.setCompoundDrawables(null, drawable, null, null);
            }
            else{
                button.setTextSize(50.0f);
            }
        });
        loadImage.execute(url);
    }

    // setImage method works on ImageView
    public void setImageOnImageView(String url, ImageView view, int bound){
        LoadImage loadImage = new LoadImage((bitmap) -> {
            if(bitmap!= null){
                Drawable drawable;
                drawable = new BitmapDrawable(bitmap);
                drawable.setBounds( 0, 0, bound, bound);
                view.setImageDrawable(drawable);
            }
        });
        loadImage.execute(url);
    }

    public boolean isSTTAble() {
        return isSTTAble;
    }
}
