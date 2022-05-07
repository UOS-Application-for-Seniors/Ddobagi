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

import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.google.gson.Gson;

public class MultipleChoiceFragment extends GameFragment{
    TextView quizDetail;
    int choiceNum = 4;
    Button[] choiceBtn = new Button[choiceNum];
    String quizAnswer;
    String curAnswer = "1";
    final int buttonImgBound = 200;

    public int commit(){
        int result = 0;
        if(quizAnswer.equals(curAnswer)){
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
        String url = "http://121.164.170.67:3000/file/" + gameID + "/" +quizID + "/";

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }


        quizDetail.setText(quiz.quizdetail);
        quizAnswer = quiz.quizanswer;

        String[] splitString = quiz.quizchoicesdetail.split(",");

        for(;i<choiceNum;i++){
            final int inmutable_index = i;
            String tmp = url;
            tmp = tmp + Integer.toString(i) + ".jfif";
            setImageOnButton(tmp, choiceBtn[i], buttonImgBound);
            choiceBtn[i].setText(splitString[i]);
//            choiceBtn[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(getActivity(), "정답이 아닙니다", Toast.LENGTH_LONG).show();
//                }
//            });
        }

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mutiple_choice, container, false);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        choiceBtn[0] = rootView.findViewById(R.id.multiple_btn1);
        choiceBtn[1] = rootView.findViewById(R.id.multiple_btn2);
        choiceBtn[2] = rootView.findViewById(R.id.multiple_btn3);
        choiceBtn[3] = rootView.findViewById(R.id.multiple_btn4);

        for(int i=0; i<choiceNum; i++){
            final int inmutable_index = i;
            choiceBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choiceBtn[Integer.parseInt(curAnswer)].setBackground(getResources().getDrawable(R.drawable.light_green_btn));
                    curAnswer = Integer.toString(inmutable_index);
                    choiceBtn[inmutable_index].setBackground(getResources().getDrawable(R.drawable.green_btn));
                }
            });
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
