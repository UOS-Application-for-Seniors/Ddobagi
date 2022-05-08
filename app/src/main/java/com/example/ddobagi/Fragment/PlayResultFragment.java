package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.R;

public class PlayResultFragment extends Fragment {
    public PlayResultFragment(){

    }

    public void setResult(QuizInfoSummary[] quizList, int[] quizScore){


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_draw_clock, container, false);
        return rootView;
    }
}
