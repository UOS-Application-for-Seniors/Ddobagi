//package com.example.ddobagi.Class;
//
//import android.app.Application;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.speech.RecognitionListener;
//import android.speech.SpeechRecognizer;
//import android.speech.tts.TextToSpeech;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.Vector;
//
//public class VoiceRecognition {
//    Context context;
//    Intent sttIntent;
//    SpeechRecognizer mRecognizer;
//    Button sttBtn;
//    TextView textView;
//
//    Vector<Integer> vAnsChoice = new Vector<Integer>();
//    String[] vAnsShort = null;
//    String vResultString = "";
//    char[] vResultChar;
//    Button sttSubmit;
//
//    static TextToSpeech tts;
//    Button ttsBtn;
//
//    /*public static void init(Context context){
//        this.context = context;
//    }*/
//
//    //=============================음성인식 listener=============================
//    private static RecognitionListener listener = new RecognitionListener() {
//        @Override
//        public void onReadyForSpeech(Bundle bundle) {
//            //Toast.makeText(context, "음성인식을 시작합니다", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onBeginningOfSpeech() {
//
//        }
//
//        @Override
//        public void onRmsChanged(float v) {
//
//        }
//
//        @Override
//        public void onBufferReceived(byte[] bytes) {
//
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//
//        }
//
//        @Override
//        public void onError(int error) {
//            String message;
//
//            switch (error) {
//                case SpeechRecognizer.ERROR_AUDIO:
//                    message = "오디오 에러";
//                    break;
//                case SpeechRecognizer.ERROR_CLIENT:
//                    message = "클라이언트 에러";
//                    break;
//                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
//                    message = "퍼미션 없음";
//                    break;
//                case SpeechRecognizer.ERROR_NETWORK:
//                    message = "네트워크 에러";
//                    break;
//                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
//                    message = "네트워크 타임아웃";
//                    break;
//                case SpeechRecognizer.ERROR_NO_MATCH:
//                    message = "찾을 수 없음";
//                    break;
//                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
//                    message = "RECOGNIZER가 바쁨";
//                    break;
//                case SpeechRecognizer.ERROR_SERVER:
//                    message = "말하는 시간 초과";
//                    break;
//                default:
//                    message = "알 수 없는 오류";
//                    break;
//            }
//
//            //Toast.makeText(context, "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onResults(Bundle bundle) {
//            ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//            for (int i = 0; i < matches.size(); i++) {
//                Log.e("MainActivity" ,"" + matches.get(i));
//                textView.setText(matches.get(i));
//            }
//
//            //matches[0]: "봄 여름 가을 겨울" String
//            //vAnsShort[0]: "봄"
//            //vAnsShort[1]: "여름"......
//            curGameFragment.receiveSTTResult(matches.get(0));
//
//            /*
//            //주관식 문제에 대한 답안 가공
//            vAnsShort = matches.get(0).split(" ");
//            for (int i = 0; i < vAnsShort.length; i++) {
//                System.out.println("주관식 답안["+ (i + 1) + "] : " + vAnsShort[i]);
//            }
//
//            //객관식 문제에 대한 답안 가공
//            vResultString = matches.toString();
//            vResultChar = vResultString.toCharArray();
//            vAnsChoice.clear();
//
//            for(int i = 0; i < vResultChar.length; i++) {
//                switch (vResultChar[i]) {
//                    case '1' :
//                        vAnsChoice.add(1);
//                        break;
//                    case '2' :
//                        vAnsChoice.add(2);
//                        break;
//                    case '3' :
//                        vAnsChoice.add(3);
//                        break;
//                    case '4' :
//                        vAnsChoice.add(4);
//                        break;
//                    case '5' :
//                        vAnsChoice.add(5);
//                        break;
//                    case '6' :
//                        vAnsChoice.add(6);
//                        break;
//                    case '7' :
//                        vAnsChoice.add(7);
//                        break;
//                    case '8' :
//                        vAnsChoice.add(8);
//                        break;
//                    case '9' :
//                        vAnsChoice.add(9);
//                        break;
//                }
//            }
//            for(int i = 0; i < vAnsChoice.size(); i++)
//            {
//                Log.e("MainActivity", "" + vAnsChoice.get(i));
//            }*/
//
//        }
//
//        @Override
//        public void onPartialResults(Bundle bundle) {
//
//        }
//
//        @Override
//        public void onEvent(int i, Bundle bundle) {
//
//        }
//
//    };
//    public static void onDestroy(){
//        if(tts != null) {
//            tts.stop();
//            tts.shutdown();
//            tts = null;
//        }
//    }
//}
