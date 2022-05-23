package com.example.ddobagi.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.ddobagi.R;

import com.example.ddobagi.Class.*;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MAIN = 101;
    public static final int RESULT_LOGIN = 111;

    final int PERMISSION = 1;
    SharedPreferences share;
    SharedPreferences.Editor editor;

    boolean isLogin = false;
    Button loginBtn, testBtn, userInfoBtn;
    TextView curCoin;
    ImageView coinImgView;


    public void setLogin(boolean login) {
        isLogin = login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //통신 requestQueue 초기화
        Communication.init(this);

        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        share = getSharedPreferences("PREF", MODE_PRIVATE);
        editor = share.edit();

        setButton();

        if(share.getString("Refresh_token", null) != null){
            Communication.refreshToken(this);
        }
    }

    private void setButton(){
        testBtn = findViewById(R.id.main_test_btn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLogin){
                    Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                    intent.putExtra("type", "test");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "로그인 이후 사용할 수 있습니다",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button recommendPlayBtn = findViewById(R.id.main_recommend_play_btn);
        recommendPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("type", "recommend");
                intent.putExtra("isLogin", isLogin);
                startActivity(intent);
            }
        });

        Button selectPlayBtn = findViewById(R.id.main_select_play_btn);
        selectPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("type", "select");
                intent.putExtra("isLogin", isLogin);
                startActivity(intent);
            }
        });

//        Button leftBtn = findViewById(R.id.left_btn);
//        leftBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), TestPlayActivity.class);
//                startActivity(intent);
//            }
//        });

        Button exitBtn = findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userInfoBtn = findViewById(R.id.main_user_info_btn);
        userInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLogin){
                    Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "로그인 이후 사용할 수 있습니다",Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginBtn = findViewById(R.id.login_btn);
        loginManagement();

        curCoin = findViewById(R.id.coin_text);
        coinImgView = findViewById(R.id.coin_img);
    }

    private void setCoin(int coin){
        editor.putInt("coin", coin);
        editor.commit();
        DecimalFormat dc = new DecimalFormat("###,###,###,###,###,###");
        String ch = dc.format(coin);

        curCoin.setText(ch);
    }

    private int getCoin(){
        return share.getInt("coin", 0);
    }

    private void hideCoinView(boolean bool){
        if(bool){
            curCoin.setVisibility(View.INVISIBLE);
            coinImgView.setVisibility(View.INVISIBLE);
        }
        else{
            curCoin.setVisibility(View.VISIBLE);
            coinImgView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_MAIN){
            if(resultCode == RESULT_LOGIN){
                isLogin = true;
                loginManagement();
            }
        }
    }

    public void loginManagement(){
        if(isLogin){
            loginBtn.setText("로그아웃");
            Toast.makeText(this, "로그인 되었습니다", Toast.LENGTH_LONG).show();
            setCoin(getCoin());
            hideCoinView(false);
            testBtn.setBackgroundResource(R.drawable.blue_btn);
            userInfoBtn.setBackgroundResource(R.drawable.red_btn);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginTokenRemove();
                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다", Toast.LENGTH_LONG).show();
                    isLogin = false;
                    loginManagement();
                    hideCoinView(true);
                }
            });
        }
        else{
            loginBtn.setText("로그인");
            testBtn.setBackgroundResource(R.drawable.grey_btn);
            userInfoBtn.setBackgroundResource(R.drawable.grey_btn);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_MAIN);
                }
            });
        }
    }

    private void loginTokenRemove(){
        SharedPreferences pref = getSharedPreferences("PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCoin(getCoin());
    }
}