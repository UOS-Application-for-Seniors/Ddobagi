package com.example.ddobagi.Fragment;

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
import com.example.ddobagi.R;
import com.example.ddobagi.View.LineConnectionView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LineConnectionFragment extends GameFragment{
    LineConnectionView lineConnectionView;
    ArrayList<Line> answerLineList;
    TextView quizDetail;
    int choiceNum = 8;
    Button[] choiceBtn = new Button[choiceNum];
    String quizAnswer;
    final int buttonImgBound = 120;

    public LineConnectionFragment(){
        isSTTAble = false;
    }

    public void receiveSTTResult(String voice){

    }

    public int commit(){
        ArrayList<Line> lineList = lineConnectionView.getLineList();
        int result = 0;

        String[] linePair = quizAnswer.split(",");
        //Log.d("linePair", linePair[0] + linePair[2]);

        int start, end, temp, answerStart, answerEnd, resultCnt = 0;
        for(Line line: lineList){
            start = line.start;
            end = line.end;

            if(start > end){
                temp = start;
                start = end;
                end = temp;
            }

            for(String str: linePair){
                String[] splitedStr = str.split("-");
                answerStart = Integer.parseInt(splitedStr[0]);
                answerEnd = Integer.parseInt(splitedStr[1]);
                if(answerStart == start && answerEnd == end){
                    resultCnt++;
                    break;
                }
            }
        }
        if(resultCnt == linePair.length){
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
        String url = "http://121.164.170.67:3000/file/" + gameID + "/" + quizID + "/";

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        quizDetail.setText(detail);
        quizAnswer = quiz.quizanswer;

        String[] splitString = quiz.quizchoicesdetail.split(",");

        for(int i=0;i<choiceNum;i++){
            String tmp = url;
            tmp = tmp + Integer.toString(i) + ".jfif";
            setImageOnButton(tmp, choiceBtn[i], buttonImgBound);
            choiceBtn[i].setText(splitString[i]);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_line_connection, container, false);
        lineConnectionView = rootView.findViewById(R.id.line_connection_view);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        choiceBtn[0] = rootView.findViewById(R.id.line_connection_btn1);
        choiceBtn[1] = rootView.findViewById(R.id.line_connection_btn2);
        choiceBtn[2] = rootView.findViewById(R.id.line_connection_btn3);
        choiceBtn[3] = rootView.findViewById(R.id.line_connection_btn4);
        choiceBtn[4] = rootView.findViewById(R.id.line_connection_btn5);
        choiceBtn[5] = rootView.findViewById(R.id.line_connection_btn6);
        choiceBtn[6] = rootView.findViewById(R.id.line_connection_btn7);
        choiceBtn[7] = rootView.findViewById(R.id.line_connection_btn8);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void init(){

    }
}
