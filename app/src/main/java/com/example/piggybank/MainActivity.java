package com.example.piggybank;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private SavingHome fragment_a;

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("piggybank", MODE_PRIVATE);
        int id = sp.getInt("piggybankid", 0);

        fragmentManager = getSupportFragmentManager();
        fragment_a = new SavingHome();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout, fragment_a);
        transaction.commit();

    }


    public void replaceFragmnet(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }







    @Override
    public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(R.anim.not_move_activity,R.anim.rightout_activity);
        }

    }






