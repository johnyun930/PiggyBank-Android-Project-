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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ExpenseBarChart extends Fragment {

        ArrayList<SavingInfo> savinginfos;
        SQLiteDatabase sqLiteDatabase;
        PiggyBankDBHelper piggyBankDBHelper;
        final String dbname = "piggybank";
        final int version = 1;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



            View view = inflater.inflate(R.layout.activity_fragment_c,container,false);
            FloatingActionButton piechartbtn = view.findViewById(R.id.piechartbtn1);
            FloatingActionButton barchartbtn = view.findViewById(R.id.barchartbtn1);

            View include = view.findViewById(R.id.bartoolbar);
            Toolbar toolbar = include.findViewById(R.id.customtoolbar);
            toolbar.setTitle("GRAPH");
            toolbar.setTitleTextColor(Color.WHITE);
            setHasOptionsMenu(true);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            BarChart barChart = view.findViewById(R.id.barChart);
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            final ArrayList<String> category = new ArrayList<>();
            SharedPreferences sp = this.getActivity().getSharedPreferences("piggybank",getContext().MODE_PRIVATE);
            int id= sp.getInt("piggybankid",0);
            sqLiteDatabase = getContext().openOrCreateDatabase(dbname,getActivity().MODE_PRIVATE,null);
            piggyBankDBHelper = new PiggyBankDBHelper(getContext(),dbname,null,version);
            sqLiteDatabase = piggyBankDBHelper.getReadableDatabase();

            piggyBankDBHelper.selectforexpensebarchart(sqLiteDatabase,id,barEntries,category);
            BarDataSet barDataset = new BarDataSet(barEntries,"Category");
            BarData barData = new BarData(barDataset);
            barData.setValueTextSize(15f);
            barData.setBarWidth(0.9f);
            IAxisValueFormatter formatter =  new IAxisValueFormatter(){
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return category.get((int)value);
                }

            };
            Description description = new Description();
            description.setText("");
            barChart.setDescription(description);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(formatter);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(false);
            xAxis.setTextSize(18f);
            xAxis.setTextColor(Color.BLACK);



            barDataset.setColors(ColorTemplate.JOYFUL_COLORS);
            barChart.setData(barData);
            barChart.setFitBars(true);
            barChart.invalidate();
            barChart.animateY(1500);





            piechartbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getActivity()).replaceFragmnet(new ExpensePiechart());
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
                    ((MainActivity)getActivity()).replaceFragmnet(new IncomeBarChart());
                    return true;

                case R.id.Expense:
                    ((MainActivity)getActivity()).replaceFragmnet(new ExpenseBarChart());
                    return true;

            }
            return super.onOptionsItemSelected(item);
        }

    }



