package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ChoiceWithPictureFragment extends GameFragment {
    TextView quizDetail;
    ImageView imageView;
    int choiceNum = 4;
    Button[] choiceBtn = new Button[choiceNum];
    String quizAnswer;
    final int buttonImgBound = 130, exampleImgBound = 150;
    String curAnswer;

    public ChoiceWithPictureFragment(){
        isSTTAble = true;
    }


    public void receiveSTTResult(String voice){
        int vAnsChoice = 0;
        String vResultString = "";
        char[] vResultChar;

        vResultString = voice.toString();
        vResultChar = vResultString.toCharArray();

        for(int i = 0; i < vResultChar.length; i++) {
            switch (vResultChar[i]) {
                case '1' :
                    vAnsChoice = 1;
                    break;
                case '2' :
                    vAnsChoice = 2;
                    break;
                case '3' :
                    vAnsChoice = 3;
                    break;
                case '4' :
                    vAnsChoice = 4;
                    break;
            }
        }

        if(vAnsChoice == 0){
            return;
        }

        onButtonTouch(Integer.toString(vAnsChoice - 1));
    }


    public int commit(){
        int result = 0;
        if(quizAnswer != null){
            if(quizAnswer.equals(curAnswer)){
                result = 1;
            }
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
        String url = "http://121.164.170.67:3000/file/" + Integer.toString(gameID) + "/" + Integer.toString(quizID) + "/";

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        quizDetail.setText(detail);
        quizAnswer = quiz.quizanswer;


        //String[] splitString = quiz.quizchoicesdetail.split(",");

        for(;i<choiceNum;i++){
            final int inmutable_index = i;
            String tmp = url;
            tmp = tmp + Integer.toString(i) + ".jfif";
            setImageOnButton(tmp, choiceBtn[i], buttonImgBound);
            // choiceBtn[i].setText(splitString[i]);
        }

        // Setup Image on Image View
        String tmp = url;
        tmp = tmp + Integer.toString(i) + ".jfif";
        setImageOnImageView(tmp, imageView, exampleImgBound);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_choice_with_picture, container, false);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        imageView = rootView.findViewById(R.id.imageView);
        choiceBtn[0] = rootView.findViewById(R.id.choice_with_pic_select_btn1);
        choiceBtn[1] = rootView.findViewById(R.id.choice_with_pic_select_btn2);
        choiceBtn[2] = rootView.findViewById(R.id.choice_with_pic_select_Btn3);
        choiceBtn[3] = rootView.findViewById(R.id.choice_with_pic_select_Btn4);

        for(int i=0; i<choiceNum; i++){
            final int inmutable_index = i;
            choiceBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonTouch(Integer.toString(inmutable_index));
                    /*choiceBtn[Integer.parseInt(curAnswer)].setBackground(getResources().getDrawable(R.drawable.light_green_btn));
                    curAnswer = Integer.toString(inmutable_index);
                    choiceBtn[inmutable_index].setBackground(getResources().getDrawable(R.drawable.green_btn));*/
                }
            });
        }
        return rootView;
    }

    private void onButtonTouch(String newAnswer){
        if(curAnswer != ""){
            Button curSelectButton = choiceBtn[Integer.parseInt(curAnswer)];
            curSelectButton.setBackground(getResources().getDrawable(R.drawable.light_green_btn));
        }

        Button newTouchButton = choiceBtn[Integer.parseInt(newAnswer)];
        this.curAnswer = newAnswer;
        newTouchButton.setBackground(getResources().getDrawable(R.drawable.green_btn));
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    public void init(){
        for(int i =0; i<choiceNum; i++){
            choiceBtn[i].setText("");
            choiceBtn[i].setCompoundDrawables(null, null, null, null);
        }
        curAnswer = "";
    }
}
