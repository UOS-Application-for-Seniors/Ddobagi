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

import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.google.gson.Gson;

public class SequenceChoiceFragment extends GameFragment{
    TextView quizDetail;
    int choiceNum = 4;
    TextView[] sequenceView = new TextView[choiceNum];
    Button[] choiceBtn = new Button[choiceNum];
    String quizAnswer;
    int[] curAnswer = new int[4];
    int index = 0;
    final int buttonImgBound = 150;

    public SequenceChoiceFragment(){
        isSTTAble = true;
    }

    public void receiveSTTResult(String voice){
        int vAnsChoice = 0;
        String vResultString = "";
        char[] vResultChar;

        vResultString = voice.toString();
        vResultChar = vResultString.toCharArray();

        for (int i = 0; i < choiceNum; i++){
            curAnswer[i] = -1;
        }
        index = 0;

        for(int i = 0; i < vResultChar.length; i++) {
            switch (vResultChar[i]) {
                case '1' :
                case '일':
                    vAnsChoice = 1;
                    onButtonTouch(vAnsChoice - 1);
                    break;
                case '2' :
                case '이':
                    vAnsChoice = 2;
                    onButtonTouch(vAnsChoice - 1);
                    break;
                case '3' :
                case '삼':
                case '참':
                    vAnsChoice = 3;
                    onButtonTouch(vAnsChoice - 1);
                    break;
                case '4' :
                case '사':
                    vAnsChoice = 4;
                    onButtonTouch(vAnsChoice - 1);
                    break;
            }
        }

        if(vAnsChoice == 0){
            return;
        }
    }

    public int commit(){
        int result = 0;
        String resultStr = "";
        for(int i=0;i<choiceNum;i++) {
            resultStr += sequenceView[i].getText().toString() + ",";
        }
        resultStr = resultStr.substring(0,resultStr.length()-1);
        if(resultStr.equals(quizAnswer)){
            //Toast.makeText(getActivity(), "정답입니다", Toast.LENGTH_LONG).show();
            result = 1;
        }
        else{
            //Toast.makeText(getActivity(), quizAnswer+resultStr, Toast.LENGTH_LONG).show();
            result = 0;
        }
        return result;
    }

    void onHelp(){

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
        quizDetail.setText(detail);
        quizAnswer = quiz.quizanswer;

        String[] splitString = quiz.quizchoicesdetail.split(",");
        String[] quizPicture = quiz.quizchoicespicture.split(",");

        if(splitString.length == choiceNum && quizPicture.length == choiceNum){
            for(;i<choiceNum;i++){
                if(!quizPicture[i].equals(" ")){
                    String tmp = url;
                    tmp = tmp + quizPicture[i] + ".jfif";
                    setImageOnButton(tmp, choiceBtn[i], buttonImgBound, 1);
                }
                else{
                    choiceBtn[i].setTextSize(40);
                }
                choiceBtn[i].setText(splitString[i]);
            }
        }
        else{
            Log.d("multipleChoice", "서버로부터 잘못된 정보가 입력되었습니다");
        }
    }

    private void onButtonTouch(int newAnswer){
        for(int i = 0; i < choiceNum; i++){
            if(curAnswer[i] == newAnswer){
                for(int j = 0; j < choiceNum; j++){
                    curAnswer[j] = -1;
                    sequenceView[j].setText("");
                }
                index = 0;
                return;
            }
        }
        curAnswer[index] = newAnswer;
        sequenceView[index].setText(Integer.toString(newAnswer + 1));
        index++;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sequence_choice, container, false);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        choiceBtn[0] = rootView.findViewById(R.id.sequnce_choice_btn1);
        choiceBtn[1] = rootView.findViewById(R.id.sequnce_choice_btn2);
        choiceBtn[2] = rootView.findViewById(R.id.sequnce_choice_btn3);
        choiceBtn[3] = rootView.findViewById(R.id.sequnce_choice_btn4);
        sequenceView[0] = rootView.findViewById(R.id.sequenceView1);
        sequenceView[1] = rootView.findViewById(R.id.sequenceView2);
        sequenceView[2] = rootView.findViewById(R.id.sequenceView3);
        sequenceView[3] = rootView.findViewById(R.id.sequenceView4);

        index = 0;

        for(int i=0; i<choiceNum; i++){
            final int inmutable_index = i;
            choiceBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonTouch(inmutable_index);
                }
            });
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    public void init(){
        super.init();
        for (int i = 0; i < choiceNum; i++){
            curAnswer[i] = -1;
            choiceBtn[i].setText("");
            sequenceView[i].setText("");
            choiceBtn[i].setCompoundDrawables(null, null, null, null);
            choiceBtn[i].setTextSize(20);
        }
        index = 0;
    }
}
