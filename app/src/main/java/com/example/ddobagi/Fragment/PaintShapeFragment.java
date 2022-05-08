package com.example.ddobagi.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.example.ddobagi.View.PaintShapeExample;
import com.example.ddobagi.View.PaintShapePractice;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PaintShapeFragment extends GameFragment{
    PaintShapeExample example;
    PaintShapePractice practice;
    TextView quizDetail;
    ArrayList<String> curAnswer = new ArrayList<>();
    String quizChoiceDetail;
    String[] shapeString;

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
        this.gameID = gameID;
        this.quizID = quizID;
        getGameData();
    }

    public void onGetGameDataResponse(String response){
        int i = 0;
        String url = "http://121.164.170.67:3000/file/";
        String quizdataUrl;

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }

        quizTTS = quiz.quizTTS;
        detail = quiz.quizdetail;
        quizDetail.setText(detail);
        quizChoiceDetail = quiz.quizchoicesdetail;
        shapeString = quizChoiceDetail.split("/");

        Communication.println("Hello3");

        for(String shapeInfo:shapeString) {
            String[] shape = shapeInfo.split(",");
            for(String shapeInfo2:shape) {
                System.out.println(shapeInfo2);
            }
            example.addShape(Integer.parseInt(shape[0]), Integer.parseInt(shape[1]), Integer.parseInt(shape[2]), Integer.parseInt(shape[3]), shape[4], shape[5], Integer.parseInt(shape[6]));
        }

        example.setPaintShapePractice(practice);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_paint_shape, container, false);
        example = rootView.findViewById(R.id.paint_shape_example);
        practice = rootView.findViewById(R.id.paint_shape_practice);
        quizDetail = rootView.findViewById(R.id.quizDetail);

        example.setPaintShapePractice(practice);

        //example.addShape(200, 300, 200, 100, "triangle", "yellow", 1);
        //example.addShape(200, 383, 170, 100, "rectangle", "green", 1);
        //example.addShape(100, 100, 50, 0, "circle", "red", 1);
        //example.addShape(160, 373, 40, 40, "rectangle", "blue", 0);
        //example.addShape(240, 373, 40, 40, "rectangle", "blue", 0);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    public void init(){
        curAnswer.clear();
    }

}
