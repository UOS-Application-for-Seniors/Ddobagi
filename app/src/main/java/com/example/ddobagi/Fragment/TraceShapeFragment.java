package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.ExtendedLine;
import com.example.ddobagi.Class.Line;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.example.ddobagi.View.TraceShapeExample;
import com.example.ddobagi.View.TraceShapePractice;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TraceShapeFragment extends GameFragment{
    TextView detailTextView;
    TraceShapeExample example;
    TraceShapePractice practice;

    ArrayList<Line> exampleLineList;

    public TraceShapeFragment(){
        isSTTAble = false;
    }

    public void receiveSTTResult(String voice){

    }

    public int commit(){
        ArrayList<ExtendedLine> practiceLineList = practice.getLineList();
        int result = 0;
        int resultCnt = 0;

        for(Line practiceLine: practiceLineList){
            for(Line exampleLine: exampleLineList){
                int start = exampleLine.start;
                int end = exampleLine.end;
                if(end < start){
                    int temp = start;
                    start = end;
                    end = temp;
                }
                if(practiceLine.start == start && practiceLine.end == end){
                    resultCnt++;
                    break;
                }
            }
        }
        if(resultCnt == exampleLineList.size() && resultCnt == practiceLineList.size()){
            //Toast.makeText(getActivity(), "정답입니다", Toast.LENGTH_LONG).show();
            result = 1;
        }
        else{
            //Toast.makeText(getActivity(), "정답이 아닙니다", Toast.LENGTH_LONG).show();
            result = 0;
        }
        return result;
    }

    void onHelp(){

    }

    public void loadGame(int gameID, int quizID){
        this.gameID = gameID;
        this.quizID = quizID;
        getGameData();
    }


    public void onGetGameDataResponse(String response){
        String url = Communication.getQuizDataUrl + gameID + "/" + quizID + "/";

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        detailTextView.setText(detail);
        String[] splitString = quiz.quizanswer.split(",");
        exampleLineList = new ArrayList<>();

        for(String str: splitString){
            int start, end;
            String[] line = str.split("-");
            start = Integer.parseInt(line[0]);
            end = Integer.parseInt(line[1]);
            exampleLineList.add(new Line(null, start, end));
        }
        example.setLineList(exampleLineList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_trace_shape, container, false);
        detailTextView = rootView.findViewById(R.id.quizDetail);

        example = rootView.findViewById(R.id.trace_shape_example);
        practice = rootView.findViewById(R.id.trace_shape_practice);

//        exampleLineList = new ArrayList<>();
//        exampleLineList.add(new Line(null, 6, 10));
//        exampleLineList.add(new Line(null, 6, 12));
//        exampleLineList.add(new Line(null, 8, 12));
//        exampleLineList.add(new Line(null, 8, 14));
//        exampleLineList.add(new Line(null, 14, 22));
//        exampleLineList.add(new Line(null, 10, 22));
//
//        example.setLineList(exampleLineList);
        return rootView;
    }

    public void init(){

    }
}
