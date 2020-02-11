package com.example.piggybank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class UpdateSavingTableOption extends AppCompatActivity {

    ArrayList<String> menu = new ArrayList<>();
    String dbname = "piggybank";
    PiggyBankDBHelper piggybankhelper;
    SQLiteDatabase database;
    int version = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banklistupdate);
        addmenu(menu);
        ListView listView = findViewById(R.id.updatelistview);
        AdapterforOption adapter = new AdapterforOption(this,menu);
        listView.setAdapter(adapter);
        final Intent intent = getIntent();
        final SavingInfo id = (SavingInfo) intent.getSerializableExtra("SavingInfo");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent intent1 = new Intent(UpdateSavingTableOption.this, UpdateSaving.class);
                        intent1.putExtra("SavingInfo",id);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(R.anim.rightin_activity,R.anim.not_move_activity);
                        break;
                    case 1:
                        database = openOrCreateDatabase(dbname,MODE_PRIVATE,null);
                        piggybankhelper  = new PiggyBankDBHelper(UpdateSavingTableOption.this,dbname,null,version);
                        database = piggybankhelper.getWritableDatabase();
                        piggybankhelper.deletelist(database,id.getSavingid());
                        database.close();
                        finish();
                        break;
                }
            }
        });

    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return  false;
        }
        return true;
    }



    private void addmenu(ArrayList<String> menu){
        menu.add("Update");
        menu.add("Delete");
    }
}



