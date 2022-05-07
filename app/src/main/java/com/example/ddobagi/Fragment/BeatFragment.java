package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class BeatFragment extends GameFragment{
    TextView quizDetail, inputProgress;
    int choiceNum = 2;
    Button[] choiceBtn = new Button[choiceNum];
    String quizAnswer;
    ArrayList<String> curAnswer = new ArrayList<>();
    int index = 0;
    final int buttonImgBound = 150;

    public BeatFragment(){
        isSTTAble = true;
    }

    public void receiveSTTResult(String voice){
        char[] vResultChar;

        vResultChar = voice.toCharArray();
        final char btn1 = choiceBtn[0].getText().charAt(0);


        for(int i = 0; i < vResultChar.length; i++) {
            if(vResultChar[i] == btn1){
                onButtonTouch(0);
            }
            else{
                onButtonTouch(1);
            }
        }
    }

    public int commit(){
        String curAnswerStr = "";
        for(String str: curAnswer){
            curAnswerStr += str;
        }
        if(curAnswerStr.equals(quizAnswer)){
            return 1;
        }
        return 0;
    }

    void onHelp(){

    }

    public void loadGame(int gameID, int quizID){
        this.gameID = gameID;
        this.quizID = quizID;
        getGameData();
    }

    public void onGetGameDataResponse(String response){
        int i = 0;
        String url = "http://121.164.170.67:3000/file/";
        String quizdataUrl;

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizdataUrl = url + Integer.toString(gameID) + "/" + Integer.toString(quizID) + "/";

        detail = quiz.quizdetail;
        quizDetail.setText(detail);
        quizAnswer = quiz.quizanswer;

        String[] splitString = quiz.quizchoicesdetail.split(",");

        for(;i<choiceNum;i++){
            String tmp = quizdataUrl;
            tmp = tmp + Integer.toString(i) + ".jfif";
            setImageOnButton(tmp, choiceBtn[i], buttonImgBound);
            choiceBtn[i].setText(splitString[i]);
        }
    }

    private void onButtonTouch(int newAnswer){
        String str = choiceBtn[newAnswer].getText().toString();
        curAnswer.add(str);
        inputProgress.append(str + " ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_beat, container, false);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        inputProgress = rootView.findViewById(R.id.beat_Progress);
        choiceBtn[0] = rootView.findViewById(R.id.beat_btn1);
        choiceBtn[1] = rootView.findViewById(R.id.beat_btn2);
        Button resetBtn = rootView.findViewById(R.id.beat_reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curAnswer.clear();
                inputProgress.setText("");
            }
        });

        index = 0;

        for(int i=0; i<choiceNum; i++){
            final int inmutable_index = i;
            choiceBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonTouch(inmutable_index);
                }
            });
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        curAnswer.clear();
        inputProgress.setText("");
    }
}
