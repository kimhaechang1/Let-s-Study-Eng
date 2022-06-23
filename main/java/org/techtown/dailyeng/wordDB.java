package org.techtown.dailyeng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class wordDB extends SQLiteOpenHelper {
    int i=0;
    static final String DATABASE_NAME = "ws.db";

    public wordDB(@Nullable Context context, int version) {
        super(context,DATABASE_NAME,null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ws(IDX INTEGER, Eng CHAR(20), Mean CHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS ws");
        onCreate(db);
    }

    public void insert(String eng, String mean){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO ws VALUES("+i+",'"+eng+"','"+mean+"')");
        db.close();
        i+=1;
    }
    public ArrayList<String> findRealAnswer(String wrongs){
        ArrayList<String> wrongsWithAnswer = new ArrayList<>();
        String[] w = wrongs.split(" ");
        SQLiteDatabase db = getReadableDatabase();
        for(String data : w){
            Cursor cursor = db.rawQuery("SELECT Eng,Mean FROM ws WHERE Eng = '"+data+"'",null);
            cursor.moveToNext();
            wrongsWithAnswer.add(cursor.getString(0)+"-"+cursor.getString(1));
        }
        return wrongsWithAnswer;
    }
    public ArrayList<String> getResult(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> words = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM ws",null);
        Cursor cursor1 = db.rawQuery("SELECT IDX FROM ws WHERE IDX = (SELECT IDX FROM ws ORDER BY IDX DESC LIMIT 1)",null);
        cursor1.moveToNext();
        i = cursor1.getInt(0)+1;
        while(cursor.moveToNext()){
            words.add(cursor.getString(1) + "-" + cursor.getString(2));
        }
        return words;
    }
}
