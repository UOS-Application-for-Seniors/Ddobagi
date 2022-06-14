package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.util.Log;
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
    final int buttonImgBound = 130, exampleImgBound = 150;
    String curAnswer;

    public ChoiceWithPictureFragment(){
        isSTTAble = true;
    }


    public void receiveSTTResult(String voice){

        int vAnsChoice = 0;

//        vResultString = voice;
//        vResultChar = vResultString.toCharArray();
//
//        for(int i = 0; i < vResultChar.length; i++) {
//            switch (vResultChar[i]) {
//                case '1' :
//                    vAnsChoice = 1;
//                    break;
//                case '2' :
//                    vAnsChoice = 2;
//                    break;
//                case '3' :
//                    vAnsChoice = 3;
//                    break;
//                case '4' :
//                    vAnsChoice = 4;
//                    break;
//            }
//        }
//

        String[] vAnsShort = voice.split(" ");
        String[] buttonText = new String[choiceNum];
        if(!choiceBtn[0].getText().toString().equals("")){
            for(int i = 0 ; i<choiceNum; i++){
                buttonText[i] = choiceBtn[i].getText().toString();
            }
            for (int i = 0; i < vAnsShort.length; i++) {
                for(int j =0;j<choiceNum;j++){
                    if(buttonText[j].contains(vAnsShort[i])){
                        vAnsChoice = j + 1;
                        onButtonTouch(Integer.toString(vAnsChoice - 1));
                        return;
                    }
                }
            }
        }


        voice = normalizationString(voice);
        if(voice.contains("1") || voice.contains("일") || voice.contains("하나") || voice.contains("첫")){
            vAnsChoice = 1;
        }
        else if(voice.contains("2") || voice.contains("이번") || voice.contains("둘") || voice.contains("두")){
            vAnsChoice = 2;
        }
        else if(voice.contains("3") || voice.contains("삼") || voice.contains("서이") || voice.contains("산") || voice.contains("세")){
            vAnsChoice = 3;
        }
        else if(voice.contains("4") || voice.contains("사") || voice.contains("넷") || voice.contains("자")){
            vAnsChoice = 4;
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
        String url = Communication.getQuizDataUrl;

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        quizDetail.setText(detail);
        quizAnswer = quiz.quizanswer;

        boolean isChoicesDetail = false;
        //버튼 텍스트 배치
        if(quiz.quizchoicesdetail != null && !quiz.quizchoicesdetail.equals("null") && !quiz.quizchoicesdetail.equals("")){
            isChoicesDetail = true;
            String[] splitString = quiz.quizchoicesdetail.split(",");
            if(splitString.length == choiceNum){
                for(int i=0;i<choiceNum;i++){
                    choiceBtn[i].setText(splitString[i]);
                }
            }
        }

        //버튼 이미지 배치
        String[] quizPicture;
        if(quiz.quizchoicespicture != null && !quiz.quizchoicespicture.equals("") && !quiz.quizchoicespicture.equals("null")){
            quizPicture = quiz.quizchoicespicture.split(",");

            setImageOnImageView(url + quizPicture[0] + ".jfif", imageView, exampleImgBound);

            if(quizPicture.length == choiceNum + 1){
                for(int i=1;i<choiceNum + 1;i++){
                    String tmp = url;
                    tmp += quizPicture[i] + ".jfif";
                    int location = 4;
                    if(isChoicesDetail){
                        location = 1;
                    }
                    setImageOnButton(tmp, choiceBtn[i - 1], buttonImgBound, location);
                }
            }
        }
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
        super.init();
        for(int i =0; i<choiceNum; i++){
            choiceBtn[i].setText("");
            choiceBtn[i].setBackground(getResources().getDrawable(R.drawable.white_btn));
            choiceBtn[i].setPadding(0,0,0,0);
            numBtn[i].setBackground(getResources().getDrawable(R.drawable.num_btn));
            choiceBtn[i].setCompoundDrawables(null, null, null, null);
        }
        imageView.setImageDrawable(null);
        curAnswer = "";
    }
}