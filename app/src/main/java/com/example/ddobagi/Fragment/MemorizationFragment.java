package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ddobagi.Activity.PlayActivity;
import com.example.ddobagi.Class.Communication;
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

    public void onGetGameDataResponse(String response){
        int i = 0;
        String url = Communication.getQuizDataUrl;

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        imgBtn.setText(quiz.quizdetail);
        if(quiz.quizchoicespicture != null && !quiz.quizchoicespicture.equals("")){
            setImageOnButton(url + quiz.quizchoicespicture + ".jfif", imgBtn, buttonImgBound, 1);
        }
        else{
            imgBtn.setTextSize(40);
        }
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
        super.init();
        imgBtn.setCompoundDrawables(null, null, null, null);
        imgBtn.setTextSize(30);
        imgBtn.setText("");
    }
}