package com.example.piggybank;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ExpensePiechart extends Fragment {
    ArrayList<SavingInfo> savinginfos;
    SQLiteDatabase sqLiteDatabase;
    PiggyBankDBHelper piggyBankDBHelper;
    final String dbname = "piggybank";
    final int version = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_b,container,false);
        PieChart pieChart = view.findViewById(R.id.piechart);
        FloatingActionButton piechartbtn = view.findViewById(R.id.piechartbtn);
        FloatingActionButton barchartbtn = view.findViewById(R.id.barchartbtn);
        pieChart.setUsePercentValues(true);

        View include = view.findViewById(R.id.include1);
        Toolbar toolbar = include.findViewById(R.id.customtoolbar);
        toolbar.setTitle("GRAPH");
        toolbar.setTitleTextColor(Color.WHITE);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = this.getActivity().getSharedPreferences("piggybank",getContext().MODE_PRIVATE);
        int id= sp.getInt("piggybankid",0);
        sqLiteDatabase = getContext().openOrCreateDatabase(dbname,getActivity().MODE_PRIVATE,null);
        piggyBankDBHelper = new PiggyBankDBHelper(getContext(),dbname,null,version);
        sqLiteDatabase = piggyBankDBHelper.getReadableDatabase();
        ArrayList<PieEntry> val = new ArrayList<>();
        piggyBankDBHelper.selectforexpensepiechart(sqLiteDatabase,id,val);
        Description description = new Description();
        description.setText("Expense");
        description.setTextSize(15);
        pieChart.setDescription(description);

        PieDataSet dataset = new PieDataSet(val,"Category");
        dataset.setValueTextColor(Color.RED);
        dataset.setValueTextSize(20f);
        dataset.setSelectionShift((12));
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataset));
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        barchartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragmnet(new ExpenseBarChart());
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar2,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                ((MainActivity)getActivity()).replaceFragmnet(new SavingHome());

                return true;
            }
            case R.id.tolist:{
                ((MainActivity)getActivity()).replaceFragmnet(new SavingHome());
                return true;
            }
            case R.id.Income:
                ((MainActivity)getActivity()).replaceFragmnet(new IncomePiechart());
                return true;

            case R.id.Expense:
                ((MainActivity)getActivity()).replaceFragmnet(new ExpensePiechart());
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    


    }


