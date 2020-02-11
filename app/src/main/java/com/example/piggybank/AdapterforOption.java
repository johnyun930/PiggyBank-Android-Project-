package com.example.piggybank;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdapterforOption extends BaseAdapter {
    Context context;
    ArrayList<String> array;

    AdapterforOption(Context context, ArrayList<String> array){
        this.context = context;
        this.array = array;
    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.updatelist,null);
        }
        TextView option = view.findViewById(R.id.menuoption);
        option.setText(array.get(i));
        option.setGravity(Gravity.CENTER);
        option.setTypeface(null, Typeface.BOLD);
        option.setTextSize(20);

        return view;
    }
}
