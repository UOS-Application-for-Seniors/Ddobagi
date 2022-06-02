package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.example.ddobagi.R;
import com.example.ddobagi.View.DrawClockView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DrawClockFragment extends GameFragment{
    DrawClockView drawClockView;
    TextView detailTextView;
    String detail;
    String quizAnswer;

    public DrawClockFragment(){
        isSTTAble = false;
    }
    public int commit(){
        ArrayList<Line> lineList = drawClockView.getLineList();
        String[] answer = quizAnswer.split(",");

        if(lineList.size() <= 0){
            return 0;
        }
        else if(lineList.size() == 1){
            if(Integer.parseInt(answer[0]) == lineList.get(0).end){
                return 2;
            }
            else{
                return 0;
            }
        }

        if(Integer.parseInt(answer[0]) == lineList.get(0).end && Integer.parseInt(answer[1]) == lineList.get(1).end){
            return 1;
        }
        else if(Integer.parseInt(answer[0]) == lineList.get(0).end || Integer.parseInt(answer[1]) == lineList.get(1).end){
            return 2;
        }

        return 0;
    }

    public void receiveSTTResult(String voice){

    }

    void onHelp(){

    }

    public void onGetGameDataResponse(String response){
        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        detailTextView.setText(detail);
        quizAnswer = quiz.quizanswer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_draw_clock, container, false);
        detailTextView = rootView.findViewById(R.id.quizDetail);
        drawClockView = rootView.findViewById(R.id.draw_clock_view);

        return rootView;
    }

    public void init(){
        super.init();
        drawClockView.clearLineList();
    }
}
