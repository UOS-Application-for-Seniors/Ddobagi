package com.example.ddobagi.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ddobagi.R;

import com.example.ddobagi.Class.*;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.text.DecimalFormat;
import java.util.Calendar;

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

    Dialog recommendDialog;

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

        setComponent();

        if(share.getString("Refresh_token", null) != null){
            Communication.refreshToken(this);
        }

        if(share.getInt("initSkipGif", 0) == 0){
            GifLoader.initSkipGif(share);
            editor.putInt("initSkipGif", 1);
        }
    }

    private void setCalender(){
        MaterialCalendarView calendarView = recommendDialog.findViewById(R.id.calendar);

        // 월, 요일을 한글로 보이게 설정 (MonthArrayTitleFormatter의 작동을 확인하려면 밑의 setTitleFormatter()를 지운다)
        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        calendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));

        // 좌우 화살표 사이 연, 월의 폰트 스타일 설정
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        String dayStr = share.getString("playDates", "");
        dayStr += ",2022-5-29,2022-5-10,2022-5-20,2022-5-19,2022-5-28";

        Log.d("dayStr", dayStr);

//        HashSet<CalendarDay> dates = new HashSet<>();
//        CalendarDay today;
//        today = CalendarDay.today();
//        dates.add(today);

        calendarView.addDecorators(new SaturdayDecorator(), new SundayDecorator(), new FutureDecorator(),
                new DayDecorator(), new PlayDayDecorator(this, dayStr), new TodayDecorator(this));
    }

    private static class TodayDecorator implements DayViewDecorator {
        private final Drawable drawable;

        public TodayDecorator(Context context) {
            drawable = ContextCompat.getDrawable(context, R.drawable.calendar_today);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            CalendarDay today = CalendarDay.today();
            if(today.isAfter(day) || today.isBefore(day)){
                return false;
            }
            return true;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(drawable);
        }
    }

    /* 선택된 요일의 background를 설정하는 Decorator 클래스 */
    private static class DayDecorator implements DayViewDecorator {

        public DayDecorator() {
        }

        // true를 리턴 시 모든 요일에 내가 설정한 드로어블이 적용된다
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new AbsoluteSizeSpan(20));
            view.setDaysDisabled(true);
//            view.addSpan(new StyleSpan(Typeface.BOLD));   // 달력 안의 모든 숫자들이 볼드 처리됨
        }
    }

    private static class SaturdayDecorator implements DayViewDecorator {
        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    private static class SundayDecorator implements DayViewDecorator {
        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    private static class FutureDecorator implements DayViewDecorator {
        CalendarDay today = CalendarDay.today();

        public FutureDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return (today.getMonth() < day.getMonth() || today.getDay() < day.getDay());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.GRAY));
        }
    }

    private static class PlayDayDecorator implements DayViewDecorator {
        private final Drawable drawable;
        private final String[] dates;

        public PlayDayDecorator(Context context, String dayStr) {
            drawable = ContextCompat.getDrawable(context, R.drawable.played_day_element);
            this.dates = dayStr.split(",");
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            String dayStr = day.getYear() + "-" + (day.getMonth() + 1) + "-" + day.getDay();
            //Log.d("dayStr", dayStr);
            for(String tmpDay : dates){
                if(tmpDay.equals(dayStr)){
                    //Log.d("true", "true");
                    return true;
                }
            }
            return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(drawable);
        }
    }

    private void setComponent(){
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


        recommendDialog = new Dialog(this);
        recommendDialog.setContentView(R.layout.recommend_dialog);

        Button dialogRecommendBtn = recommendDialog.findViewById(R.id.recommend_play_btn);
        dialogRecommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recommendDialog.dismiss();

                CalendarDay today = CalendarDay.today();
                String todayStr = today.getYear()+"-"+ (today.getMonth()+1)+"-"+ today.getDay();
                editor.putString("notRecommend", todayStr);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("type", "recommend");
                intent.putExtra("isLogin", isLogin);
                startActivity(intent);
            }
        });

        Button dialogExitBtn = recommendDialog.findViewById(R.id.recommend_exit_btn);
        CheckBox dialogCheck = recommendDialog.findViewById(R.id.recommend_check);
        dialogExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialogCheck.isChecked()){
                    CalendarDay today = CalendarDay.today();
                    String todayStr = today.getYear()+"-"+ (today.getMonth()+1)+"-"+ today.getDay();
                    editor.putString("notRecommend", todayStr);
                    editor.commit();
                }
                recommendDialog.dismiss();
            }
        });
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
            showRecommendDialog();

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

    private void showRecommendDialog(){
        CalendarDay today = CalendarDay.today();
        String todayStr = today.getYear()+"-"+ (today.getMonth()+1)+"-"+ today.getDay();
        if(!share.getString("notRecommend","").equals(todayStr)){
            setCalender();
            recommendDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCoin(getCoin());
        if(isLogin){
            showRecommendDialog();
        }
    }
}