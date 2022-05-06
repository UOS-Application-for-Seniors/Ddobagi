package com.example.ddobagi.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.LoadImage;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GameTestActivity extends AppCompatActivity {
    Bitmap bitmap;
    ImageView imageView;
    Button[] choiceBtn = new Button[4];
    TextView quizDetail;

    Intent intent;
    SpeechRecognizer mRecognizer;
    Button sttBtn;
    TextView textView;

    Vector<Integer> vAnsChoice = new Vector<Integer>();
    String[] vAnsShort = null;
    String vResultString = "";
    char[] vResultChar;
    Button sttSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);

        makeRequest();
        //downloadImage();

        Button exitBtn = findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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

        sttSubmit = (Button) findViewById(R.id.sttSubmit);

        choiceBtn[0] = findViewById(R.id.choice_with_pic_select_btn1);
        choiceBtn[1] = findViewById(R.id.short_img_btn);
        choiceBtn[2] = findViewById(R.id.choice_with_pic_select_Btn3);
        choiceBtn[3] = findViewById(R.id.choice_with_pic_select_Btn4);

        for (int i = 0; i < 4; i++) {
            choiceBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "정답이 아닙네다", Toast.LENGTH_LONG).show();
                }
            });
        }

        quizDetail = findViewById(R.id.quizDetail);
    }

    public void makeRequest() {
        String url = "http://121.164.170.67:3000/quiz/0";

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
                        if(error.networkResponse.statusCode==401) {
                            Communication.refreshToken(getApplicationContext());
                        }
                        else{
                            Communication.handleVolleyError(error);
                        }
                    }
                }
        ) {
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
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Communication.requestQueue.add(request);
        println("요청 보냄.");
    }

    public void processQuizResponse(String response) {
        String url = "http://121.164.170.67:3000/file/";

        Gson gson = new Gson();
        Quiz quiz = gson.fromJson(response, Quiz.class);

        url = url + quiz.quizdatapath + "/";

        quizDetail.setText(quiz.quizdetail);

        String[] splitString = quiz.quizchoicesdetail.split(",");

        for (int i = 0; i < 4; i++) {
            String tmp = url;
            tmp = tmp + Integer.toString(i) + ".jfif";
            setImage(tmp, choiceBtn[i]);
            choiceBtn[i].setText(splitString[i]);
        }

        sttSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((vAnsChoice.get(0)) == Integer.parseInt(quiz.quizanswer)) {
                    Toast.makeText(getApplicationContext(), "정답입네다", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "정답이 아닙네다", Toast.LENGTH_LONG).show();
                }
            }
        });

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

    public void setImage(String url, Button button) {
        //String url = "https://cdn.pixabay.com/photo/2022/04/18/19/51/rocks-7141482__340.jpg";
        LoadImage loadImage = new LoadImage((bitmap) -> {
            Drawable drawable;

            drawable = new BitmapDrawable(bitmap);
            drawable.setBounds(0, 0, 200, 200);
            button.setCompoundDrawables(null, drawable, null, null);
            //imageView.setImageDrawable(drawable);
        });
        loadImage.execute(url);
    }

    public void println(String data) {
        Log.d("GameTestActivity", data);
    }

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
                textView.setText(matches.get(i));
            }

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
            }

        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }

    };
}