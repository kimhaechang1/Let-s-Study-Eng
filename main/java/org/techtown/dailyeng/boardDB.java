package org.techtown.dailyeng;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class boardDB extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "ws.db";

    public boardDB( Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS board(year INTEGER, month INTEGER, day INTEGER, correct INTEGER, wrong VARCHAR(100))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS board");
        onCreate(db);
    }
    public void insert(String year, String month, String day, int correct, ArrayList<String> wrongs){
        SQLiteDatabase db = getWritableDatabase();
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);
        int d = Integer.parseInt(day);
        if (correct == 10){
            db.execSQL("INSERT INTO board VALUES("+y+","+m+","+d+","+correct+",'"+""+"')");
        }else{
            String w = convert(wrongs);
            db.execSQL("INSERT INTO board VALUES("+y+","+m+","+d+","+correct+",'"+w+"')");
        }
    }
    private String convert(ArrayList<String> data){
        StringBuilder result= new StringBuilder();
        for(String d : data){
            result.append(d);
            result.append(" ");
        }
        return result.toString();
    }
    public ArrayList<String> getWrongs(String year, String month, String day){
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT wrong FROM board WHERE (year = "+Integer.parseInt(year)+" AND month = "+Integer.parseInt(month)+" AND day = "+Integer.parseInt(day)+")",null);
        while(cursor.moveToNext()){
            data.add(cursor.getString(0));
        }
        return data;
    }
}
