package com.example.ddobagi.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.google.gson.Gson;

public class ShortAnswerFragment extends GameFragment{
    TextView quizDetail;
    ImageView imgView;
    EditText editText;
    String[] quizAnswer;
    String[] quizPicture;
    final int buttonImgBound = 320;

    public ShortAnswerFragment(){
        isSTTAble = true;
    }

    public void receiveSTTResult(String voice){
        if(voice.length() == 1){
            String[] numStr = new String[10];
            numStr[0] = "영공";
            numStr[1] = "일";
            numStr[2] = "이";
            numStr[3] = "삼참";
            numStr[4] = "사넷가자";
            numStr[5] = "오";
            numStr[6] = "육";
            numStr[7] = "칠";
            numStr[8] = "팔칼";
            numStr[9] = "구국";

            for(int i= 0; i<10; i++){
                if(numStr[i].contains(voice)){
                     voice = Integer.toString(i);
                     break;
                }
            }
        }
        editText.setText(voice);
    }

    public int commit(){
        if(quizAnswer == null){
            return 0;
        }
        int result = 0;
        String inputText = editText.getText().toString();
        inputText = normalizationString(inputText);

        if(gameID == 31){
            if(inputText.contains("정월")){
                inputText = "1";
            }
        }
        else if(gameID == 32){
            if(inputText.contains("초하루")){
                inputText = "1";
            }
        }

        for(String str: quizAnswer){
            if(inputText.contains(str)){
                result = 1;
                break;
            }
        }
        return result;
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
        quizPicture = quiz.quizchoicespicture.split(",");
        String tmpAnswer = quiz.quizanswer;
        if(gameID == 34){
            Activity activity = getActivity();
            SharedPreferences share = activity.getSharedPreferences("PREF", activity.MODE_PRIVATE);
            quizAnswer = new String[1];
            quizAnswer[0] = share.getString("address", "").replace(",", "");
            Log.d("address", quizAnswer[0]);
        }
        else{
            quizAnswer = tmpAnswer.split(",");
        }

        setImageOnImageView(url + quizPicture[0] + ".jfif", imgView, buttonImgBound);

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
        imgView = rootView.findViewById(R.id.short_answer_img);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    public void init(){
        super.init();
        imgView.setImageDrawable(null);
        editText.setText("");
    }
}