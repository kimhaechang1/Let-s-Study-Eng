package org.techtown.dailyeng;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class addEngActivity extends AppCompatActivity {
    ArrayList<String> items = new ArrayList<>();
    ListView listView;
    MyAdapter myadapter;
    Button addandSave;

    wordDB wdb = new wordDB(addEngActivity.this, 1);

    private void loadWordByDB(){
        items = wdb.getResult();
    }
    private boolean isDuplicate(String eng){
        try{
            if(eng.equals("")){
                return true;
            }
            for(String s : items){
                Log.d("error",s);
                if((s.split("-")[0]).equals(eng)){
                    return true;
                }
            }
        }catch(ArrayIndexOutOfBoundsException e){
            Log.d("error",String.valueOf(items));
            Log.d("error",String.valueOf(items.size()));
            Log.d("error",e.toString());
        }
        return false;
    }

    private void writeWordByDB(String Eng, String Mean){
        wdb.insert(Eng, Mean);
    }
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addeng);
        listView = findViewById(R.id.listView);
        loadWordByDB();
        myadapter = new MyAdapter(this, items);
        listView.setAdapter(myadapter);
        addandSave = findViewById(R.id.addandsave);
        EditText e = findViewById(R.id.engW);
        EditText m = findViewById(R.id.Mean);
        addandSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDuplicate(e.getText().toString())){
                    Toast.makeText(getApplicationContext(),"중복된 단어가 존재하거나 잘못된 요청입니다.", LENGTH_SHORT).show();
                }else{
                    writeWordByDB( e.getText().toString(), m.getText().toString());
                    loadWordByDB();
                    myadapter = new MyAdapter(addEngActivity.this, items);
                    listView.setAdapter(myadapter);
                    e.setText("");
                    m.setText("");
                }
            }
        });
    }
}