package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SavingHome extends Fragment {

    ArrayList<SavingInfo> savinginfos;
    SQLiteDatabase sqLiteDatabase;
    PiggyBankDBHelper piggyBankDBHelper;
    final String dbname = "piggybank";
    final int version = 1;

    long dday = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_savinghome,container,false);
        ImageView imageView = view.findViewById(R.id.currentStatus);
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);

        Button formaddbutton = view.findViewById(R.id.formaddbtn);
        Toolbar toolbar = view.findViewById(R.id.accountToolbar);
        toolbar.setTitle("Pig E Account");
        toolbar.setTitleTextColor(Color.WHITE);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        formaddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddSummary.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.rightin_activity,R.anim.not_move_activity);

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String due = "";
        TextView income = getView().findViewById(R.id.totalincome);
        TextView outcome = getView().findViewById(R.id.totaloutcome);
        TextView reminder = getView().findViewById(R.id.totalreminder);
        ImageView currentimage = getView().findViewById(R.id.currentStatus);
        TextView imagedescription = getView().findViewById(R.id.imagedescription);
        ListView listView = getView().findViewById(R.id.moneylistview);
        SharedPreferences sp = this.getActivity().getSharedPreferences("piggybank",getContext().MODE_PRIVATE);
        int id= sp.getInt("piggybankid",0);
        savinginfos = new ArrayList<>();
        sqLiteDatabase = getContext().openOrCreateDatabase(dbname,getActivity().MODE_PRIVATE,null);
        piggyBankDBHelper = new PiggyBankDBHelper(getContext(),dbname,null,version);
        sqLiteDatabase = piggyBankDBHelper.getReadableDatabase();
        piggyBankDBHelper.readAllSavinginfo(sqLiteDatabase,savinginfos,id);
        due = piggyBankDBHelper.readduedate(sqLiteDatabase,id);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar currenttime = Calendar.getInstance();
            Calendar duedate = Calendar.getInstance();
            duedate.setTime(simpleDateFormat.parse(due));
            long current = currenttime.getTimeInMillis() / (24 * 60 * 60 * 1000);
            long duetime = duedate.getTimeInMillis() / (24 * 60 * 60 * 1000);
            dday = duetime - current;
        }catch (Exception e){

        }

        AdapterforSavingTable moneylistAdapter = new AdapterforSavingTable(getContext(),savinginfos);
        double totalamount = Double.parseDouble(sp.getString("amount",null));
        double totalincome = 0;
        double totaloutcome = 0;
        double totalreminder = 0;

        for(int i = 0; i<savinginfos.size();i++){
            if(savinginfos.get(i).getCategory()==0){
                totalincome += savinginfos.get(i).getMoneyamout();
            }else{
                totaloutcome += savinginfos.get(i).getMoneyamout();
            }
        }
        totalreminder = totalamount - totalincome+totaloutcome;
        double percent = totalreminder/totalamount;
        DecimalFormat df = new DecimalFormat("$##.##");
        income.setText(df.format(totalincome));
        outcome.setText(df.format(totaloutcome));
        reminder.setText(df.format(totalreminder));
        listView.setAdapter(moneylistAdapter);

        Drawable img = getResources().getDrawable(R.drawable.payup);
        imagedescription.setText("More More");
         if(totalincome<totaloutcome){
            img = getResources().getDrawable(R.drawable.loss);
            imagedescription.setText("No Money... Debt");
        }else if(dday<10&&percent>0.7){
            img = getResources().getDrawable(R.drawable.notime);
            imagedescription.setText("Time is almost up.. T_T");
        }else if(dday<10&&percent>0.5){
            img = getResources().getDrawable(R.drawable.pleasepay);
            imagedescription.setText("Cheer up");
        }else if(totalreminder <0){
            img = getResources().getDrawable(R.drawable.deal);
            imagedescription.setText("SUCCESS");
        }
        currentimage.setImageDrawable(img);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(),UpdateSavingTableOption.class);
                intent.putExtra("SavingInfo",savinginfos.get(i));
                startActivity(intent);

                return true;
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar1,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }



        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case android.R.id.home:{
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.not_move_activity,R.anim.rightout_activity);

                    return true;
                }
                case R.id.tograph:{
                    ((MainActivity)getActivity()).replaceFragmnet(new IncomePiechart());
                    return true;
                }


            }
            return super.onOptionsItemSelected(item);
        }

}
