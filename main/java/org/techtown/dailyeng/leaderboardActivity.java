package org.techtown.dailyeng;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class leaderboardActivity extends AppCompatActivity {
    TextView noti2;
    TextView rangeOfChange1;
    TextView rangeOfChange2;
    Button recall2;
    LinearLayout main2;
    Date mdate;
    ListView todayWrongs;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String [] getCurrentTime(){
        String [] time;
        long currentTimeMillis = System.currentTimeMillis();
        mdate = new Date(currentTimeMillis);
        String getTime  = simpleDateFormat.format(mdate);
        time = getTime.split("-");
        return time;
    }
    private String getTodayCorrectData(){
        String [] today = getCurrentTime();
        int avg=0;
        String result = "";
        boardDB bdb = new boardDB(leaderboardActivity.this, 1);
        SQLiteDatabase db = bdb.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT correct FROM board WHERE (year = "+Integer.parseInt(today[0])+" AND month = "+Integer.parseInt(today[1])+" AND day = "+Integer.parseInt(today[2])+")", null);
        if(cursor.getCount()==0){
            return result;
        }
        int sum = 0;
        while(cursor.moveToNext()){
            sum += cursor.getInt(0);
        }
        avg = sum/cursor.getCount();
        result = String.valueOf(avg)+" "+String.valueOf(cursor.getCount());
        return result;
    }

    protected void onCreate(Bundle savedInstanceState){
        ArrayList<String> wrongs;
        ArrayList<String> wrongsWithAnswer;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);
        noti2 = findViewById(R.id.noti2);
        rangeOfChange1 = findViewById(R.id.rangeOfChange1);
        rangeOfChange2 = findViewById(R.id.rangeOfChange2);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) findViewById(R.id.main2).getLayoutParams();
        recall2 = findViewById(R.id.recall2);
        String todayResult = getTodayCorrectData();
        MyAdapter myadapter;
        noti2.setText("오늘의 통계");
        if(todayResult.equals("")){
            rangeOfChange1.setText("오늘의 퀴즈를 해 주세요!");
            rangeOfChange2.setText("아직 오늘 한번도 안하셨어요!");
            params.weight = 0;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }else{
            String []r = todayResult.split(" ");
            int avg = Integer.parseInt(r[0]);
            int times = Integer.parseInt(r[1]);
            rangeOfChange1.setText("오늘은 "+times+"번 퀴즈를 풀었고");
            rangeOfChange2.setText("평균 "+avg+"개의 단어를 맞추셨습니다!");
            wordDB wdb = new wordDB(leaderboardActivity.this, 1);
            boardDB bdb = new boardDB(leaderboardActivity.this, 1);
            String []time = getCurrentTime();
            wrongs = bdb.getWrongs(time[0],time[1],time[2]);
            Set<String> s = new HashSet<>();
            for (String w : wrongs){
                if (w.equals("")){ // 10개 모두 맞춘 경우는 찾을 필요가 없다.
                    continue;
                }
                ArrayList<String> temp = wdb.findRealAnswer(w);
                s.addAll(temp);
            }
            if (s.size()>0){
                wrongsWithAnswer = new ArrayList<>(s);
                myadapter = new MyAdapter(this, wrongsWithAnswer);
                todayWrongs = findViewById(R.id.listView1);
                todayWrongs.setAdapter(myadapter);
            }else{
                params.weight = 0;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            }

        }
        recall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goMainActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(goMainActivity);
            }
        });
    }
}
