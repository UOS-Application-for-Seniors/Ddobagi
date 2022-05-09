package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.google.gson.Gson;

public class ListenAndSolveFragment extends GameFragment{
    Button imgBtn;
    TextView inputBox;
    String quizAnswer;
    final int buttonImgBound = 350;

    public ListenAndSolveFragment(){
        isSTTAble = true;
    }

    public void receiveSTTResult(String voice){
        inputBox.setText(voice);
    }

    public int commit(){
        int result = 0;
        String inputText = inputBox.getText().toString();
        inputText.replace(",", "");
        inputText.replace("-", "");
        inputText.replace(" ", "");
        inputText.replace("_", "");
        inputText.replace("/", "");
        inputText.replace(".", "");

        if(inputText.equals(quizAnswer)){
            result = 1;
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
        int i = 0;
        String url = Communication.getQuizDataUrl + gameID + "/" +quizID + "/0.jfif";

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        quizAnswer = quiz.quizanswer;
        imgBtn.setText(quiz.quizdetail);
        setImageOnButton(url, imgBtn, buttonImgBound);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_listen_and_solve, container, false);
        inputBox = rootView.findViewById(R.id.listen_editText);
        imgBtn = rootView.findViewById(R.id.listen_img_btn);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    public void init(){
        imgBtn.setCompoundDrawables(null, null, null, null);
        imgBtn.setText("");
        inputBox.setText("");
    }
}