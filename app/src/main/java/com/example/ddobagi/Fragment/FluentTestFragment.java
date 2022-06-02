package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Activity.PlayActivity;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.DictResult;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FluentTestFragment extends GameFragment{
    TextView quizDetail, inputProgress, timerText;
    EditText inputText;
    String quizAnswer;
    ArrayList<String> curAnswer = new ArrayList<>();
    int correctWordCnt = 0;
    final int maxTime = 70;
    int remainTime = maxTime;

    Timer timer;
    TimerTask task;
    Handler handler = new Handler();

    public FluentTestFragment(){
        isSTTAble = true;
    }

    @Override
    public void setReadyToCommit(boolean readyToCommit) {
        super.setReadyToCommit(readyToCommit);
        if(readyToCommit){
            task = new TimerTask() {
                @Override
                public void run() {
                    remainTime--;
                    if(remainTime < 0){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                PlayActivity play = (PlayActivity) getActivity();
                                play.commitBtn.callOnClick();
                            }
                        });
                        return;
                    }
                    if(remainTime < 60){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                timerText.setText(Integer.toString(remainTime));
                            }
                        });
                    }
                }
            };
            timer.schedule(task, 0,1000);
        }
    }

    public void receiveSTTResult(String voice){
        String[] vAnsShort;
        vAnsShort = voice.split(" ");
        for (int i = 0; i < vAnsShort.length; i++) {
            putNewWord(vAnsShort[i]);
        }
    }

    public int commit(){
        task.cancel();
        if(correctWordCnt >= 15){
            return 1;
        }
        else if(correctWordCnt >= 9){
            return 2;
        }
        else{
            return 0;
        }
    }

    void onHelp(){

    }

    void wordValidation(String word){
        String url = Communication.dictQuizUrl;
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Communication.println("wordValidation 응답 --> " + response);
                        Gson gson = new Gson();

                        DictResult dictResult = gson.fromJson(response, DictResult.class);
                        if(dictResult == null){
                            Log.d("warning","dictResult is null");
                            return;
                        }
                        correctWordCnt += dictResult.score;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Communication.handleVolleyError(error);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("result", word);

                return params;
            }
        };
        request.setShouldCache(false);
        Communication.requestQueue.add(request);
        Communication.println("요청 보냄.");
    }

    public void onGetGameDataResponse(String response){
//        int i = 0;
//        String url = Communication.getQuizDataUrl;
//        String quizdataUrl;

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        quizDetail.setText(detail);
        quizAnswer = quiz.quizanswer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_fluent_test, container, false);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        inputProgress = rootView.findViewById(R.id.fluent_progress);
        inputText = rootView.findViewById(R.id.fluent_editText);
        timerText = rootView.findViewById(R.id.timer_text);

        timer = new Timer();

        Button inputBtn = rootView.findViewById(R.id.fluent_input_btn);
        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = inputText.getText().toString().trim();
                if(str.equals("")){
                    return;
                }
                putNewWord(str);
            }
        });

        return rootView;
    }

    private void putNewWord(String str){
        if(!curAnswer.contains(str)){
            curAnswer.add(str);
            inputProgress.append(str + ", ");
            inputText.setText("");
            wordValidation(str);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    public void init(){
        super.init();
        curAnswer.clear();
        inputProgress.setText("");
        remainTime = maxTime;
        timerText.setText("60");
    }
}
