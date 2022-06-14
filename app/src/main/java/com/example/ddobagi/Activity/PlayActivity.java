package com.example.ddobagi.Activity;

import static android.speech.tts.TextToSpeech.ERROR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.GameInfoSummary;
import com.example.ddobagi.Class.GifLoader;
import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.Fragment.BeatFragment;
import com.example.ddobagi.Fragment.CIST10Fragment;
import com.example.ddobagi.Fragment.ChoiceWithPictureFragment;
import com.example.ddobagi.Fragment.DrawClockFragment;
import com.example.ddobagi.Fragment.FluentTestFragment;
import com.example.ddobagi.Fragment.GameFragment;
import com.example.ddobagi.Fragment.GameSelectFragment;
import com.example.ddobagi.Fragment.ListenAndSolveFragment;
import com.example.ddobagi.Fragment.MemorizationFragment;
import com.example.ddobagi.Fragment.MemoryRecallFragment;
import com.example.ddobagi.Fragment.MultipleChoiceFragment;
import com.example.ddobagi.Fragment.LineConnectionFragment;
import com.example.ddobagi.Fragment.PaintShapeFragment;
import com.example.ddobagi.Fragment.PlayResultFragment;
import com.example.ddobagi.Fragment.SequenceChoiceFragment;
import com.example.ddobagi.Fragment.ShortAnswerFragment;
import com.example.ddobagi.Fragment.TestResultFragment;
import com.example.ddobagi.Fragment.TraceShapeFragment;
import com.example.ddobagi.R;
import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class PlayActivity extends AppCompatActivity {
    final int wrongAnswerCoin = 0;
    final int correctAnswerCoin = 100;

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
    MemoryRecallFragment memoryRecallFragment;
    CIST10Fragment cist10Fragment;

    PlayResultFragment playResultFragment;
    TestResultFragment testResultFragment;
    GameSelectFragment gameSelectFragment;

    Date date;

    SharedPreferences share;
    SharedPreferences.Editor editor;

    QuizInfoSummary[] quizList;
    int[] quizScore;
    int[] quizCoin;
    int quizIndex = 0;

    int fragmentIndex = 0;
    int fragmentNum = 13;

    Intent sttIntent;
    SpeechRecognizer recognizer;
    Button sttBtn;
    TextView sttResultView;

    Button sttSubmit;

    TextToSpeech tts;
    Button ttsBtn;

    LinearLayout resultPerQuizLayout, resultCoinLayout;
    Button exitBtn;
    public Button commitBtn;
    TextView centerText, curCoin;
    ImageView coinImg, resultCoinImg;
    TextView resultCoinText,resultCoinBonusText, resultCheerText, resultBaseText;

    LinearLayout exampleLayout;
    TextView exampleBtn;
    ImageView exampleImg;
    CheckBox exampleCheckBox;

    Party explode, paradeLeft, paradeRight, quizRain, resultRain;
    KonfettiView konfettiView;
    Shape.DrawableShape drawableShape;

    boolean isLogin;
    public boolean isTest;
    boolean isRecommend;
    String type;

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
        memoryRecallFragment = new MemoryRecallFragment();
        cist10Fragment = new CIST10Fragment();

        playResultFragment = new PlayResultFragment();
        testResultFragment = new TestResultFragment();

        share = getSharedPreferences("PREF", MODE_PRIVATE);
        editor = share.edit();

        setComponent();

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        isLogin = intent.getBooleanExtra("isLogin", false);

        if(isLogin){
            setCoin(getCoin());
            hideCoinView(false);
        }
        else{
            hideCoinView(true);
        }

        if(type.equals("select")){
            setCenterText("놀이");
            gameSelectFragment = new GameSelectFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, gameSelectFragment).commit();
            ttsBtn.setVisibility(View.GONE);
            commitBtn.setVisibility(View.INVISIBLE);
            sttBtn.setVisibility(View.GONE);
            ttsBtn.setPadding(470, 0, 0, 0);
            ttsBtn.setBackgroundResource(R.drawable.tts_btn_full);

            //resultPerQuizControl(-1);
        }
        else{
            getRecommendQuizList(type);
            resultPerQuizControl(4);
        }
    }

    public String coinFormat(int coin){
        DecimalFormat dc = new DecimalFormat("###,###,###,###,###,###");
        return dc.format(coin);
    }

    public void setCoin(int coin){
        if(isLogin){
            editor.putInt("coin", coin);
            editor.commit();

            curCoin.setText(coinFormat(coin));
        }
    }

    public int getCoin(){
        if(isLogin){
            return share.getInt("coin", 0);
        }
        return 0;
    }

    public void hideCoinView(boolean bool){
        if(bool){
            curCoin.setVisibility(View.INVISIBLE);
            coinImg.setVisibility(View.INVISIBLE);
        }
        else{
            curCoin.setVisibility(View.VISIBLE);
            coinImg.setVisibility(View.VISIBLE);
        }
    }

    private void getRecommendQuizList(String type){
        String url;
        if(type.equals("recommend")){
            url = Communication.recommendQuizListUrl;
            isRecommend = true;
        }
        else if (type.equals("test")){
            url = Communication.testListUrl;
            isTest = true;
        }
        else{
            return;
        }

        //refreshShare();

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
                            makeToast("서버가 켜져 있지 않습니다\n잠시 후 다시 시도해보세요");
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
        Communication.println("recommendQuizList 요청 보냄");
    }

    public void onGetQuizListResponse(String response){
        Gson gson = new Gson();
        quizList = gson.fromJson(response, QuizInfoSummary[].class);
        if(quizList == null || quizList.length == 0){
            Log.d("warning","quizList is null");
            return;
        }

        quizScore = new int[quizList.length];
        quizCoin = new int[quizList.length];
        loadGame();
    }

    public void onGameSelect(GameInfoSummary game, int difficulty){
        getSelectQuizList(game.gameid, difficulty);
        commitBtn.setVisibility(View.VISIBLE);
        ttsBtn.setVisibility(View.VISIBLE);
    }

    private void getSelectQuizList(int gameID, int difficulty){
            //refreshShare();
            String url = Communication.selectQuizList;
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Communication.println("getSelectQuizList: " + response);
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
                                else if(error.networkResponse.statusCode==406){
                                    makeToast("선택한 놀이가 서버에 등록되지 않았습니다\n다른 놀이와 난이도를 즐겨주세요");
                                }
                            }
                            else{
                                Communication.handleVolleyError(error);
                                makeToast("서버가 켜져 있지 않습니다\n잠시 후 다시 시도해보세요");
                            }
                        }
                    }
            ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gameID", Integer.toString(gameID));
                params.put("difficulty", Integer.toString(difficulty));
                return params;
            }
        };
        request.setShouldCache(false);
        Communication.requestQueue.add(request);
        Communication.println("selectQuizList 요청 보냄");
    }

    private void refreshShare(){
        if((share != null) && (share.contains("Access_token"))){
            //Toast.makeText(getApplicationContext(), share.getString("Access_token", ""), Toast.LENGTH_LONG).show();
            date = new Date();
            Communication.println(String.valueOf(date.getTime()) + " " + String.valueOf(share.getLong("Access_token_time", 0)
            ) );
            if(date.getTime() - share.getLong("Access_token_time", 0) > 100000) {
                Communication.refreshToken(getApplicationContext());
                Communication.println("Refreshed");
            }
        }
    }

    void sendGameScore(int gameID, int score, int difficulty, int coin){
        String url;
        if(isTest){
            url = Communication.sendTestResultUrl;
        }
        else{
            url = Communication.sendGameResultUrl;
        }
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Communication.println("gameID:" + gameID + ", score: " + score + " 서버로 보냄");
                        if(isTest){
                            if(gameID == 54){
                                onDementiaDiagnosisResponse(response);
                            }
                        }
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

                if(!isTest){
                    params.put("coin", Integer.toString(coin));
                    params.put("difficulty", Integer.toString(difficulty));
                }

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
            case "drawClock":
                curGameFragment = drawClockFragment;
                break;
            case "traceShape":
                curGameFragment = traceShapeFragment;
                break;
            case "paintShape":
                curGameFragment = paintShapeFragment;
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
            case "memoryRecall":
                curGameFragment = memoryRecallFragment;
                break;
            case "cist10":
                curGameFragment = cist10Fragment;
                break;
            case "0":
            case "1":
            case "2":
                quizScore[quizIndex] = Integer.parseInt(usingFragment);
                sendGameScore(quizList[quizIndex].gameid, Integer.parseInt(usingFragment), 0, 0);
                quizIndex++;
                if(quizIndex < quizList.length){
                    loadGame();
                }
                return;
            default:
                curGameFragment = null;
        }
        if(curGameFragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            if(fragmentManager == null || fragmentManager.isDestroyed()){
                return;
            }
            fragmentManager.beginTransaction().replace(R.id.container, curGameFragment).commit();
            int difficulty;
            if(isTest){
                difficulty = 0;
            }
            else{
                difficulty = Integer.parseInt(quizList[quizIndex].difficulty);
            }
            curGameFragment.loadGame(quizList[quizIndex].gameid, quizList[quizIndex].quizid, difficulty);
            if(curGameFragment.isSTTAble()){
                sttBtn.setVisibility(View.VISIBLE);
                ttsBtn.setPadding(200, 0, 0, 0);
                ttsBtn.setBackgroundResource(R.drawable.tts_btn);
            }
            else{
                sttBtn.setVisibility(View.GONE);
                ttsBtn.setPadding(470, 0, 0, 0);
                ttsBtn.setBackgroundResource(R.drawable.tts_btn_full);
            }
            //Log.d("gameID", Integer.toString(quizList[quizIndex].gameid));
        }
    }

    private void setComponent(){
        centerText = findViewById(R.id.center_text);
        curCoin = findViewById(R.id.coin_text);
        coinImg = findViewById(R.id.coin_img);

        commitBtn = findViewById(R.id.commit_btn);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCommit();
            }
        });

        exitBtn = findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        resultPerQuizLayout = findViewById(R.id.result_per_quiz_layout);
        resultPerQuizLayout.setVisibility(View.INVISIBLE);
        resultPerQuizLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultPerQuizControl(-1);
            }
        });

        resultCoinLayout = findViewById(R.id.result_coin_layout);
        resultCoinText = findViewById(R.id.result_coin_text);
        resultCoinImg = findViewById(R.id.result_coin_img);
        resultCoinBonusText = findViewById(R.id.result_coin_bonus_text);
        resultBaseText = findViewById(R.id.result_base_text);
        resultCheerText = findViewById(R.id.result_cheer_text);

        exampleLayout = findViewById(R.id.example_layout);
        exampleLayout.setVisibility(View.INVISIBLE);
        exampleBtn = findViewById(R.id.example_button);
        exampleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exampleCheckBox.isChecked()){
                    exampleCheckBox.setChecked(false);
                    resultPerQuizControl(-3);
                }
                else{
                    resultPerQuizControl(-2);
                }
            }
        });
        exampleCheckBox = findViewById(R.id.example_checkBox);
        //exampleText = findViewById(R.id.example_text);
        exampleImg = findViewById(R.id.example_img);

        //=============================음성인식=============================
        sttResultView = findViewById(R.id.sttResult);
        sttBtn = (Button) findViewById(R.id.sttStart);
        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        sttBtn.setOnClickListener(v -> {
            if(curGameFragment != null){
                if(curGameFragment.isReadyToCommit()){
                    recognizer = SpeechRecognizer.createSpeechRecognizer(this);
                    recognizer.setRecognitionListener(listener);
                    recognizer.startListening(sttIntent);
                }
            }
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
                if(curGameFragment != null){
                    if(curGameFragment.isReadyToCommit()){
                        CharSequence ttsText = curGameFragment.getQuizTTS();
                        if(ttsText == null){
                            return;
                        }
                        if(ttsText.equals("exceeded")){
                            makeToast("설명 듣기 시도 횟수를 초과했습니다");
                            return;
                        }
                        tts.setPitch(1.0f);         // 음성 톤 설정 (n배)
                        tts.setSpeechRate(0.7f);    // 읽는 속도 설정 (n배)
                        tts.speak(ttsText.toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });
        //=================================================================

        final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart);
        drawableShape = new Shape.DrawableShape(drawable, true);

        konfettiView = findViewById(R.id.konfetti);

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);

        explode = new PartyFactory(new Emitter(100L, TimeUnit.MILLISECONDS).max(100))
                .spread(360)
                .sizes(Size.Companion.getLARGE())
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(0f, 50f)
                .position(display.widthPixels / 2, display.heightPixels / 3)
                .build();

        quizRain = new PartyFactory(new Emitter(2, TimeUnit.SECONDS).perSecond(60))
                .angle(Angle.BOTTOM)
                .spread(Spread.ROUND)
                .sizes(Size.Companion.getLARGE())
                .timeToLive(2500L)
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(0f, 20f)
                .position(0, 0, display.widthPixels, 0)
                .build();
        resultRain = new PartyFactory(new Emitter(7, TimeUnit.SECONDS).perSecond(60))
                .angle(Angle.BOTTOM)
                .spread(Spread.ROUND)
                .sizes(Size.Companion.getLARGE())
                .timeToLive(2500L)
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(0f, 20f)
                .position(0, 0, display.widthPixels, 0)
                .build();

        EmitterConfig paradeEmitter = new Emitter(2, TimeUnit.SECONDS).perSecond(100);
        paradeLeft = new PartyFactory(paradeEmitter)
                .angle(Angle.RIGHT - 30)
                .spread(Spread.SMALL)
                .sizes(Size.Companion.getLARGE())
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(30f, 100f)
                .position(0, display.heightPixels/2)
                .build();
        paradeRight = new PartyFactory(paradeEmitter)
                .angle(Angle.LEFT + 30)
                .spread(Spread.SMALL)
                .sizes(Size.Companion.getLARGE())
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(30f, 100f)
                .position(display.widthPixels, display.heightPixels/2)
                .build();
    }

    private void onCommit(){
        if(curGameFragment != null){
            if(!curGameFragment.isReadyToCommit()){
               return;
            }

            int result = curGameFragment.commit();
            curGameFragment.init();

            int score;
            if(isTest){
                resultPerQuizControl(5);
                score = calcTestScore(quizList[quizIndex].gameid, result);
            }
            else{
                resultPerQuizControl(result);
                score = result;
            }

            quizScore[quizIndex] = score;

            //Log.d("score", Integer.toString(quizIndex) + ": " + Integer.toString(score));
            int difficulty;
            if(isTest){
                difficulty = 0;
            }
            else{
                difficulty = Integer.parseInt(quizList[quizIndex].difficulty);
            }
            sendGameScore(quizList[quizIndex].gameid, score, difficulty, quizCoin[quizIndex]);
            quizIndex++;
            if(quizIndex < quizList.length){
                loadGame();
            }
            else{
                String dayString = share.getString("playDates", "");
                String newStr;
                if(!dayString.equals("")){
                    newStr = ",";
                }
                else{
                    newStr = "";
                }
                CalendarDay today = CalendarDay.today();
                String todayStr = today.getYear() + "-" + (today.getMonth() + 1) + "-" + today.getDay();
                newStr += todayStr;

                String[] dates = dayString.split(",");

                boolean played = false;
                for(String day : dates){
                    if(day.equals(todayStr)){
                        played = true;
                        break;
                    }
                }

                if(!played){
                    dayString += newStr;
                    editor.putString("playDates", dayString);
                }

                editor.putString("lastPlayDay", todayStr);
                editor.commit();

                ttsBtn.setVisibility(View.GONE);
                commitBtn.setVisibility(View.INVISIBLE);
                sttBtn.setVisibility(View.GONE);
                ttsBtn.setPadding(470, 0, 0, 0);
                ttsBtn.setBackgroundResource(R.drawable.tts_btn_full);
                if(isTest){
                    makeToast("검사가 종료되었습니다. 수고하셨습니다.");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, testResultFragment).commit();
                }
                else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, playResultFragment).commit();
                }
                for(int i=0;i<quizScore.length; i++){
                    Log.d("quizScore", Integer.toString(i) + ": " + Integer.toString(quizScore[i]));
                }
                curGameFragment = null;
            }
        }
    }

    private void onDementiaDiagnosisResponse(String response){
        testResultFragment.onDementiaDiagnosisResponse(response);
    }

    private int calcTestScore(int gameid, int result){
        int score = 0;
        switch(gameid){
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 37:
            case 38:
            case 39:
            case 41:
            case 42:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
                if(result == 0){
                    score = 0;
                }
                else if(result == 1){
                    score = 1;
                }
                else if(result == 2){
                    score = 0;
                }
                break;

            case 40:
            case 43:
            case 54:
                if(result == 0){
                    score = 0;
                }
                else if(result == 1){
                    score = 2;
                }
                else if(result == 2){
                    score = 1;
                }
                break;

            default:
                //Log.d("calcTestScore","gameid error: " + gameid);
                break;

        }

        return score;
    }

    public void setCenterText(String str){
        centerText.setText(str);
    }

    public Button getExitBtn(){
        return exitBtn;
    }

    public void resultPerQuizControl(int result){
        String baseMsg = "\n여기를 눌러 계속 진행하세요.";
        int difficultyBonus = 0;
        switch (result){
            case -3: //다시 보지 않음을 누르고
                GifLoader.setSkipGif(quizList[quizIndex].usingfragment, share);
            case -2: //예시 GIF 끄기
                curGameFragment.setReadyToShow(false);
                curGameFragment.setReadyToCommit(true);
                ttsBtn.callOnClick();

                exampleLayout.setVisibility(View.INVISIBLE);
                return;
            case -1: //결과창 끄기
                if(quizList == null){
                    makeToast("문제를 불러오는 중입니다\n다시 시도해주세요");
                    return;
                }
                if(quizIndex + 1 > quizList.length){
                    centerText.setText("결과");
                    if(!isTest){
                        konfettiView.start(resultRain);
                    }
                }
                else if(curGameFragment.isReadyToShow()){
                    centerText.setText((quizIndex+1) + " 번 / " + (quizList.length) + " 문제");
                }
                else if(!curGameFragment.isReadyToShow()){
                    makeToast("문제를 불러오는 중입니다\n다시 시도해주세요");
                    return;
                }
                else{
                    return;
                }

                resultCheerText.setText("");
                resultBaseText.setText("");
                resultPerQuizLayout.setVisibility(View.INVISIBLE);
                resultCoinLayout.setVisibility(View.VISIBLE);
                if(curGameFragment != null){
                    if(GifLoader.needToLoadGif(quizList[quizIndex].usingfragment, share)){ //예시 GIF layout 표시
                        GifLoader.loadGif(this, exampleImg, quizList[quizIndex].usingfragment);
                        exampleLayout.setVisibility(View.VISIBLE);
                    }
                    else{
                        curGameFragment.setReadyToShow(false);
                        curGameFragment.setReadyToCommit(true);
                        ttsBtn.callOnClick();
                    }
                }
                return;
            case 0: //0, 1, 2 부분은 아래에서 한번에 처리
            case 1:
            case 2:
                break;
            case 3: //MemorizationFragment에서 호출. 별이 따로 없음
                resultCheerText.setText("잠시 뒤에 외우신 문장을 여쭈어보겠습니다");
                resultBaseText.setText(baseMsg);
                resultCoinLayout.setVisibility(View.GONE);
                resultPerQuizLayout.setVisibility(View.VISIBLE);
                quizCoin[quizIndex] = 0;
                return;
            case 4: //처음 게임 로딩할 때 호출
                if(isTest){
                    resultCheerText.setText("지금부터 인지 선별 검사를 진행합니다. \n알맞은 답을 고르고 우측 상단의 \n\"다음\" 버튼을 눌러주세요.");
                    resultBaseText.setText(baseMsg);
                    resultCoinImg.setVisibility(View.INVISIBLE);
                    resultCoinText.setVisibility(View.INVISIBLE);
                    hideCoinView(true);
                }
                else{
                    resultCheerText.setText("지금부터 놀이를 진행합니다.\n알맞은 답을 고르고 우측 상단의 \n\"다음\" 버튼을 눌러주세요.");
                    resultBaseText.setText(baseMsg);
                }
                resultCoinLayout.setVisibility(View.GONE);
                resultPerQuizLayout.setVisibility(View.VISIBLE);
                return;
            case 5: //인지 선별 검사 문제
                resultCheerText.setText("결과가 저장되었습니다");
                resultBaseText.setText(baseMsg);
                resultCoinLayout.setVisibility(View.GONE);
                resultPerQuizLayout.setVisibility(View.VISIBLE);
                quizCoin[quizIndex] = 0;
                return;
            default:
                Log.d("resultPerQuizControl", "올바르지 않은 result 값");
                return;
        }

        int coin = 1;
        String msg = "";
        int difficulty = Integer.parseInt(quizList[quizIndex].difficulty);
        String coinBonusStr = "";

        resultCoinBonusText.setVisibility(View.VISIBLE);

        switch (difficulty){
            case 0:
                resultCoinBonusText.setVisibility(View.INVISIBLE);
                break;
            case 1:
                coin *= 2;
                coinBonusStr += "보통 난이도 보너스 적용!\n";
                break;
            case 2:
                coin *= 3;
                coinBonusStr += "어려움 난이도 보너스 적용!\n";
                break;
            default:
        }

        if(isRecommend){
            resultCoinBonusText.setVisibility(View.VISIBLE);
            coinBonusStr  += "추천 놀이 보너스 적용!!";
            coin *= 5;
        }

        resultCoinBonusText.setText(coinBonusStr);

        if(result == 0){
            msg = "잘하고 계십니다";
            resultCoinBonusText.setVisibility(View.INVISIBLE);
            coin = wrongAnswerCoin;
            konfettiView.start(explode);
            resultCoinLayout.setVisibility(View.GONE);

        }
        else if(result == 1){
            msg = "완벽합니다";
            coin *= correctAnswerCoin;
            konfettiView.start(paradeLeft, paradeRight);
            setCoin(getCoin() + coin);
        }
        else if(result == 2){
            msg = "훌륭합니다";
            coin *= correctAnswerCoin/2;
            konfettiView.start(quizRain);
            setCoin(getCoin() + coin);
        }

        resultCoinText.setText("금화 " + coinFormat(coin) +"개 획득");
        resultCheerText.setText(msg);
        resultBaseText.setText(baseMsg);
        resultCoinImg.setVisibility(View.VISIBLE);
        resultCoinText.setVisibility(View.VISIBLE);
        resultPerQuizLayout.setVisibility(View.VISIBLE);
        quizCoin[quizIndex] = coin;
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
            makeToast("음성인식을 시작합니다");
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

            makeToast("음성을 잘 인식하지 못했습니다\n다시 시도해주세요");
        }

        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < matches.size(); i++) {
                Log.e("음성인식 결과: ", "" + matches.get(i));
                makeToast("음성인식: " + matches.get(i));
            }

            //matches[0]: "봄 여름 가을 겨울" String
            //vAnsShort[0]: "봄"
            //vAnsShort[1]: "여름"......
            if(curGameFragment != null){
                curGameFragment.receiveSTTResult(matches.get(0));
            }
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

    public int[] getQuizCoin() {
        return quizCoin;
    }


    public int[] getQuizScore() {
        return quizScore;
    }

    public void skipNextList(int index, int score){
        if(index<0){
            return;
        }
        quizList[quizIndex + index].usingfragment = Integer.toString(score);
    }

    public boolean isLogin() {
        return isLogin;
    }

    private void makeToast(String str){
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.layout));
        TextView textView = layout.findViewById(R.id.text);
        textView.setText(str);

        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void startRecommendQuiz(){
        isTest = false;
        hideCoinView(false);
        setCoin(getCoin());
        type = "recommend";
        getRecommendQuizList("recommend");
        resultPerQuizControl(4);
        quizIndex = 0;
        ttsBtn.setVisibility(View.VISIBLE);
        sttBtn.setVisibility(View.VISIBLE);
        commitBtn.setVisibility(View.VISIBLE);
    }
}