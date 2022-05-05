package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.R;

import java.util.HashMap;
import java.util.Map;

public class DrawClockFragment extends GameFragment{
    public int commit(){
        int result = 0;
        return result;
    }
    void onHelp(){

    }

    public void loadGame(int gameID, int quizID){

    }

    public void onGetGameDataResponse(String response){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_draw_clock, container, false);
        return rootView;
    }
}
