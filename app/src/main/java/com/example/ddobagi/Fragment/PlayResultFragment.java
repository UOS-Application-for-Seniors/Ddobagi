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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddobagi.Activity.PlayActivity;
import com.example.ddobagi.Class.PlayResultAdapter;
import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.Class.QuizResult;
import com.example.ddobagi.R;

import java.util.ArrayList;

public class PlayResultFragment extends Fragment {
    TextView starCntTextView;
    RecyclerView recyclerView;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        PlayResultAdapter adapter = new PlayResultAdapter();

        for(int i = 0; i<quizList.length;i++){
            if(quizScore[i] == 3){
                continue;
            }
            adapter.addItem(new QuizResult(i+1, quizList[i].usingfragment, quizScore[i]));
        }
        recyclerView.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_play_result, container, false);

        recyclerView = rootView.findViewById(R.id.play_result_recylerView);
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