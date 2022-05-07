package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.util.Log;
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

public class ShortAnswerFragment extends GameFragment{
    TextView quizDetail;
    Button imgBtn;
    EditText editText;
    String quizAnswer;
    final int buttonImgBound = 350;

    public ShortAnswerFragment(){
        isSTTAble = true;
    }

    public void receiveSTTResult(String voice){
        editText.setText(voice);
    }

    public int commit(){
        int result = 0;

        if(editText.getText().toString().equals(quizAnswer)){
            result = 1;
        }
        Log.d("commit", Integer.toString(result));
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
        String url = "http://121.164.170.67:3000/file/" + gameID + "/" +quizID + "/0.jfif";

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        detail = quiz.quizdetail;
        quizDetail.setText(detail);
        quizAnswer = quiz.quizanswer;

        setImageOnButton(url, imgBtn, buttonImgBound);

//        choiceBtn[Integer.parseInt(quizAnswer)].setOnClickListener((new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "정답입니다", Toast.LENGTH_LONG).show();
//            }
//        }));

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_short_answer, container, false);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        editText = rootView.findViewById(R.id.editTextTextPersonName);
        imgBtn = rootView.findViewById(R.id.short_answer_img_button);
        return rootView;
    }
}
