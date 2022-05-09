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

public class TestResultFragment extends Fragment {
    TextView textView;

    public TestResultFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_test_result, container, false);

        textView = rootView.findViewById(R.id.test_result_text);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}