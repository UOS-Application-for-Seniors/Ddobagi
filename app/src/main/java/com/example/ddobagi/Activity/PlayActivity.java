package com.example.ddobagi.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ddobagi.Fragment.DrawClockFragment;
import com.example.ddobagi.Fragment.GameFragment;
import com.example.ddobagi.Fragment.MultipleChoiceFragment;
import com.example.ddobagi.Fragment.LineConnectionFragment;
import com.example.ddobagi.Fragment.ShortAnswerFragment;
import com.example.ddobagi.R;

public class PlayActivity extends AppCompatActivity {
    MultipleChoiceFragment multipleChoiceFragment;
    ShortAnswerFragment shortAnswerFragment;
    LineConnectionFragment lineConnectionFragment;
    DrawClockFragment drawClockFragment;

    int fragmentIndex = 0;
    int fragmentNum = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        multipleChoiceFragment = new MultipleChoiceFragment();
        shortAnswerFragment = new ShortAnswerFragment();
        lineConnectionFragment = new LineConnectionFragment();
        drawClockFragment = new DrawClockFragment();

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
                    fragmentIndex += 4;
                }
                fragmentChange();
            }
        });

        Button rightBtn = findViewById(R.id.right_btn);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentIndex = (fragmentIndex + 1) % 4;
                fragmentChange();
            }
        });

        fragmentChange();
    }

    private void fragmentChange(){
        GameFragment gameFragment;
        switch(fragmentIndex){
            case 0:
                gameFragment = multipleChoiceFragment;
                break;
            case 1:
                gameFragment = shortAnswerFragment;
                break;
            case 2:
                gameFragment = lineConnectionFragment;
                break;
            case 3:
                gameFragment = drawClockFragment;
                break;
            default:
                gameFragment = multipleChoiceFragment;
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, gameFragment).commit();
    }
}