package com.example.piggybank;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddSummary extends AppCompatActivity {

    int version = 1;
    PiggyBankDBHelper piggyBankDBHelper;
    SQLiteDatabase database;
    final String dbname = "piggybank";
    int categoryType = 0;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        View include = findViewById(R.id.include2);
        Toolbar toolbar = findViewById(R.id.customtoolbar);
        toolbar.setTitle("Add Summary");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onResume() {
        super.onResume();

        final RadioButton incomebtn = findViewById(R.id.incomebtn);
        RadioButton expensebtn = findViewById(R.id.expensebtn);

        final TextView categorytext = findViewById(R.id.category);
        final DatePicker datetext = findViewById(R.id.dateform);
        final EditText amounttext = findViewById(R.id.amountform);
        final EditText description = findViewById(R.id.descriptionform);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button addbtn = findViewById(R.id.savebutton);
         sp = getSharedPreferences("piggybank",MODE_PRIVATE);
        final int piggbank_id = sp.getInt("piggybankid",0);
        final String category = sp.getString("adapteritem","");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar calendar = Calendar.getInstance();
        calendar.set(datetext.getYear(),datetext.getMonth(),datetext.getDayOfMonth());
        categorytext.setText(category);

        categorytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSummary.this,CategoryActivity.class);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("category",categoryType);
                editor.commit();
                startActivity(intent);
            }
        });


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(datetext.getYear(), datetext.getMonth(), datetext.getDayOfMonth());
                String money = amounttext.getText().toString();
                String date = simpleDateFormat.format(calendar.getTime());
                String categorydescription = categorytext.getText().toString();
                String textdescription = description.getText().toString();
                if(money.equals("")||date.equals("")){
                    Toast.makeText(AddSummary.this,"Please fill all the blank except the title",Toast.LENGTH_SHORT).show();
                }else if(categorydescription.equals("")){
                    Toast.makeText(AddSummary.this,"Please Select the category",Toast.LENGTH_SHORT).show();

                }else{
                    database = openOrCreateDatabase(dbname,MODE_PRIVATE,null);
                    piggyBankDBHelper  = new PiggyBankDBHelper(AddSummary.this,dbname,null,version);
                    database = piggyBankDBHelper.getWritableDatabase();
                    ContentValues contentValues = piggyBankDBHelper.addoruseMoney(Double.parseDouble(money),categoryType,categorydescription,date,textdescription,piggbank_id);
                    long i = database.insert("SavingTable",null,contentValues);
                    if(i !=-1){
                        SharedPreferences.Editor editor = sp.edit();
                        editor.remove("adapteritem");
                        editor.commit();
                        finish();
                        overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity);
                    }else{
                        Toast.makeText(AddSummary.this,"fail to put the data",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i){
                        case R.id.incomebtn:
                            categorytext.setText("");
                            categoryType =0;
                            break;
                        case R.id.expensebtn:
                            categorytext.setText("");
                            categoryType =1;
                            break;

                    }
            }
        });
    }

    @Override
    public void onBackPressed() {

        sp = getSharedPreferences("piggybank",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("adapteritem");
        editor.commit();
        finish();
        overridePendingTransition(R.anim.not_move_activity,R.anim.rightout_activity);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                SharedPreferences sharedPreferences = getSharedPreferences("piggybank",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("adapteritem");
                finish();
                overridePendingTransition(R.anim.not_move_activity,R.anim.rightout_activity);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
