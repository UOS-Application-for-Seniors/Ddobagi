package com.example.ddobagi.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ddobagi.Fragment.MultipleChoiceFragment;
import com.example.ddobagi.Fragment.PaintingFragment;
import com.example.ddobagi.Fragment.ShortAnswerFragment;
import com.example.ddobagi.R;

public class PlayActivity extends AppCompatActivity {
    MultipleChoiceFragment multipleChoiceFragment;
    ShortAnswerFragment shortAnswerFragment;
    PaintingFragment paintingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        multipleChoiceFragment = new MultipleChoiceFragment();
        shortAnswerFragment = new ShortAnswerFragment();
        paintingFragment = new PaintingFragment();

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
                getSupportFragmentManager().beginTransaction().replace(R.id.container, shortAnswerFragment).commit();
            }
        });

        Button rightBtn = findViewById(R.id.right_btn);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, paintingFragment).commit();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container, multipleChoiceFragment).commit();
    }
}