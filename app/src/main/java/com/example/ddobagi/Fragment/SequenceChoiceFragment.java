package com.example.ddobagi.Fragment;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.ddobagi.View.LineConnectionView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SequenceChoiceFragment extends GameFragment{

    TextView quizDetail;
    int choiceNum = 4;
    TextView[] sequenceView = new TextView[choiceNum];
    Button[] choiceBtn = new Button[choiceNum];
    String quizAnswer;
    int array = 0;

    public void commit(){
        String resultStr = "";
        for(int i=0;i<choiceNum;i++) {
            resultStr += sequenceView[i].getText().toString() + ",";
        }
        resultStr = resultStr.substring(0,resultStr.length()-1);
        if(resultStr.equals(quizAnswer)){
            Toast.makeText(getActivity(), "정답입니다", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), quizAnswer+resultStr, Toast.LENGTH_LONG).show();
        }


    }

    void onHelp(){

    }

    void loadGame(){
        getGameData();
    }

    void getGameData(){
        String url = "http://121.164.170.67:3000/quiz/4";
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

        if(quiz == null){
            return;
        }

        quizdatapathUrl = url + quiz.quizdatapath + "/";

        quizDetail.setText(quiz.quizdetail);
        quizAnswer = quiz.quizanswer;

        String[] splitString = quiz.quizchoicesdetail.split(",");

        for(;i<choiceNum;i++){
            final int inmutable_index = i;
            String tmp = quizdatapathUrl;
            tmp = tmp + Integer.toString(i) + ".jfif";
            setImage(tmp, choiceBtn[i]);
            choiceBtn[i].setText(splitString[i]);
            choiceBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tmp = choiceBtn[inmutable_index].getText().toString().substring(0,1);
                    sequenceView[array].setText(tmp);
                    array++;
                    if(array>3) {
                        array = 0;
                    }
                }
            });

        }
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sequence_choice, container, false);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        choiceBtn[0] = rootView.findViewById(R.id.selectBtn1);
        choiceBtn[1] = rootView.findViewById(R.id.selectBtn2);
        choiceBtn[2] = rootView.findViewById(R.id.selectBtn3);
        choiceBtn[3] = rootView.findViewById(R.id.selectBtn4);
        sequenceView[0] = rootView.findViewById(R.id.sequenceView1);
        sequenceView[1] = rootView.findViewById(R.id.sequenceView2);
        sequenceView[2] = rootView.findViewById(R.id.sequenceView3);
        sequenceView[3] = rootView.findViewById(R.id.sequenceView4);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadGame();
    }
}
