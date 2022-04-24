package com.example.ddobagi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GameTestActivity extends AppCompatActivity {
    static RequestQueue requestQueue;
    Bitmap bitmap;
    ImageView imageView;
    Button[] choiceBtn = new Button[4];
    TextView quizDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        makeRequest();
        //downloadImage();

        Button exitBtn = findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button speakAns = findViewById(R.id.sttStart);
        speakAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VoiceRecognizer.class);
                startActivity(intent);
            }
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
        requestQueue.add(request);
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

}