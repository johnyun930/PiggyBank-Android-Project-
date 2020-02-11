package com.example.piggybank;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterforSavingTable extends BaseAdapter {

    Context context;
    ArrayList<SavingInfo> mylist;
    AdapterforSavingTable(Context context, ArrayList<SavingInfo> mylist){
        this.context = context;
        this.mylist = mylist;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public Object getItem(int i) {
        return mylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            view = layoutInflater.inflate(R.layout.moneylist,null);
        }
        DecimalFormat df = new DecimalFormat("$##.##");
        TextView title = view.findViewById(R.id.moneylisttitle);
        TextView date = view.findViewById(R.id.moneylistdate);
        TextView moneydescription = view.findViewById(R.id.moneydescription);
        TextView amount = view.findViewById(R.id.moneylistamount);
        TextView categorydescription = view.findViewById(R.id.moneylistcategory);
        SavingInfo savinginfo = mylist.get(i);
        moneydescription.setText(savinginfo.getDescription());
        if(savinginfo.getCategorydescription().length()>10){
            moneydescription.setTextSize(10);
        }
        date.setText(savinginfo.getDate());
        categorydescription.setText(savinginfo.getCategorydescription());
        categorydescription.setTypeface(null,Typeface.BOLD);
        amount.setText(df.format(savinginfo.getMoneyamout()));
        if(mylist.get(i).getCategory()==0){
            title.setText("Income");
            title.setTextColor(Color.BLUE);
            amount.setTextColor(Color.BLUE);
        }else{
            title.setText("Expense");
            title.setTextColor(Color.RED);
            amount.setTextColor(Color.RED);

        }

        amount.setTypeface(null, Typeface.BOLD);

         return view;
    }
}
