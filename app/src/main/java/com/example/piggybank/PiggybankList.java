package com.example.piggybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class PiggybankList extends AppCompatActivity {
    ArrayList<PiggybankInfo> idInfo;
    String dbname = "piggybank";
    PiggyBankDBHelper piggybankhelper;
    SQLiteDatabase database;
    int version = 1;
    private final long FINISH_INTERVAL = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.dashboradtoolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle(R.string.list);


    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if(0<=intervalTime&&FINISH_INTERVAL>=intervalTime){
            super.onBackPressed();
        }else{
            backPressedTime = tempTime;
            Toast.makeText(PiggybankList.this,"Press back button to exit the app",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        idInfo = new ArrayList<>();
        int amount = 0;
        SharedPreferences sp = getSharedPreferences("piggybank",MODE_PRIVATE);
        String userid = sp.getString("userid","");
        Button addbutton = findViewById(R.id.addbutton);
        final ListView list = findViewById(R.id.piggybanklist);
        database = openOrCreateDatabase(dbname,MODE_PRIVATE,null);
        piggybankhelper  = new PiggyBankDBHelper(this,dbname,null,version);
        database = piggybankhelper.getReadableDatabase();
        piggybankhelper.readbanks(database, idInfo,userid);

            Adapterforbanklist customAdapter = new Adapterforbanklist(this, idInfo);
            list.setAdapter(customAdapter);

        database.close();
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PiggybankList.this, AddPiggybank.class);

                startActivity(intent);

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PiggybankList.this,MainActivity.class);
                SharedPreferences sp = getSharedPreferences("piggybank",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("piggybankid",idInfo.get(i).getBankid());
                editor.putString("amount",Double.toString(idInfo.get(i).getAmout()));
                editor.commit();

                startActivity(intent);
                overridePendingTransition(R.anim.rightin_activity,R.anim.not_move_activity);

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(PiggybankList.this, UpdatePiggybank.class);
                intent.putExtra("bankInfo",idInfo.get(i));
                startActivity(intent);


                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.tohelp:{
                Intent intent = new Intent(PiggybankList.this,HelpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.rightin_activity,R.anim.not_move_activity);
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_question,menu);
        return true;
    }
}
