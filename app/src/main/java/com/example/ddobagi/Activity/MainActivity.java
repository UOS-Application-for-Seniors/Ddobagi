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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import java.util.HashMap;
import java.util.Map;

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

    Dialog calendarDialog;
    TextView attendanceText, recommendText;
    Button attendanceBtn, recommendBtn;
    LinearLayout attendanceLayout, recommendLayout;

    public void setLogin(boolean login) {
        isLogin = login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //?????? requestQueue ?????????
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
        MaterialCalendarView calendarView = calendarDialog.findViewById(R.id.calendar);

        // ???, ????????? ????????? ????????? ?????? (MonthArrayTitleFormatter??? ????????? ??????????????? ?????? setTitleFormatter()??? ?????????)
        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        calendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));

        // ?????? ????????? ?????? ???, ?????? ?????? ????????? ??????
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        String dayStr = share.getString("playDates", "");
        //dayStr += ",2022-5-29,2022-5-10,2022-5-20,2022-5-19,2022-5-28";

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

    /* ????????? ????????? background??? ???????????? Decorator ????????? */
    private static class DayDecorator implements DayViewDecorator {

        public DayDecorator() {
        }

        // true??? ?????? ??? ?????? ????????? ?????? ????????? ??????????????? ????????????
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        // ?????? ?????? ??? ?????? ????????? ??????????????? ??????????????? ??????
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new AbsoluteSizeSpan(20));
            view.setDaysDisabled(true);
