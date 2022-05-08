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

public class MemorizationFragment extends GameFragment{
    Button imgBtn;
    final int buttonImgBound = 350;

    public MemorizationFragment(){
        isSTTAble = false;
    }

    public void receiveSTTResult(String voice){
    }

    public int commit(){
        return 3;
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
        String url = "http://121.164.170.67:3000/file/" + gameID + "/" +quizID + "/0.jfif";

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        imgBtn.setText(quiz.quizdetail);
        setImageOnButton(url, imgBtn, buttonImgBound);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_memorization, container, false);
        imgBtn = rootView.findViewById(R.id.memorization_img_btn);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //init();
    }

    public void init(){
        imgBtn.setCompoundDrawables(null, null, null, null);
        imgBtn.setText("");
    }
}