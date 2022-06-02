package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.Line;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.example.ddobagi.View.LineConnectionView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class LineConnectionFragment extends GameFragment{
    LineConnectionView lineConnectionView;
    ArrayList<Line> answerLineList;
    TextView quizDetail;
    int choiceNum = 8;
    Button[] choiceBtn = new Button[choiceNum];
    String quizAnswer;
    final int buttonImgBound = 115;

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
        else if(resultCnt > 0){
            result = 2;
        }
        else{
            //Toast.makeText(getActivity(), "정답이 아닙니다", Toast.LENGTH_LONG).show();
            result = 0;
        }
        return result;
    }

    void onHelp(){

    }


    public void onGetGameDataResponse(String response){
        String url = Communication.getQuizDataUrl;

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        quizDetail.setText(detail);
        quizAnswer = quiz.quizanswer;

        String[] quizChoicesDetail = quiz.quizchoicesdetail.split(",");
        String[] quizPicture = quiz.quizchoicespicture.split(",");

        if(quizPicture.length == choiceNum){
            for(int i=0;i<choiceNum;i++){
                if(!quizPicture[i].equals(" ")){
                    String tmp = url;
                    tmp = tmp + quizPicture[i] + ".jfif";
                    setImageOnButton(tmp, choiceBtn[i], buttonImgBound, 0);
                }
                if(!quizChoicesDetail[i].equals(" ")){
                    choiceBtn[i].setText(quizChoicesDetail[i]);
                }
            }
        }
        else{
            Log.d("lineConnection", "서버에서 잘못된 개수의 정보가 도착했습니다.");
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
        super.init();
        for(int i=0;i<choiceNum;i++){
            choiceBtn[i].setText("");
            choiceBtn[i].setCompoundDrawables(null, null, null, null);
            choiceBtn[i].setTextSize(40);
        }
        lineConnectionView.clearLineList();
    }
}
