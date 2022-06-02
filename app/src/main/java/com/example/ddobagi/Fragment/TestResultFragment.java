package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ddobagi.Activity.PlayActivity;
import com.example.ddobagi.R;

public class TestResultFragment extends Fragment {
    TextView textView1;
    int orientation = 0; //지남력
    int attention = 0; // 주의력
    int spaceTime = 0; //시공간기능
    int executive = 0; //집행기능
    int memory = 0; //기억력
    int language = 0; //언어기능
    int totalScore = 0;

    public TestResultFragment(){

    }

    public void setResult(int[] score){
        for(int i=0; i<score.length; i++){
            if(0 <= i && i <= 4){
                orientation += score[i];
            }
            else if(7 <= i && i <= 9){
                attention += score[i];
            }
            else if(10 == i){
                spaceTime += score[i];
            }
            else if((11 <= i && i <= 13) || i == 24){
                executive += score[i];
            }
            else if(14 <= i && i <= 19){
                memory += score[i];
            }
            else if(20 <= i && i<= 23){
                language += score[i];
            }
            totalScore += score[i];

            String str = "총점: " + totalScore + "\n지남력: " + orientation + ", 주의력: " + attention + ", 시공간기능: " + spaceTime
                    + "\n집행기능: " + executive + ", 기억력: " + memory + ", 언어기능: " + language;
            textView1.setText(str);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_test_result, container, false);

        textView1 = rootView.findViewById(R.id.test_result_text1);
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