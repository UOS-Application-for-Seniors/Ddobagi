package com.example.ddobagi.Activity;

import static android.speech.tts.TextToSpeech.ERROR;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.Fragment.BeatFragment;
import com.example.ddobagi.Fragment.ChoiceWithPictureFragment;
import com.example.ddobagi.Fragment.DrawClockFragment;
import com.example.ddobagi.Fragment.FluentTestFragment;
import com.example.ddobagi.Fragment.GameFragment;
import com.example.ddobagi.Fragment.LineConnectionFragment;
import com.example.ddobagi.Fragment.MultipleChoiceFragment;
import com.example.ddobagi.Fragment.PaintShapeFragment;
import com.example.ddobagi.Fragment.SequenceChoiceFragment;
import com.example.ddobagi.Fragment.ShortAnswerFragment;
import com.example.ddobagi.Fragment.TraceShapeFragment;
import com.example.ddobagi.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class TestPlayActivity extends AppCompatActivity {
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

    SharedPreferences share;

    QuizInfoSummary[] quizList;
    int[] quizScore;
    int quizIndex = 0;

    int fragmentIndex = 0;
    int fragmentNum = 10;

    Intent sttIntent;
    SpeechRecognizer mRecognizer;
    Button sttBtn;
    TextView textView;

    Vector<Integer> vAnsChoice = new Vector<Integer>();
    String[] vAnsShort = null;
    String vResultString = "";
    char[] vResultChar;
    Button sttSubmit;

    TextToSpeech tts;
    Button ttsBtn;

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


        Button commitBtn = findViewById(R.id.commit_btn);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curGameFragment != null){
                    if(curGameFragment.commit() == 1){
                        Toast.makeText(getApplicationContext(), "정답입니다",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "오답입니다",Toast.LENGTH_LONG).show();
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

        Button leftBtn = findViewById(R.id.left_btn);
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

        //=============================음성인식=============================
        textView = (TextView) findViewById(R.id.sttResult);
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

        fragmentChange();
    }
    //=================================================================

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
            case 7:
                curGameFragment = paintShapeFragment;
                break;
            case 8:
                curGameFragment = beatFragment;
                break;
            case 9:
                curGameFragment = fluentTestFragment;
                break;
            default:
                curGameFragment = multipleChoiceFragment;
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, curGameFragment).commit();

        if(curGameFragment.isSTTAble()){
            sttBtn.setVisibility(View.VISIBLE);
        }
        else{
            sttBtn.setVisibility(View.INVISIBLE);
        }
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
                textView.setText(matches.get(i));
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
}