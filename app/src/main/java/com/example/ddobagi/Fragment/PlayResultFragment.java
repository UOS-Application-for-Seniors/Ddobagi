package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ddobagi.Activity.PlayActivity;
import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.R;

public class PlayResultFragment extends Fragment {
    TextView starCntTextView;

    public PlayResultFragment(){

    }

    public void setResult(QuizInfoSummary[] quizList, int[] quizScore){
        int count = 0;
        for(int score: quizScore){
            switch (score){
                case 0:
                    count++;
                    break;
                case 1:
                    count += 3;
                    break;
                case 2:
                    count += 2;
                    break;
                default:
                    break;
            }
        }
        Log.d("별 개수", Integer.toString(count));
        if(starCntTextView != null){
            starCntTextView.setText("X " + Integer.toString(count) + "개 획득!!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_play_result, container, false);

        starCntTextView = rootView.findViewById(R.id.play_result_star_count);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        PlayActivity playActivity = (PlayActivity) getActivity();
        setResult(playActivity.getQuizList(), playActivity.getQuizScore());
    }
}