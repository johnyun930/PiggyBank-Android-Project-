package com.example.piggybank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateSchedule extends AppCompatActivity {

    SQLiteDatabase db;
    SimpleDateFormat simpleDateFormat;
    ContentValues contentValues;
    PiggyBankDBHelper piggybankhelper;
    String dbname = "piggybank";
    final int version = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addschedule);
        Intent intent = getIntent();
        TextView title = findViewById(R.id.addformtitle);
        title.setText("Update Piggy Bank");
        final PiggybankInfo idInfo = (PiggybankInfo) intent.getSerializableExtra("bankInfo");
        final EditText piggybankTitle = findViewById(R.id.piggybanktitle);
        final EditText piggybankAmount = findViewById(R.id.piggybankamount);
        final DatePicker datePicker = findViewById(R.id.formdatepicker);
        piggybankTitle.setText(idInfo.getTitle());
        piggybankAmount.setText(Double.toString(idInfo.getAmout()));
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(idInfo.getDate()));
            datePicker.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
        }catch(Exception ex){
            Log.e("dateparse in update","error");
        }
        SharedPreferences sp = getSharedPreferences("piggybank",MODE_PRIVATE);
        final String userid = sp.getString("userid","");
        Button CancelBtn = findViewById(R.id.cancelButton);
        Button makeBtn = findViewById(R.id.makeButton);
        makeBtn.setText("Update");
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar calendar = Calendar.getInstance();
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        makeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = piggybankTitle.getText().toString();
                calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                Calendar current = Calendar.getInstance();
                long currenttime = current.getTimeInMillis()/(24*60*60*1000);
                long duedate = calendar.getTimeInMillis()/(24*60*60*1000);
                long dday = duedate - currenttime;
                if(dday<0){
                    Toast.makeText(UpdateSchedule.this,"Change your due date, that date is already over",Toast.LENGTH_SHORT).show();
                }else {
                    String date = simpleDateFormat.format(calendar.getTime());
                    double amount = Double.parseDouble(piggybankAmount.getText().toString());
                    db = openOrCreateDatabase(dbname, MODE_PRIVATE, null);
                    piggybankhelper = new PiggyBankDBHelper(UpdateSchedule.this, dbname, null, version);
                    db = piggybankhelper.getWritableDatabase();
                    contentValues = piggybankhelper.makepiggybank(title, amount, date, userid);
                    db.update("PiggyBanklist", contentValues, "piggybank_id =" + idInfo.getBankid(), null);
                    db.close();
                    finish();
                }
            }
        });
    }
}
