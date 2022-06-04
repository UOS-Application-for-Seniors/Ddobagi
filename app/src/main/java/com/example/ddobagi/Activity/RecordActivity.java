package com.example.ddobagi.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.PlayResultAdapter;
import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.Class.QuizResult;
import com.example.ddobagi.Class.Record;
import com.example.ddobagi.Class.RecordAdapter;
import com.example.ddobagi.R;
import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecordAdapter adapter;
    Dialog calendarDialog;

    SharedPreferences share;
    SharedPreferences.Editor editor;

    LinearLayout recommendLayout, attendanceLayout;
    TextView recommendText, attendanceText;
    Button recommendBtn, attendanceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        share = getSharedPreferences("PREF", MODE_PRIVATE);
        editor = share.edit();

        recyclerView = findViewById(R.id.record_recycler);
        Button exitBtn = findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button calendarBtn = findViewById(R.id.calendar_btn);
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecommendDialog();
            }
        });

        calendarDialog = new Dialog(this);
        calendarDialog.setContentView(R.layout.recommend_dialog);

        recommendLayout = calendarDialog.findViewById(R.id.recommend_layout);
        recommendText = calendarDialog.findViewById(R.id.recommend_text_top);
        recommendBtn = calendarDialog.findViewById(R.id.recommend_play_btn);
        attendanceLayout = calendarDialog.findViewById(R.id.attendance_layout);
        attendanceBtn = calendarDialog.findViewById(R.id.attendance_btn);
        attendanceText = calendarDialog.findViewById(R.id.attendance_text);

        CheckBox checkBox = calendarDialog.findViewById(R.id.recommend_check);
        checkBox.setVisibility(View.INVISIBLE);

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
                intent.putExtra("isLogin", true);
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

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecordAdapter();
        recyclerView.setAdapter(adapter);

        getRecord();
        setCalender();
    }

    private void setCalender(){
        MaterialCalendarView calendarView = calendarDialog.findViewById(R.id.calendar);

        // 월, 요일을 한글로 보이게 설정 (MonthArrayTitleFormatter의 작동을 확인하려면 밑의 setTitleFormatter()를 지운다)
        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        calendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));

        // 좌우 화살표 사이 연, 월의 폰트 스타일 설정
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


    private void getRecord(){
        SharedPreferences share = getSharedPreferences("PREF", MODE_PRIVATE);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Communication.recordUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Communication.println("응답 --> " + response);
                        onGetRecordResponse(response);
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

    private void onGetRecordResponse(String response){
        Gson gson = new Gson();
        Record[] tmp = gson.fromJson(response, Record[].class);
        if(tmp == null){
            Log.d("warning","quizList is null");
            return;
        }

        for(int i=0;i<tmp.length;i++){
            adapter.addItem(tmp[i]);
        }
        adapter.notifyDataSetChanged();
    }
    private void showRecommendDialog(){
        CalendarDay today = CalendarDay.today();
        int todayYear, todayMonth, todayDay;

        todayYear = today.getYear();
        todayMonth = (today.getMonth()+1);
        todayDay = today.getDay();

        String todayStr = todayYear + "-" + todayMonth + "-" + todayDay;

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

            attendanceText.setText("이번달\n\"" + attend + "일\" 연속 출석!");
            final int fixAttend = attend;
            if(!share.getString("attendDay","").equals(todayStr)){
                attendanceBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int coin = fixAttend * 100;

                        editor.putString("attendDay", todayStr);
                        editor.commit();

                        makeToast(coin + "개 금화를 획득했습니다!");
                        setCoin(getCoin() + coin);

                        attendanceBtn.setBackgroundResource(R.drawable.grey_btn);
                        attendanceBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                makeToast("오늘의 출석을 완료했습니다!\n내일 다시 출석해주세요");
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
                        makeToast("오늘의 출석을 완료했습니다!\n내일 다시 출석해주세요");
                    }
                });
            }


            recommendLayout.setVisibility(View.INVISIBLE);
            attendanceLayout.setVisibility(View.VISIBLE);
        }
        else if(share.getString("lastRecommendDay","").equals(todayStr)){
            recommendText.setVisibility(View.GONE);
            recommendBtn.setBackgroundResource(R.drawable.grey_btn);
            recommendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeToast("오늘 이미 추천 놀이를 즐기셨습니다.\n내일 다시 추천해드릴게요");
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

    private void setCoin(int coin){
        editor.putInt("coin", coin);
        editor.commit();
        DecimalFormat dc = new DecimalFormat("###,###,###,###,###,###");
        String ch = dc.format(coin);
    }

    private int getCoin(){
        return share.getInt("coin", 0);
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