//            view.addSpan(new StyleSpan(Typeface.BOLD));   // ?????? ?????? ?????? ???????????? ?????? ?????????
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
            if(today.getMonth() < day.getMonth()){
                return true;
            }
            else if(today.getMonth() == day.getMonth()){
                if(today.getDay() < day.getDay()){
                    return true;
                }
            }
            return false;
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
                    intent.putExtra("isLogin", isLogin);
                    startActivity(intent);
                }
                else{
                    makeToast("????????? ?????? ???????????? ??? ????????????");
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
                    makeToast("????????? ?????? ???????????? ??? ????????????");
                }
            }
        });

        loginBtn = findViewById(R.id.login_btn);
        loginManagement();

        curCoin = findViewById(R.id.coin_text);
        coinImgView = findViewById(R.id.coin_img);


        calendarDialog = new Dialog(this);
        calendarDialog.setContentView(R.layout.recommend_dialog);
        
        recommendLayout = calendarDialog.findViewById(R.id.recommend_layout);
        recommendText = calendarDialog.findViewById(R.id.recommend_text_top);
        recommendBtn = calendarDialog.findViewById(R.id.recommend_play_btn);
        attendanceLayout = calendarDialog.findViewById(R.id.attendance_layout);
        attendanceBtn = calendarDialog.findViewById(R.id.attendance_btn);
        attendanceText = calendarDialog.findViewById(R.id.attendance_text);

        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarDialog.dismiss();

                CalendarDay today = CalendarDay.today();
                String todayStr = today.getYear()+"-"+ (today.getMonth()+1)+"-"+ today.getDay();
                editor.putString("lastRecommendDay", todayStr);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("type", "recommend");
                intent.putExtra("isLogin", isLogin);
                startActivity(intent);
            }
        });

        Button dialogExitBtn = calendarDialog.findViewById(R.id.recommend_exit_btn);
        CheckBox dialogCheck = calendarDialog.findViewById(R.id.recommend_check);
        dialogExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialogCheck.isChecked()){
                    CalendarDay today = CalendarDay.today();
                    String todayStr = today.getYear()+"-"+ (today.getMonth()+1)+"-"+ today.getDay();
                    editor.putString("notRecommend", todayStr);
                    editor.commit();
                }
                calendarDialog.dismiss();
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

            loginBtn.setText("????????????");
            makeToast("????????? ???????????????");
            setCoin(getCoin());
            hideCoinView(false);
            testBtn.setBackgroundResource(R.drawable.blue_btn);
            userInfoBtn.setBackgroundResource(R.drawable.red_btn);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginTokenRemove();
                    makeToast("???????????? ???????????????");
                    isLogin = false;
                    loginManagement();
                    hideCoinView(true);
                }
            });
        }
        else{
            loginBtn.setText("?????????");
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

    public boolean isLogin() {
        return isLogin;
    }

    private void loginTokenRemove(){
        editor.putString("Access_token", "");
        editor.putString("Access_token_expiration", "");
        editor.putString("Access_token_time", "");
        editor.putString("Refresh_token", "");
        editor.putString("Refresh_token_expiration", "");
        editor.putString("Refresh_token_time", "");
        editor.putInt("coin", 0);
        editor.putString("address", "");
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        calendarDialog.dismiss();
    }

    private void showRecommendDialog(){
        CalendarDay today = CalendarDay.today();
        int todayYear, todayMonth, todayDay;

        todayYear = today.getYear();
        todayMonth = (today.getMonth()+1);
        todayDay = today.getDay();

        String todayStr = todayYear + "-" + todayMonth + "-" + todayDay;

        if(!share.getString("notRecommend","").equals(todayStr)){
            setCalender();
            Log.d("lastPlayDay", share.getString("lastPlayDay",""));
            if(share.getString("lastPlayDay", "").equals(todayStr)){
                String[] playDates = share.getString("playDates", "").split(",");

                int attend = 1, curDay = todayDay - 1;

                boolean loop = true;
                while(curDay > 0 && loop){
                    loop = false;
                    for(String dayStr:playDates){
                        String[] tmpDay = dayStr.split("-");
                        if(!tmpDay[0].equals(Integer.toString(todayYear))){
                            continue;
                        }
                        else if(!tmpDay[1].equals(Integer.toString(todayMonth))){
                            continue;
                        }

                        if(tmpDay[2].equals(Integer.toString(curDay))){
                            loop = true;
                            curDay--;
                            attend++;
                            break;
                        }
                    }
                }

                attendanceText.setText("?????????\n\"" + attend + "???\" ?????? ??????!");
                final int fixAttend = attend;
                if(!share.getString("attendDay","").equals(todayStr)){
                    attendanceBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int coin = fixAttend * 100;

                            editor.putString("attendDay", todayStr);
                            editor.commit();

                            makeToast("?????? " + coin + "?????? ??????????????????!");
                            sendServerCoin(coin);
                            setCoin(getCoin() + coin);

                            attendanceBtn.setBackgroundResource(R.drawable.grey_btn);
                            attendanceBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    makeToast("????????? ????????? ??????????????????!\n?????? ?????? ??????????????????");
                                }
                            });
                        }
                    });
                }
                else{
                    attendanceBtn.setBackgroundResource(R.drawable.grey_btn);
                    attendanceBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            makeToast("????????? ????????? ??????????????????!\n?????? ?????? ??????????????????");
                        }
                    });
                }


                recommendLayout.setVisibility(View.INVISIBLE);
                attendanceLayout.setVisibility(View.VISIBLE);
            }
            else if(share.getString("lastRecommendDay","").equals(todayStr)){
                recommendText.setVisibility(View.INVISIBLE);
                recommendBtn.setBackgroundResource(R.drawable.grey_btn);
                recommendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeToast("?????? ?????? ?????? ????????? ??????????????????.\n?????? ?????? ?????????????????????");
                    }
                });
                recommendLayout.setVisibility(View.VISIBLE);
                attendanceLayout.setVisibility(View.INVISIBLE);
            }
            else{
                recommendLayout.setVisibility(View.VISIBLE);
                attendanceLayout.setVisibility(View.INVISIBLE);
            }
            calendarDialog.show();
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

    private void sendServerCoin(int coin){
            String url= Communication.addCoinUrl;

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Communication.println("?????? ??????" + coin + " ????????? ??????");
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
                    params.put("coin", Integer.toString(coin));

                    return params;
                }
            };
            request.setShouldCache(false);
            Communication.requestQueue.add(request);
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
}