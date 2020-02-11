package com.example.piggybank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddPiggybank extends AppCompatActivity {
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
        Toast.makeText(this,dbname,Toast.LENGTH_SHORT).show();
        final EditText piggybankTitle = findViewById(R.id.piggybanktitle);
        final EditText piggybankAmount = findViewById(R.id.piggybankamount);
        final DatePicker datePicker = findViewById(R.id.formdatepicker);
        SharedPreferences sp = getSharedPreferences("piggybank",MODE_PRIVATE);
        final String userid = sp.getString("userid","");
        Button CancelBtn = findViewById(R.id.cancelButton);
        Button makeBtn = findViewById(R.id.makeButton);
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
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                String date = simpleDateFormat.format(calendar.getTime());


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar currenttime = Calendar.getInstance();
                long current = currenttime.getTimeInMillis() / (24 * 60 * 60 * 1000);
                long due = calendar.getTimeInMillis() / (24 * 60 * 60 * 1000);
                long dday = due - current;
                String amountstring = piggybankAmount.getText().toString();
                if (amountstring.equals("")) {
                    Toast.makeText(AddPiggybank.this, "Please write the amount", Toast.LENGTH_SHORT).show();
                } else {
                    double amount = Double.parseDouble(amountstring);

                    if (amount <= 0) {
                        Toast.makeText(AddPiggybank.this, "Please set up the amount more than 0", Toast.LENGTH_SHORT).show();
                    } else if (title.equals("")) {
                        Toast.makeText(AddPiggybank.this, "Please write the title of this Pig E Bank", Toast.LENGTH_SHORT).show();

                    } else if (dday < 0) {
                        Toast.makeText(AddPiggybank.this, "Please do not set the past date for due date ", Toast.LENGTH_SHORT).show();

                    } else {

                        db = openOrCreateDatabase(dbname, MODE_PRIVATE, null);
                        piggybankhelper = new PiggyBankDBHelper(AddPiggybank.this, dbname, null, version);
                        db = piggybankhelper.getWritableDatabase();
                        contentValues = piggybankhelper.makepiggybank(title, amount, date, userid);
                        db.insert("PiggyBanklist", null, contentValues);
                        db.close();
                        finish();
                    }
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

    @Override
    public void onBackPressed() {

    }
}
