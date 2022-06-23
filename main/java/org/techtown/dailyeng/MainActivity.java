package org.techtown.dailyeng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button start; // 시작버튼 -> quiz
    Button leaderBoard; // 기록보기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.start);
        leaderBoard = findViewById(R.id.leaderBoard);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent quiz = new Intent(getApplicationContext(), quizActivity.class);
                startActivity(quiz);
            }
        });
        leaderBoard.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent lb = new Intent(getApplicationContext(), leaderboardActivity.class);
                startActivity(lb);
            }
        });
    }

}