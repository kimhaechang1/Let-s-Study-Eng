package org.techtown.dailyeng;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    ArrayList<String> items;
    Context context;
    public MyAdapter(Context context, ArrayList<String> items){
        this.items = items;
        this.context = context;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = null;
        if (convertView != null){
            view = convertView;
        }else{
            LayoutInflater layoutInflater = ((AppCompatActivity)context).getLayoutInflater();
            view = layoutInflater.inflate(R.layout.list_item, viewGroup,false);
        }
        String eng = ((items.get(position)).split("-"))[0];
        String mean = ((items.get(position)).split("-"))[1];
        TextView word = view.findViewById(R.id.word);
        TextView word2 = view.findViewById(R.id.word2);
        word.setText(eng);
        word2.setText(mean);
        return view;
    }
}