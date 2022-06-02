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

import com.example.ddobagi.Activity.PlayActivity;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.google.gson.Gson;

public class CIST10Fragment extends GameFragment{
    TextView quizDetail;
    Button imgBtn;
    EditText editText;
    String[] quizAnswer;
    final int buttonImgBound = 350;

    PlayActivity activity;

    public CIST10Fragment(){
        isSTTAble = true;
    }

    public void receiveSTTResult(String voice){
        editText.setText(voice);
    }

    public int commit(){
        int result = 0, strIndex = 0;
        String inputText = editText.getText().toString();
        for(String str: quizAnswer){
            strIndex++;
            if(inputText.contains(str)){
                activity.skipNextList(strIndex, 2);
                result = 1;
            }
        }
        return result;
    }

    void onHelp(){

    }

    public void onGetGameDataResponse(String response){
        int i = 0;
        String url = Communication.getQuizDataUrl + gameID + "/" +quizID + "/0.jfif";

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        quizDetail.setText(detail);
        String tmpAnswer = quiz.quizanswer;
        quizAnswer = tmpAnswer.split(",");

        //setImageOnButton(url, imgBtn, buttonImgBound);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_listen_and_solve, container, false);
        quizDetail = rootView.findViewById(R.id.listen_img_btn);
        editText = rootView.findViewById(R.id.listen_editText);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    public void init(){
        super.init();
        editText.setText("");
        activity = (PlayActivity) getActivity();
    }
}