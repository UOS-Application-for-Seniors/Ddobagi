package com.example.ddobagi.Activity;

import static android.speech.tts.TextToSpeech.ERROR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.Fragment.BeatFragment;
import com.example.ddobagi.Fragment.ChoiceWithPictureFragment;
import com.example.ddobagi.Fragment.DrawClockFragment;
import com.example.ddobagi.Fragment.FluentTestFragment;
import com.example.ddobagi.Fragment.GameFragment;
import com.example.ddobagi.Fragment.ListenAndSolveFragment;
import com.example.ddobagi.Fragment.MemorizationFragment;
import com.example.ddobagi.Fragment.MultipleChoiceFragment;
import com.example.ddobagi.Fragment.LineConnectionFragment;
import com.example.ddobagi.Fragment.PaintShapeFragment;
import com.example.ddobagi.Fragment.PlayResultFragment;
import com.example.ddobagi.Fragment.SequenceChoiceFragment;
import com.example.ddobagi.Fragment.ShortAnswerFragment;
import com.example.ddobagi.Fragment.TraceShapeFragment;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.Date;

public class PlayActivity extends AppCompatActivity {
    GameFragment curGameFragment;

    MultipleChoiceFragment multipleChoiceFragment;
    ShortAnswerFragment shortAnswerFragment;
    LineConnectionFragment lineConnectionFragment;
    DrawClockFragment drawClockFragment;
    SequenceChoiceFragment sequenceChoiceFragment;
    ChoiceWithPictureFragment choiceWithPictureFragment;
    TraceShapeFragment traceShapeFragment;
    PaintShapeFragment paintShapeFragment;
    BeatFragment beatFragment;
    FluentTestFragment fluentTestFragment;
    ListenAndSolveFragment listenAndSolveFragment;
    MemorizationFragment memorizationFragment;

    PlayResultFragment playResultFragment;

    Date date;

    SharedPreferences share;

    QuizInfoSummary[] quizList;
    int[] quizScore;
    int quizIndex = 0;

    int fragmentIndex = 0;
    int fragmentNum = 12;

    Intent sttIntent;
    SpeechRecognizer mRecognizer;
    Button sttBtn;
    TextView sttResultView;

    Vector<Integer> vAnsChoice = new Vector<Integer>();
    String[] vAnsShort = null;
    String vResultString = "";
    char[] vResultChar;
    Button sttSubmit;

    TextToSpeech tts;
    Button ttsBtn;

    ConstraintLayout resultPerQuizLayout;
    Button resultPerQuizBtn, leftStar, rightStar, centerStar;

    boolean isTest;

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
        paintShapeFragment = new PaintShapeFragment();
        beatFragment = new BeatFragment();
        fluentTestFragment = new FluentTestFragment();
        listenAndSolveFragment = new ListenAndSolveFragment();
        memorizationFragment = new MemorizationFragment();

        playResultFragment = new PlayResultFragment();

        setComponent();

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        if(type.equals("select")){
            //게임 선택하는 창 띄우기
        }
        else{
            getSuggestionQuizList(type);
        }

        if(type.equals("test")){
            isTest = true;
        }

