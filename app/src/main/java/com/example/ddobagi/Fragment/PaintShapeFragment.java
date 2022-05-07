package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ddobagi.R;
import com.example.ddobagi.View.PaintShapeExample;
import com.example.ddobagi.View.PaintShapePractice;

public class PaintShapeFragment extends GameFragment{
    PaintShapeExample example;
    PaintShapePractice practice;

    public PaintShapeFragment(){
        isSTTAble = false;
    }

    public int commit(){
        return example.commit();
    }

    public void receiveSTTResult(String voice){

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_paint_shape, container, false);
        example = rootView.findViewById(R.id.paint_shape_example);
        practice = rootView.findViewById(R.id.paint_shape_practice);


        example.setPaintShapePractice(practice);
        example.addShape(200, 300, 200, 100, "triangle", "yellow", 1);
        example.addShape(200, 383, 170, 100, "rectangle", "green", 1);
        example.addShape(100, 100, 50, 0, "circle", "red", 1);
        example.addShape(160, 373, 40, 40, "rectangle", "blue", 0);
        example.addShape(240, 373, 40, 40, "rectangle", "blue", 0);

        return rootView;
    }
}
