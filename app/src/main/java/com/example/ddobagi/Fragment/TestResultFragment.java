package com.example.ddobagi.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Activity.PlayActivity;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class TestResultFragment extends Fragment {
    TextView textViewUp, textViewDown;
    final int ORIENTATION = 0; //지남력
    final int ATTENTION = 1; // 주의력
    final int SPACETIME = 2; //시공간기능
    final int EXECUTIVE = 3; //집행기능
    final int MEMORY = 4; //기억력
    final int LANGUAGE = 5; //언어기능

    int[] fieldScore = new int[6];
    int[] maxFieldScore = new int[6];
    String[] fieldStr = new String[6];
    int totalScore = 0;
    SharedPreferences share;

    public TestResultFragment(){
        maxFieldScore[0] = 5;
        maxFieldScore[1] = 3;
        maxFieldScore[2] = 2;
        maxFieldScore[3] = 6;
        maxFieldScore[4] = 10;
        maxFieldScore[5] = 4;

        fieldStr[0] = "지남력";
        fieldStr[1] = "주의력";
        fieldStr[2] = "시공간기능";
        fieldStr[3] = "집행기능";
        fieldStr[4] = "기억력";
        fieldStr[5] = "언어기능";
    }

    public void setResult(int[] score){
        dementiaDiagnosis();

        for(int i=0; i<score.length; i++){
            if(0 <= i && i <= 4){
                fieldScore[ORIENTATION] += score[i];
            }
            else if(7 <= i && i <= 9){
                fieldScore[ATTENTION] += score[i];
            }
            else if(10 == i){
                fieldScore[SPACETIME] += score[i];
            }
            else if((11 <= i && i <= 13) || i == 24){
                fieldScore[EXECUTIVE] += score[i];
            }
            else if(14 <= i && i <= 19){
                fieldScore[MEMORY] += score[i];
            }
            else if(20 <= i && i<= 23){
                fieldScore[LANGUAGE] += score[i];
            }
            totalScore += score[i];
        }

        float minFirstScore = 99, minSecondScore = 99;
        int minFirstIndex = -1, minSecondIndex = -1;

        for(int i = 0; i<fieldScore.length; i++){
            float tmpNum = fieldScore[i] / maxFieldScore[i];
            if(minFirstScore > tmpNum){
                minFirstScore = tmpNum;
                minFirstIndex = i;
            }
        }

        for(int i=0; i<fieldScore.length; i++){
            if(minFirstIndex == i){
                continue;
            }

            float tmpNum = fieldScore[i] / maxFieldScore[i];
            if(minSecondScore > tmpNum){
                minSecondScore = tmpNum;
                minSecondIndex = i;
            }
        }

        textViewDown.setText("다른 영역보다 " + fieldStr[minFirstIndex] + ", " + fieldStr[minSecondIndex] + "이\n부족한 것 같습니다.\n" +
                "앞으로 놀이를 즐기실 때\n부족한 영역을 우선해서 추천해드리겠습니다!");
    }

    private void onDementiaDiagnosisResponse(String response){
        Gson gson = new Gson();
        Dementia dementia = gson.fromJson(response, Dementia.class);


        if(dementia == null){
            textViewUp.setText("서버로부터 정보를 불러오지 못했습니다");
            textViewDown.setVisibility(View.GONE);
        }

        if(dementia.isDemensia){
            textViewUp.setTextSize(25);
            textViewUp.setText("인지기능이 다른 분들에 비해서 조금 낮게 나오셨습니다.\n점수가 낮다고 해서 반드시 치매가 있는 것은 아니며,\n" +
                    "인지기능은 우울이나 건강상의 다양한 원인에 의해서\n점수가 낮아질 수 있습니다.\n\n" +
                    "먼저 또바기 놀이와 검사를 지속적으로 해보시고\n그럼에도 점수가 계속 낮게 나온다면 추가적인 진단 검사가 필요합니다.");
            textViewDown.setVisibility(View.GONE);
        }
        else{
            textViewUp.setText("인지기능이 비교적 잘 유지되고 있으세요.\n치매를 예방하기 위해서는 지속적으로\n또바기와 함께하는 것이 중요합니다.");
        }
    }

    private class Dementia{
        public boolean isDemensia;
    }

    private void dementiaDiagnosis(){
        String url;
        url = Communication.sendTestResultUrl;
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Communication.println("치매 진단 결과: " + response);
                        onDementiaDiagnosisResponse(response);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + share.getString("Access_token", ""));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gameID", "54");
                params.put("score", "0");

                return params;
            }
        };
        request.setShouldCache(false);
        Communication.requestQueue.add(request);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_test_result, container, false);
        share = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);

        textViewUp = rootView.findViewById(R.id.test_result_text_up);
        textViewDown = rootView.findViewById(R.id.test_result_text_down);
        Button startRecommend = rootView.findViewById(R.id.recommend_play_btn);
        startRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayActivity play = (PlayActivity) getActivity();
                play.startRecommendQuiz();
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        PlayActivity play = (PlayActivity) getActivity();
        setResult(play.getQuizScore());
    }
}