        setComponent();
        fragmentChange();
    }

    private void getSuggestionQuizList(String type){
        String url;
        if(type.equals("recommend")){
            url = Communication.gameListUrl;
        }
        else if (type.equals("test")){
            url = Communication.testListUrl;
        }
        else{
            return;
        }

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
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Communication.println("응답 --> " + response);
                        onGetQuizListResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(!String.valueOf(error).equals("com.android.volley.TimeoutError")){
                            if(error.networkResponse.statusCode==401) {
                                Communication.refreshToken(getApplicationContext());
                            }
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
        Communication.println("gameList 요청 보냄");
    }

    public void onGetQuizListResponse(String response){
        Gson gson = new Gson();
        quizList = gson.fromJson(response, QuizInfoSummary[].class);
        if(quizList == null){
            Log.d("warning","quizList is null");
            return;
        }
        quizScore = new int[quizList.length];

        loadGame();
    }

    void sendGameScore(int gameID, int score){
        String url = "http://121.164.170.67:3000";
        if(isTest){
            url += "/quiz/CISTADDResult";
        }
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Communication.println("응답 --> " + response);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + share.getString("Access_token", ""));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gameID", Integer.toString(gameID));
                params.put("score", Integer.toString(score));
                return params;
            }
        };
        request.setShouldCache(false);
        Communication.requestQueue.add(request);
        Communication.println("gameScore 전송--> " + Integer.toString(gameID) + " : " + Integer.toString(score));
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
            case "traceShape":
                curGameFragment = traceShapeFragment;
                break;
            case "beat":
                curGameFragment = beatFragment;
                break;
            case "fluentTest":
                curGameFragment = fluentTestFragment;
                break;
            case "listenAndSolve":
                curGameFragment = listenAndSolveFragment;
                break;
            case "memorization":
                curGameFragment = memorizationFragment;
                break;
            default:
                curGameFragment = null;
        }
        if(curGameFragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, curGameFragment).commit();
            curGameFragment.loadGame(quizList[quizIndex].gameid, 0);
            if(curGameFragment.isSTTAble()){
                sttBtn.setVisibility(View.VISIBLE);
            }
            else{
                sttBtn.setVisibility(View.INVISIBLE);
            }
            //Log.d("gameID", Integer.toString(quizList[quizIndex].gameid));
        }
    }

    private void setComponent(){
        Button commitBtn = findViewById(R.id.commit_btn);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curGameFragment != null){
                    int result = curGameFragment.commit();
                    Log.d("score", Integer.toString(quizIndex) + ": " + Integer.toString(result));
                    curGameFragment.init();

                    if(!isTest){
                        resultPerQuizControl(result);
                    }

                    quizScore[quizIndex] = result;
                    sendGameScore(quizList[quizIndex].gameid, result);
                    quizIndex++;
                    if(quizIndex < quizList.length){
                        loadGame();
                    }
                    else{
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, playResultFragment).commit();
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


        resultPerQuizLayout = findViewById(R.id.result_per_quiz_layout);
        resultPerQuizBtn = findViewById(R.id.result_per_quiz_btn);
        resultPerQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultPerQuizControl(-1);
            }
        });

        leftStar = findViewById(R.id.small_star_left);
        rightStar = findViewById(R.id.small_star_right);
        centerStar = findViewById(R.id.small_star_center);
        leftStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultPerQuizControl(-1);
            }
        });
        rightStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultPerQuizControl(-1);
            }
        });
        centerStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultPerQuizControl(-1);
            }
        });


        //=============================음성인식=============================
        sttResultView = findViewById(R.id.sttResult);
        sttBtn = (Button) findViewById(R.id.sttStart);
        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        sttBtn.setOnClickListener(v -> {
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(sttIntent);
        });

        //음성인식 답안 제출
        sttSubmit = (Button) findViewById(R.id.sttSubmit);

        ttsBtn = (Button) findViewById(R.id.ttsStart);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        ttsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence ttsText = curGameFragment.getQuizTTS();
                tts.setPitch(1.0f);         // 음성 톤 설정 (n배)
                tts.setSpeechRate(1.0f);    // 읽는 속도 설정 (n배)
                tts.speak(ttsText.toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        //=================================================================
    }

    private void resultPerQuizControl(int result){
        switch (result){
            case -1: //결과창 끄기
                leftStar.setVisibility(View.INVISIBLE);
                rightStar.setVisibility(View.INVISIBLE);
                centerStar.setVisibility(View.INVISIBLE);
                resultPerQuizBtn.setText("");
                resultPerQuizLayout.setVisibility(View.INVISIBLE);
                break;
            case 0: //오답시 중앙 별만 출력
                centerStar.setVisibility(View.VISIBLE);
                resultPerQuizBtn.setText("잘하고 계십니다");
                resultPerQuizLayout.setVisibility(View.VISIBLE);
                break;
            case 1: //정답시 모든 별 출력
                leftStar.setVisibility(View.VISIBLE);
                rightStar.setVisibility(View.VISIBLE);
                centerStar.setVisibility(View.VISIBLE);
                resultPerQuizBtn.setText("완벽합니다!");
                resultPerQuizLayout.setVisibility(View.VISIBLE);
                break;
            case 2: //부분 정답시 왼쪽, 오른쪽 별만 출력
                leftStar.setVisibility(View.VISIBLE);
                rightStar.setVisibility(View.VISIBLE);
                resultPerQuizBtn.setText("훌륭하세요");
                resultPerQuizLayout.setVisibility(View.VISIBLE);
                break;
            case 3: //MemorizationFragment에서 호출. 별이 따로 없음
                break;
            default:
                break;
        }
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

    //=============================음성인식 listener=============================
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트워크 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "말하는 시간 초과";
                    break;
                default:
                    message = "알 수 없는 오류";
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < matches.size(); i++) {
                Log.e("MainActivity" ,"" + matches.get(i));
                sttResultView.setText(matches.get(i));
            }

            //matches[0]: "봄 여름 가을 겨울" String
            //vAnsShort[0]: "봄"
            //vAnsShort[1]: "여름"......
            curGameFragment.receiveSTTResult(matches.get(0));

            /*
            //주관식 문제에 대한 답안 가공
            vAnsShort = matches.get(0).split(" ");
            for (int i = 0; i < vAnsShort.length; i++) {
                System.out.println("주관식 답안["+ (i + 1) + "] : " + vAnsShort[i]);
            }

            //객관식 문제에 대한 답안 가공
            vResultString = matches.toString();
            vResultChar = vResultString.toCharArray();
            vAnsChoice.clear();

            for(int i = 0; i < vResultChar.length; i++) {
                switch (vResultChar[i]) {
                    case '1' :
                        vAnsChoice.add(1);
                        break;
                    case '2' :
                        vAnsChoice.add(2);
                        break;
                    case '3' :
                        vAnsChoice.add(3);
                        break;
                    case '4' :
                        vAnsChoice.add(4);
                        break;
                    case '5' :
                        vAnsChoice.add(5);
                        break;
                    case '6' :
                        vAnsChoice.add(6);
                        break;
                    case '7' :
                        vAnsChoice.add(7);
                        break;
                    case '8' :
                        vAnsChoice.add(8);
                        break;
                    case '9' :
                        vAnsChoice.add(9);
                        break;
                }
            }
            for(int i = 0; i < vAnsChoice.size(); i++)
            {
                Log.e("MainActivity", "" + vAnsChoice.get(i));
            }*/

        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    public QuizInfoSummary[] getQuizList() {
        return quizList;
    }

    public int[] getQuizScore() {
        return quizScore;
    }
}