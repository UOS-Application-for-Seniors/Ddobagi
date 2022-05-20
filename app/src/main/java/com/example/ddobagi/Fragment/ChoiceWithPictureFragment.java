package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.google.gson.Gson;

public class ChoiceWithPictureFragment extends GameFragment {
    TextView quizDetail;
    ImageView imageView;
    int choiceNum = 4;
    Button[] choiceBtn = new Button[choiceNum];
    Button[] numBtn = new Button[choiceNum];
    String quizAnswer;
    final int buttonImgBound = 120, exampleImgBound = 150;
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

    public void onGetGameDataResponse(String response){
        int i = 0;
        String url = Communication.getQuizDataUrl + Integer.toString(gameID) + "/" + Integer.toString(quizID) + "/";

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
            setImageOnButton(tmp, choiceBtn[i], buttonImgBound, 4);
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

        numBtn[0] = rootView.findViewById(R.id.num1);
        numBtn[1] = rootView.findViewById(R.id.num2);
        numBtn[2] = rootView.findViewById(R.id.num3);
        numBtn[3] = rootView.findViewById(R.id.num4);

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
            int curNum = Integer.parseInt(curAnswer);
            Button curSelectButton = choiceBtn[curNum];
            curSelectButton.setBackground(getResources().getDrawable(R.drawable.white_btn));
            numBtn[curNum].setBackground(getResources().getDrawable(R.drawable.num_btn));
        }

        int newNum = Integer.parseInt(newAnswer);
        Button newTouchButton = choiceBtn[newNum];
        this.curAnswer = newAnswer;
        newTouchButton.setBackground(getResources().getDrawable(R.drawable.selected_btn));
        numBtn[newNum].setBackground(getResources().getDrawable(R.drawable.selected_num_btn));
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    public void init(){
        quizTTS = "";
        for(int i =0; i<choiceNum; i++){
            choiceBtn[i].setText("");
            choiceBtn[i].setBackground(getResources().getDrawable(R.drawable.white_btn));
            numBtn[i].setBackground(getResources().getDrawable(R.drawable.num_btn));
            choiceBtn[i].setCompoundDrawables(null, null, null, null);
        }
        imageView.setImageDrawable(null);
        curAnswer = "";
    }
}