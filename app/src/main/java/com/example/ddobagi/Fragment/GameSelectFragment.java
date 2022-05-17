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

public class GameSelectFragment extends Fragment {
    TextView starCntTextView;
    RecyclerView recyclerView;

    public GameSelectFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game_select, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}