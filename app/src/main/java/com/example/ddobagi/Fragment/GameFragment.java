package com.example.ddobagi.Fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Activity.PlayActivity;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.LoadImage;

import java.util.HashMap;
import java.util.Map;

public abstract class GameFragment extends Fragment {
    int gameID, quizID, difficulty;
    final int maxRemainNum = 99;
    int ttsRemainNum = maxRemainNum;
    String quizTTS;
    String gameName;
    String gameField;
    String detail = "문제 설명";
    Bitmap pictures[];
    String helpdata;
    boolean isReadyToShow = false;
    boolean isReadyToCommit = false;

    public String getQuizTTS() {
        if(ttsRemainNum > 0){
            ttsRemainNum--;
            return quizTTS;
        }
        else{
            return "exceeded";
        }
    }

    boolean isSTTAble;

    abstract public int commit();
    abstract void onHelp();
    abstract public void receiveSTTResult(String voice);
    public void init(){
        quizTTS = "";
    }

    public void loadGame(int gameID, int quizID, int difficulty){
        isReadyToCommit = false;
        this.gameID = gameID;
        this.quizID = quizID;

//        PlayActivity play = (PlayActivity) getActivity();
//        if(play.isTest){}

        if(gameID == 35 || gameID == 36 || gameID == 37 || gameID == 38){
            ttsRemainNum = 1;
        }
        else if(gameID == 39 || gameID == 53){
            ttsRemainNum = 2;
        }
        else{
            ttsRemainNum = maxRemainNum;
        }
        //Log.d("ttsRemainNum", Integer.toString(ttsRemainNum));


        this.difficulty = difficulty;
        getGameData();
    }

    public boolean isReadyToShow(){
        return isReadyToShow;
    }

    public void setReadyToShow(boolean readyToShow){
        this.isReadyToShow = readyToShow;
    }

    public boolean isReadyToCommit() {
        return isReadyToCommit;
    }

    public void setReadyToCommit(boolean readyToCommit) {
        isReadyToCommit = readyToCommit;
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
                        isReadyToShow = true;
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

    public void setImageOnButton(String url, Button button, int bound, int location) {
        LoadImage loadImage = new LoadImage((bitmap) -> {
            if(bitmap != null){
                Drawable drawable;
                drawable = new BitmapDrawable(bitmap);
                drawable.setBounds(0, 0, bound, bound);
                switch(location){
                    case 0:
                        button.setCompoundDrawables(drawable, null, null, null);
                        break;
                    case 1:
                        button.setCompoundDrawables(null, drawable, null, null);
                        button.setPadding(0, 10, 0, 10);
                        break;
                    case 2:
                        button.setCompoundDrawables(null, null, drawable, null);
                        button.setPadding(0, 0, 0, 0);
                        break;
                    case 3:
                        button.setCompoundDrawables(null, null, null, drawable);
                        button.setPadding(0, 0, 0, 0);
                        break;
                    case 4:
                        button.setCompoundDrawables(null, drawable, null, null);
                        button.setPadding(0, 50, 0, 0);
                        break;
                }
            }
            else{
                button.setTextSize(40.0f);
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

    public String normalizationString(String str){
        str = str.replace(" ", "");
        str = str.replace(",", "");
        str = str.replace(".", "");
        str = str.replace("-", "");

        return str;
    }

    public boolean isSTTAble() {
        return isSTTAble;
    }
}
