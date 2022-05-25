package com.example.ddobagi.Fragment;

import android.os.Bundle;
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

public class PlayResultFragment extends Fragment {
    TextView coinSumTextView;
    RecyclerView recyclerView;
    PlayResultAdapter adapter;

    public PlayResultFragment(){

    }

    public void setResult(QuizInfoSummary[] quizList, int[] quizCoin){
        int sum = 0;
        for(int coin: quizCoin){
            sum += coin;
        }
        if(coinSumTextView != null){
            PlayActivity play = (PlayActivity) getActivity();
            coinSumTextView.setText("총 " + play.coinFormat(sum) + "개\n금화 획득!!");
        }

        for(int i = 0; i<quizList.length;i++){
            if(quizCoin[i] == 0){
                continue;
            }
            adapter.addItem(new QuizResult(i+1, quizList[i].gamename, Integer.parseInt(quizList[i].difficulty), quizCoin[i]));
        }
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_play_result, container, false);

        recyclerView = rootView.findViewById(R.id.play_result_recylerView);
        coinSumTextView = rootView.findViewById(R.id.play_result_star_count);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlayResultAdapter();

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        PlayActivity playActivity = (PlayActivity) getActivity();
        setResult(playActivity.getQuizList(), playActivity.getQuizCoin());
    }
}