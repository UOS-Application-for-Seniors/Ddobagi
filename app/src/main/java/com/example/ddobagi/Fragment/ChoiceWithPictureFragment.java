package com.example.ddobagi.Fragment;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.Line;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.Class.LoadImage;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChoiceWithPictureFragment extends GameFragment {
    TextView quizDetail;
    ImageView imageView;
    int choiceNum = 4;
    Button[] choiceBtn = new Button[choiceNum];
    String quizAnswer;

    public void commit(){

    }

    void onHelp(){

    }

    void loadGame(){
        getGameData();
    }

    void getGameData(){
        String url = "http://121.164.170.67:3000/quiz/5";
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

    public void onGetGameDataResponse(String response){
        int i = 0;
        String url = "http://121.164.170.67:3000/file/";
        String quizdatapathUrl;

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        quizdatapathUrl = url + quiz.quizdatapath + "/";

        quizDetail.setText(quiz.quizdetail);
        quizAnswer = quiz.quizanswer;

        String[] splitString = quiz.quizchoicesdetail.split(",");

        for(;i<choiceNum;i++){
            final int inmutable_index = i;
            String tmp = quizdatapathUrl;
            tmp = tmp + Integer.toString(i) + ".jfif";
            setImage(tmp, choiceBtn[i]);
            // choiceBtn[i].setText(splitString[i]);
            choiceBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "정답이 아닙네다", Toast.LENGTH_LONG).show();
                }
            });
        }

        choiceBtn[Integer.parseInt(quizAnswer)].setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "정답입네다", Toast.LENGTH_LONG).show();
            }
        }));

        // Setup Image on Image View
        String tmp = quizdatapathUrl;
        tmp = tmp + Integer.toString(i) + ".jfif";
        setImageOnImageView(tmp, imageView);
    }

    private void setImage(String url, Button button){
        LoadImage loadImage = new LoadImage((bitmap) -> {
            Drawable drawable;

            drawable = new BitmapDrawable(bitmap);
            if(drawable != null){
                drawable.setBounds( 0, 0, 120, 120);
                button.setCompoundDrawables(null, drawable, null, null);
            }
        });
        loadImage.execute(url);
    }

    // setImage method works on ImageView
    private void setImageOnImageView(String url, ImageView view){
        LoadImage loadImage = new LoadImage((bitmap) -> {
            Drawable drawable;

            drawable = new BitmapDrawable(bitmap);
            if(drawable != null){
                drawable.setBounds( 0, 0, 120, 120);
                view.setImageDrawable(drawable);
            }
        });
        loadImage.execute(url);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_choice_with_picture, container, false);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        imageView = rootView.findViewById(R.id.imageView);
        choiceBtn[0] = rootView.findViewById(R.id.selectBtn1);
        choiceBtn[1] = rootView.findViewById(R.id.selectBtn2);
        choiceBtn[2] = rootView.findViewById(R.id.selectBtn3);
        choiceBtn[3] = rootView.findViewById(R.id.selectBtn4);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadGame();
    }
}
