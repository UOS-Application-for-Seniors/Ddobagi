package com.example.ddobagi.Fragment;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.Line;
import com.example.ddobagi.Class.LoadImage;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.example.ddobagi.View.LineConnectionView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LineConnectionFragment extends GameFragment{
    LineConnectionView lineConnectionView;
    ArrayList<Line> answerLineList;
    TextView quizDetail;
    int choiceNum = 8;
    Button[] choiceBtn = new Button[choiceNum];
    String quizAnswer;

    public void commit(){
        ArrayList<Line> lineList = lineConnectionView.getLineList();

        String[] linePair = quizAnswer.split(",");
        //Log.d("linePair", linePair[0] + linePair[2]);

        int start, end, temp, answerStart, answerEnd, resultCnt = 0;
        for(Line line: lineList){
            start = line.start;
            end = line.end;

            if(start > end){
                temp = start;
                start = end;
                end = temp;
            }

            for(String str: linePair){
                String[] splitedStr = str.split("-");
                answerStart = Integer.parseInt(splitedStr[0]);
                answerEnd = Integer.parseInt(splitedStr[1]);
                if(answerStart == start && answerEnd == end){
                    resultCnt++;
                    break;
                }
            }
        }
        if(resultCnt == linePair.length){
            Toast.makeText(getActivity(), "정답입니다", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), "정답이 아닙니다", Toast.LENGTH_LONG).show();
        }
    }

    void onHelp(){

    }

    void loadGame(){
        getGameData();
    }

    void getGameData(){
        String url = "http://121.164.170.67:3000/quiz/1";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Communication.println("응답 --> " + response);
                        onGetGameDataResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Communication.handleVolleyError(error);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("path", "/file/asd/test.png");
//                params.put("name", "1");
//                params.put("fn", "test.jpg");

                return params;
            }
        };
        request.setShouldCache(false);
        Communication.requestQueue.add(request);
        Communication.println("요청 보냄.");
    }

    public void onGetGameDataResponse(String response){
        String url = "http://121.164.170.67:3000/file/";
        String quizdatapathUrl;

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        if(quiz == null){
            return;
        }
        quizdatapathUrl = url + quiz.quizdatapath + "/";

        quizDetail.setText(quiz.quizdetail);
        quizAnswer = quiz.quizanswer;

        String[] splitString = quiz.quizchoicesdetail.split(",");

        for(int i=0;i<choiceNum;i++){
            String tmp = quizdatapathUrl;
            tmp = tmp + Integer.toString(i) + ".jfif";
            setImage(tmp, choiceBtn[i]);
            choiceBtn[i].setText(splitString[i]);
        }
    }

    private void setImage(String url, Button button){
        LoadImage loadImage = new LoadImage((bitmap) -> {
            Drawable drawable;

            drawable = new BitmapDrawable(bitmap);
            if(drawable != null){
                drawable.setBounds( 0, 0, 120, 120);
                button.setCompoundDrawables(null, drawable, null, null);
            }
        });
        loadImage.execute(url);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_line_connection, container, false);
        lineConnectionView = rootView.findViewById(R.id.line_connection_view);
        quizDetail = rootView.findViewById(R.id.quizDetail);
        choiceBtn[0] = rootView.findViewById(R.id.line_connection_btn1);
        choiceBtn[1] = rootView.findViewById(R.id.line_connection_btn2);
        choiceBtn[2] = rootView.findViewById(R.id.line_connection_btn3);
        choiceBtn[3] = rootView.findViewById(R.id.line_connection_btn4);
        choiceBtn[4] = rootView.findViewById(R.id.line_connection_btn5);
        choiceBtn[5] = rootView.findViewById(R.id.line_connection_btn6);
        choiceBtn[6] = rootView.findViewById(R.id.line_connection_btn7);
        choiceBtn[7] = rootView.findViewById(R.id.line_connection_btn8);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadGame();
    }
}
