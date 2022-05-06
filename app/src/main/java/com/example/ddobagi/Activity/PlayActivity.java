package com.example.ddobagi.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.Fragment.ChoiceWithPictureFragment;
import com.example.ddobagi.Fragment.DrawClockFragment;
import com.example.ddobagi.Fragment.GameFragment;
import com.example.ddobagi.Fragment.MultipleChoiceFragment;
import com.example.ddobagi.Fragment.LineConnectionFragment;
import com.example.ddobagi.Fragment.SequenceChoiceFragment;
import com.example.ddobagi.Fragment.ShortAnswerFragment;
import com.example.ddobagi.Fragment.TraceShapeFragment;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlayActivity extends AppCompatActivity {
    GameFragment curGameFragment;

    MultipleChoiceFragment multipleChoiceFragment;
    ShortAnswerFragment shortAnswerFragment;
    LineConnectionFragment lineConnectionFragment;
    DrawClockFragment drawClockFragment;
    SequenceChoiceFragment sequenceChoiceFragment;
    ChoiceWithPictureFragment choiceWithPictureFragment;
    TraceShapeFragment traceShapeFragment;
    Date date;

    SharedPreferences share;

    QuizInfoSummary[] quizList;
    int[] quizScore;
    int quizIndex = 0;

    int fragmentIndex = 0;
    int fragmentNum = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        multipleChoiceFragment = new MultipleChoiceFragment();
        shortAnswerFragment = new ShortAnswerFragment();
        lineConnectionFragment = new LineConnectionFragment();
        drawClockFragment = new DrawClockFragment();
        sequenceChoiceFragment = new SequenceChoiceFragment();
        choiceWithPictureFragment = new ChoiceWithPictureFragment();
        traceShapeFragment = new TraceShapeFragment();

        setButton();

        Intent intent = getIntent();
        getRecommandationList();
        String type = intent.getStringExtra("type");
        if(type == "recommend"){
            getRecommandationList();
        }
        else if(type == "select"){
            //게임 선택하는 창 띄우기
        }
    }


    private void getRecommandationList(){
        share = getSharedPreferences("PREF", MODE_PRIVATE);
        if((share != null) && (share.contains("Access_token"))){
            Toast.makeText(getApplicationContext(), share.getString("Access_token", ""), Toast.LENGTH_LONG).show();
            date = new Date();
            Communication.println(String.valueOf(date.getTime()) + " " + String.valueOf(share.getLong("Access_token_time", 0)
            ) );
            if(date.getTime() - share.getLong("Access_token_time", 0) > 100000) {
                Communication.refreshToken(getApplicationContext());
                Communication.println("Refreshed");
            }
        }


        StringRequest request = new StringRequest(
                Request.Method.GET,
                Communication.gameListUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Communication.println("응답 --> " + response);
                        onGetRecommandationListResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode==401) {
                            //Communication.refreshToken(getApplicationContext());
                        }
                        else{
                            Communication.handleVolleyError(error);
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + share.getString("Access_token", ""));

                return params;
            }
        };
        request.setShouldCache(false);
        Communication.requestQueue.add(request);




        Communication.println("요청 보냄.");

    }

    public void onGetRecommandationListResponse(String response){
        Gson gson = new Gson();
        quizList = gson.fromJson(response, QuizInfoSummary[].class);
        if(quizList == null){
            Log.d("warning","quizList is null");
            return;
        }
        quizScore = new int[quizList.length];


        loadGame();
        Log.d("gameID", Integer.toString(quizList[0].gameid));
    }

    private void loadGame(){
        String usingFragment = quizList[quizIndex].usingfragment;
        switch(usingFragment){
            case "choiceWithPicture":
                curGameFragment = choiceWithPictureFragment;
                break;
            case "multipleChoice":
                curGameFragment = multipleChoiceFragment;
                break;
            case "lineConnection":
                curGameFragment = lineConnectionFragment;
                break;
            case "sequenceChoice":
                curGameFragment = sequenceChoiceFragment;
                break;
            case "shortAnswer":
                curGameFragment = shortAnswerFragment;
                break;
            default:
                curGameFragment = null;
        }
        if(curGameFragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, curGameFragment).commit();
            curGameFragment.loadGame(quizList[quizIndex].gameid, 0);
            Log.d("gameID", Integer.toString(quizList[quizIndex].gameid));
        }
    }

    private void setButton(){
        Button commitBtn = findViewById(R.id.commit_btn);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curGameFragment != null){
                    int result = curGameFragment.commit();
                    quizScore[quizIndex] = result;
                    quizIndex++;
                    if(quizIndex < quizList.length){
                        loadGame();
                    }
                    else{
                        //결과 출력 및 서버로 결과 보내기
                        for(int i=0;i<quizScore.length; i++){
                            Log.d("quizScore", Integer.toString(i) + ": " + Integer.toString(quizScore[i]));
                        }
                        curGameFragment = null;
                    }
                }
            }
        });
        Button exitBtn = findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*Button leftBtn = findViewById(R.id.left_btn);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentIndex--;
                if(fragmentIndex < 0){
                    fragmentIndex += fragmentNum;
                }
                fragmentChange();
            }
        });

        Button rightBtn = findViewById(R.id.right_btn);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentIndex = (fragmentIndex + 1) % fragmentNum;
                fragmentChange();
            }
        });

        fragmentChange();*/
    }

    private void fragmentChange(){
        switch(fragmentIndex){
            case 0:
                curGameFragment = multipleChoiceFragment;
                break;
            case 1:
                curGameFragment = shortAnswerFragment;
                break;
            case 2:
                curGameFragment = lineConnectionFragment;
                break;
            case 3:
                curGameFragment = drawClockFragment;
                break;
            case 4:
                curGameFragment = sequenceChoiceFragment;
                break;
            case 5:
                curGameFragment = choiceWithPictureFragment;
                break;
            case 6:
                curGameFragment = traceShapeFragment;
                break;
            default:
                curGameFragment = multipleChoiceFragment;
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, curGameFragment).commit();
    }
}