package com.example.ddobagi.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ddobagi.R;

public class MultipleChoiceFragment extends GameFragment{
    ProgressDialog dialog;

    void commit(){

    }

    void onHelp(){

    }

    void loadGame(){
        /*dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터를 확인하는 중입니다.");
        dialog.show();*/
    }

    void getGameData(){

    }

    void onGetGameDataResponse(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mutiple_choice, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadGame();
    }
}
