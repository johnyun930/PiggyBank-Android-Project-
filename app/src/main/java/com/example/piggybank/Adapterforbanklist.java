package com.example.piggybank;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Adapterforbanklist extends BaseAdapter {
    Context context;
    ArrayList<PiggybankInfo> myarray = new ArrayList<>();
    SQLiteDatabase sqLiteDatabase;
    String dbname = "piggybank";
    PiggyBankDBHelper piggyBankDBHelper;
    final int version = 1;
    public Adapterforbanklist(Context context, ArrayList<PiggybankInfo> myarray) {
        this.context = context;
        this.myarray = myarray;
    }

    @Override
    public int getCount() {
        return myarray.size();
    }

    @Override
    public Object getItem(int i) {
        return myarray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layout = LayoutInflater.from(viewGroup.getContext());
            view = layout.inflate(R.layout.activity_externallayout, null);

        }

        TextView titletextview = view.findViewById(R.id.titletxtview);
        TextView duedatetextview = view.findViewById(R.id.duedatetxt);
        TextView result = view.findViewById(R.id.percentagetxt);
        ProgressBar amountbar = view.findViewById(R.id.amoutbar);

        Drawable img = viewGroup.getResources().getDrawable(R.drawable.piggybankicon);
        img.setBounds(0,0,120,120);
        titletextview.setText(myarray.get(i).getTitle());
        titletextview.setCompoundDrawables(img,null,null,null);
        titletextview.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        titletextview.setCompoundDrawablePadding(15);
        titletextview.setPadding(15,0,0,0);
        titletextview.setTextSize(15);
        titletextview.setTypeface(null,Typeface.BOLD_ITALIC);

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar currenttime = Calendar.getInstance();
            Calendar duedate = Calendar.getInstance();
            duedate.setTime(simpleDateFormat.parse(myarray.get(i).getDate()));
            long current = currenttime.getTimeInMillis()/(24*60*60*1000);
            long due = duedate.getTimeInMillis()/(24*60*60*1000);
            long dday = due - current;
            if(dday == 0){
                duedatetextview.setText("DUE DATE");
            }else if(dday<0){
                duedatetextview.setText("D-("+dday + ")");
            }else {
                duedatetextview.setText("D-" + dday);
            }
            if(dday>30){
                duedatetextview.setTextColor(Color.BLUE);
            }else{
                duedatetextview.setTextColor(Color.RED);
            }

        }catch (Exception e){
            Log.e("calendar parse","Calendar parse error");
        }

        duedatetextview.setPadding(0,0,30,0);
        duedatetextview.setTextSize(20);
        duedatetextview.setTypeface(null, Typeface.BOLD);
        duedatetextview.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);

        sqLiteDatabase = context.openOrCreateDatabase(dbname,context.MODE_PRIVATE,null);
        piggyBankDBHelper = new PiggyBankDBHelper(context,dbname,null,version);
        sqLiteDatabase = piggyBankDBHelper.getReadableDatabase();
        int savingamount = piggyBankDBHelper.readAllSavingamount(sqLiteDatabase,myarray.get(i).getBankid());
        sqLiteDatabase.close();
        amountbar.setMax((int)myarray.get(i).getAmout());
        if(savingamount <0){
            savingamount = 0;
        }
        amountbar.setProgress(savingamount);
        DecimalFormat df = new DecimalFormat("#.###%");
        result.setText(df.format(savingamount/myarray.get(i).getAmout()));


        return view;
    }
    public void  getduedate(String date)
    {

    }


}
