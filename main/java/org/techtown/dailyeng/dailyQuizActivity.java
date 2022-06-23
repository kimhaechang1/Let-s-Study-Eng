package org.techtown.dailyeng;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
public class dailyQuizActivity extends AppCompatActivity {
    ArrayList<String> problems = new ArrayList<>(); // 문제들이 모여있는 list
    HashMap<String, String> map = new HashMap<>(); // 문제 : 답 으로 되어있는 데이터
    Button getResult; // 결과보기 버튼
    Button nextandsubmit; // next 버튼
    Button abort; // 중단 버튼
    TextView problem; // problem 들이 랜덤하게 출력
    EditText answer; // 사용자가 입력한 answer 값
    
    ArrayList<String> wrongs = new ArrayList<>(); // 틀린 답안과 문제 저장
    int correct = 0; // 맞춘 개수
    int []randNum = new int[10];
    wordDB wdb = new wordDB(dailyQuizActivity.this,1);

    private void makeProblem(){
        ArrayList<String> temp = new ArrayList<>();
        SQLiteDatabase db = wdb.getReadableDatabase();
        Intent getRandomNum = getIntent();
        randNum = getRandomNum.getIntArrayExtra("random");
        for(int rn : randNum){
            Cursor cursor = db.rawQuery("SELECT * FROM WS WHERE IDX = "+rn+"",null);
            cursor.moveToNext();
            map.put(cursor.getString(1),cursor.getString(2));
            temp.add(cursor.getString(1));
        }
        problems.addAll(temp);
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailyquiz);
        makeProblem(); // 문제 만들기
        nextandsubmit = findViewById(R.id.next);
        nextandsubmit.setClickable(true);
        abort = findViewById(R.id.abort);
        getResult = findViewById(R.id.rs);
        answer = findViewById(R.id.answer);
        problem = findViewById(R.id.problem);
        problem.setText(problems.get(0));
        nextandsubmit.setOnClickListener(new View.OnClickListener(){
            int count = 0;
            @Override
            public void onClick(View view) {
                if((map.get(problems.get(count)).equals(answer.getText().toString()))){
                    correct++;
                }else{
                    wrongs.add(problems.get(count));
                }
                count++;
                if(count ==10){
                    getResult.setVisibility(View.VISIBLE);
                    nextandsubmit.setClickable(false);
                }else{
                    problem.setText(problems.get(count));
                }
                answer.setText("");
            }

        });
        getResult.setOnClickListener(new View.OnClickListener(){

            Date mDate;
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
            @Override
            public void onClick(View view) {
                Intent result = new Intent(getApplicationContext(), result.class);
                result.putExtra("wrong", wrongs);
                result.putExtra("correct",correct);
                boardDB bdb = new boardDB(dailyQuizActivity.this, 1);
                long Now = System.currentTimeMillis();
                mDate = new Date(Now);
                String getTime = mFormat.format(mDate);
                String [] time = getTime.split("-");
                bdb.insert(time[0],time[1],time[2],correct, wrongs);
                startActivity(result);
            }
        });
        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent quiz = new Intent(getApplicationContext(), quizActivity.class);
                startActivity(quiz);
            }
        });
    }
}
