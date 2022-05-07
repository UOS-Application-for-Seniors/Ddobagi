package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FluentTestFragment extends GameFragment{
    TextView quizDetail, inputProgress;
    int choiceNum = 2;
    EditText inputText;
    String quizAnswer;
    ArrayList<String> curAnswer = new ArrayList<>();
    int index = 0;
    final int buttonImgBound = 150;

    public FluentTestFragment(){
        isSTTAble = true;
    }

    public void receiveSTTResult(String voice){
        String[] vAnsShort;
        vAnsShort = voice.split(" ");
        for (int i = 0; i < vAnsShort.length; i++) {
            curAnswer.add(vAnsShort[i]);
            inputProgress.append(vAnsShort[i] + ", ");
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

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        quizDetail.setText(detail);
        quizAnswer = quiz.quizanswer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_fluent_test, container, false);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        inputProgress = rootView.findViewById(R.id.fluent_progress);
        inputText = rootView.findViewById(R.id.fluent_editText);

        Button inputBtn = rootView.findViewById(R.id.fluent_input_btn);
        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = inputText.getText().toString();
                if(str.equals("")){
                    return;
                }
                curAnswer.add(str);
                inputProgress.append(str + ", ");
                inputText.setText("");
            }
        });

        index = 0;

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    public void init(){
        curAnswer.clear();
        inputProgress.setText("");
    }
}
