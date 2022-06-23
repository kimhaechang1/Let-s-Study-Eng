package org.techtown.dailyeng;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;

public class result extends AppCompatActivity {
    TextView correct;
    TextView noti;
    LinearLayout main;
    ListView lv;
    MyAdapter myAdapter;
    protected void onCreate(Bundle savedInstanceState){
        wordDB wdb = new wordDB(result.this, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        main = findViewById(R.id.main);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) main.getLayoutParams();
        correct = findViewById(R.id.correct);
        noti = findViewById(R.id.noti);
        lv = findViewById(R.id.listView1);
        Button recall = findViewById(R.id.recall);
        Intent getWrongsandcorrects = getIntent();
        ArrayList<String> wrongs = getWrongsandcorrects.getStringArrayListExtra("wrong");
        int cor = getWrongsandcorrects.getIntExtra("correct",0);
        if(cor == 10){
            noti.setText("훌륭합니다!");
            correct.setText("10 / 10");
            params.height= ViewGroup.LayoutParams.MATCH_PARENT;
            params.weight = 0;
        }else{
            ArrayList<String> data = new ArrayList<>();
            if(cor>=7 && cor<10){
                noti.setText("만점을 향해!");
                correct.setText(cor+" / 10");

            }else if (cor>=4 && cor<=6){
                noti.setText("나쁘지 않아요!");
                correct.setText(cor+" / 10");
            }else{
                noti.setText("좀 더 노력합시다");
                correct.setText(cor+" / 10");
            }
            SQLiteDatabase db = wdb.getReadableDatabase();
            for(String s : wrongs){
                Cursor cursor = db.rawQuery("SELECT Eng, Mean FROM ws WHERE ENG = '"+s+"'",null);
                cursor.moveToNext();
                data.add(cursor.getString(0)+"-"+cursor.getString(1));
            }
            myAdapter = new MyAdapter(this, data);
            lv.setAdapter(myAdapter);

        }
        recall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goQuizView = new Intent(getApplicationContext(), quizActivity.class);
                startActivity(goQuizView);
            }
        });
    }
}
