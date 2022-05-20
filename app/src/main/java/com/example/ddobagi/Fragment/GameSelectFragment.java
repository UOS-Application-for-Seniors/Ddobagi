package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddobagi.Activity.PlayActivity;
import com.example.ddobagi.Class.GameInfoSummary;
import com.example.ddobagi.Class.GameSelectAdapter;
import com.example.ddobagi.Class.OnGameItemClickListener;
import com.example.ddobagi.Class.PlayResultAdapter;
import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.Class.QuizResult;
import com.example.ddobagi.R;

public class GameSelectFragment extends Fragment {
    LinearLayout listLayout, difficultLayout;

    RecyclerView recyclerView;

    TextView descriptText, fieldText;
    Button curGameBtn, easyBtn, normalBtn, hardBtn;

    GameInfoSummary curGame;


    public GameSelectFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game_select, container, false);

        setComponent(rootView);

        return rootView;
    }

    private void setComponent(ViewGroup rootView){
        listLayout = rootView.findViewById(R.id.game_select_list_layout);
        difficultLayout = rootView.findViewById(R.id.game_select_difficulty_layout);

        recyclerView = rootView.findViewById(R.id.game_select_recylerView);

        descriptText = rootView.findViewById(R.id.game_select_descript);
        fieldText = rootView.findViewById(R.id.game_select_field);
        curGameBtn = rootView.findViewById(R.id.game_select_cur_game);
        curGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficultLayout.setVisibility(View.INVISIBLE);
                listLayout.setVisibility(View.VISIBLE);
                curGame = null;
            }
        });

        easyBtn = rootView.findViewById(R.id.game_select_easy);
        setDifficultBtn(easyBtn, 0);
        normalBtn = rootView.findViewById(R.id.game_select_normal);
        setDifficultBtn(normalBtn, 1);
        hardBtn = rootView.findViewById(R.id.game_select_hard);
        setDifficultBtn(hardBtn, 2);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        GameSelectAdapter adapter = new GameSelectAdapter();

        for(int i=0;i<30;i++) {
            adapter.addItem(new GameInfoSummary(i, "테스트 게임 " + Integer.toString(i), "테스트 게임의 간략한 설명이 들어갈 곳입니다.", "기억력"));
        }

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnGameItemClickListener() {
            @Override
            public void onItemClick(GameSelectAdapter.ViewHolder holder, View view, int position) {
                GameInfoSummary item = adapter.getItem(position);

                curGame = item;
                curGameBtn.setText(curGame.gamename);
                descriptText.setText(curGame.gamedescript);
                fieldText.setText("효과 영역: " + curGame.field);

                listLayout.setVisibility(View.INVISIBLE);
                difficultLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "아이템 선택됨: " + item.gamename, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setDifficultBtn(Button button, int difficulty){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curGame != null){
                    PlayActivity playActivity = (PlayActivity) getActivity();
                    playActivity.onGameSelect(curGame, difficulty);
                    difficultLayout.setVisibility(View.INVISIBLE);
                    listLayout.setVisibility(View.VISIBLE);
                    curGame = null;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}