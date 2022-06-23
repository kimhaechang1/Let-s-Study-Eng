package org.techtown.dailyeng;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class quizActivity extends AppCompatActivity {
    Button addEng;
    Button startQuiz;
    Button gotoMain;
    wordDB wdb = new wordDB(quizActivity.this, 1);
    int []makeRandomNum(int count){
        Random r = new Random();
        int[] answer = new int[10];
        for(int i=0;i<answer.length;i++){
            answer[i] = r.nextInt(count);
            for(int j=0;j<i;j++){
                if(answer[i] == answer[j]){
                    i--;
                }
            }
        }
        return answer;
    }
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        addEng = findViewById(R.id.addEng);
        startQuiz = findViewById(R.id.startQuiz);
        gotoMain = findViewById(R.id.mainMenu);
        addEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEng = new Intent(getApplicationContext(), addEngActivity.class);
                startActivity(addEng);
            }
        });
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startQuiz = new Intent(getApplicationContext(), dailyQuizActivity.class);
                int count = 0;
                try{
                    SQLiteDatabase db = wdb.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT IDX FROM ws WHERE IDX = (SELECT IDX FROM ws ORDER BY IDX DESC LIMIT 1)",null);
                    cursor.moveToNext();
                    count = cursor.getInt(0)+1;
                    db.close();
                }catch(IllegalArgumentException e) {
                    Log.d("error",e.toString());
                }
                if (count<10){
                    Toast.makeText(getApplicationContext(), "단어 목록을 10개 이상 추가 해 주세요",Toast.LENGTH_SHORT).show();
                }else{
                    int [] num = makeRandomNum(count);
                    startQuiz.putExtra("random",num);
                    startActivity(startQuiz);
                }

            }
        });
        gotoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
            }
        });
    }
}
