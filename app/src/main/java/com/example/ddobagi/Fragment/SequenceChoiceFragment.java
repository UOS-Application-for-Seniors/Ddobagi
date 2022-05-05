package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

public class SequenceChoiceFragment extends GameFragment{

    TextView quizDetail;
    int choiceNum = 4;
    TextView[] sequenceView = new TextView[choiceNum];
    Button[] choiceBtn = new Button[choiceNum];
    String quizAnswer;
    int array = 0;
    final int buttonImgBound = 150;

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

    public void loadGame(int gameID, int quizID){
        this.gameID = gameID;
        this.quizID = quizID;
        getGameData();
    }

    public void onGetGameDataResponse(String response){
        int i = 0;
        String url = "http://121.164.170.67:3000/file/";
        String quizdataUrl;

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizdataUrl = url + Integer.toString(gameID) + "/" + Integer.toString(quizID) + "/";

        quizDetail.setText(quiz.quizdetail);
        quizAnswer = quiz.quizanswer;

        String[] splitString = quiz.quizchoicesdetail.split(",");

        for(;i<choiceNum;i++){
            final int inmutable_index = i;
            String tmp = quizdataUrl;
            tmp = tmp + Integer.toString(i) + ".jfif";
            setImageOnButton(tmp, choiceBtn[i], buttonImgBound);
            choiceBtn[i].setText(splitString[i]);
            choiceBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tmp = choiceBtn[inmutable_index].getText().toString().substring(0,1);
                    sequenceView[array].setText(tmp);
                    array++;
                    if(array>3) {
                        array = 0;
                    }
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sequence_choice, container, false);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        choiceBtn[0] = rootView.findViewById(R.id.choice_with_pic_select_btn1);
        choiceBtn[1] = rootView.findViewById(R.id.choice_with_pic_select_btn2);
        choiceBtn[2] = rootView.findViewById(R.id.choice_with_pic_select_Btn3);
        choiceBtn[3] = rootView.findViewById(R.id.choice_with_pic_select_Btn4);
        sequenceView[0] = rootView.findViewById(R.id.sequenceView1);
        sequenceView[1] = rootView.findViewById(R.id.sequenceView2);
        sequenceView[2] = rootView.findViewById(R.id.sequenceView3);
        sequenceView[3] = rootView.findViewById(R.id.sequenceView4);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
