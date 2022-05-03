package com.example.ddobagi.Activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.LoadImage;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameTestActivity extends AppCompatActivity {
    Bitmap bitmap;
    ImageView imageView;
    Button[] choiceBtn = new Button[4];
    TextView quizDetail;

    Intent intent;
    SpeechRecognizer mRecognizer;
    Button sttBtn;
    TextView textView;

    int voiceAnswer;
    TextView voiceanscheck;
    String voiceString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);

//        makeRequest();
        //downloadImage();

        Button exitBtn = findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        voiceanscheck = (TextView) findViewById(R.id.sttResult2);

        textView = (TextView) findViewById(R.id.sttResult);
        sttBtn = (Button) findViewById(R.id.sttStart);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        sttBtn.setOnClickListener(v -> {
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(intent);
        });

        choiceBtn[0] = findViewById(R.id.selectBtn1);
        choiceBtn[1] = findViewById(R.id.selectBtn2);
        choiceBtn[2] = findViewById(R.id.selectBtn3);
        choiceBtn[3] = findViewById(R.id.selectBtn4);

        for(int i=0;i<4;i++){
            choiceBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "정답이 아닙네다", Toast.LENGTH_LONG).show();
                }
            });
        }

        quizDetail = findViewById(R.id.quizDetail);
    }

/*

    public void handleVolleyError(VolleyError error){
        NetworkResponse response = error.networkResponse;
        if(error instanceof ServerError && response != null){
            try{
                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers,"utf-8"));
                println(res);
            }catch (UnsupportedEncodingException e1){
                e1.printStackTrace();
            }
        }
        println("onErrorResponse: " + String.valueOf(error));
    }


    public void makeRequest(){
        String url = "http://121.164.170.67:3000/quiz/1";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답 --> " + response);
                        processQuizResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleVolleyError(error);
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
        println("요청 보냄.");
    }

    public void processQuizResponse(String response){
        String url = "http://121.164.170.67:3000/file/";

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        url = url + quiz.quizdatapath + "/";

        quizDetail.setText(quiz.quizdetail);

        String[] splitString = quiz.quizchoicesdetail.split("/");

        for(int i=0;i<4;i++){
            String tmp = url;
            tmp = tmp + Integer.toString(i + 1) + ".jfif";
            setImage(tmp, choiceBtn[i]);
            choiceBtn[i].setText(splitString[i]);
        }

        choiceBtn[Integer.parseInt(quiz.quizanswer) - 1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "정답입네다", Toast.LENGTH_LONG).show();
            }
        });
    }

//    public void imageRequest(){
//        String url = "http://121.164.170.67:3000/file/asd/test.png";
//
//        ImageRequest request = new ImageRequest(
//                url,
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap response) {
//
//                    }
//                }
//        )
//    }

    public void setImage(String url, Button button){
        //String url = "https://cdn.pixabay.com/photo/2022/04/18/19/51/rocks-7141482__340.jpg";
        LoadImage loadImage = new LoadImage((bitmap) -> {
            Drawable drawable;

            drawable = new BitmapDrawable(bitmap);
            drawable.setBounds( 0, 0, 200, 200);
            button.setCompoundDrawables(null, drawable, null, null);
            //imageView.setImageDrawable(drawable);
        });
        loadImage.execute(url);
    }

    public void println(String data){
        Log.d("GameTestActivity", data);
    }
*/
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
                textView.setText(matches.get(i));

//            String key = "";
//            key = SpeechRecognizer.RESULTS_RECOGNITION;
//            ArrayList<String> mResult = bundle.getStringArrayList(key);
//            String[] rs = new String[mResult.size()];
//            mResult.toArray(rs);
//            for(int i = 0; i < mResult.size(); i++) {
//                textView.setText(mResult.get(i));
//            }
            }
            voiceString = matches.toString();
            voiceanscheck.setText(voiceString);
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };
}