package com.example.piggybank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    ArrayList<String> incomeList;
    ArrayList<String> expenseList;
    int version = 1;
    PiggyBankDBHelper piggyBankDBHelper;
    SQLiteDatabase database;
    final String dbname = "piggybank";
    AdapterforOption adapterforOption;
    ArrayList<Categoryinfo> categoryinfos ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

    }

    @Override
    protected void onResume() {
        super.onResume();
        incomeList = new ArrayList<>();
        expenseList = new ArrayList<>();
        categoryinfos = new ArrayList<>();
        final EditText newcategory = findViewById(R.id.editText);
        Button makebutton = findViewById(R.id.categorymakebtn);
        Button cancelbutton = findViewById(R.id.categorycancelbtn);
        ListView listView = findViewById(R.id.listview);
        final SharedPreferences sp = getSharedPreferences("piggybank",MODE_PRIVATE);
        final int category = sp.getInt("category",0);
        final String userid = sp.getString("userid","");
        database = openOrCreateDatabase(dbname,MODE_PRIVATE,null);
        piggyBankDBHelper  = new PiggyBankDBHelper(CategoryActivity.this,dbname,null,version);
        database = piggyBankDBHelper.getReadableDatabase();
        piggyBankDBHelper.readCategryinfo(database,categoryinfos,userid);
        piggyBankDBHelper.readCategryonly(database,incomeList,expenseList,userid);

        if(category == 0){
            adapterforOption = new AdapterforOption(this,incomeList);
        }else{
            adapterforOption = new AdapterforOption(this,expenseList);
        }

        listView.setAdapter(adapterforOption);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("adapteritem",adapterView.getAdapter().getItem(i).toString());
                editor.commit();
                database.close();
                finish();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int id =0;
                   for(int k =0; k<categoryinfos.size();k++){
                       if(adapterView.getAdapter().getItem(i).equals(categoryinfos.get(k).getCategory())&&category==categoryinfos.get(k).getCategorytype()){
                           id = categoryinfos.get(k).getCategoryid();

                           break;
                       }
                   }


                       if(category==0){
                           show(database,id,incomeList,i,adapterforOption);
                       }else{
                           show(database,id,expenseList,i,adapterforOption);
                       }



                return true;
            }
        });

        makebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newcategory.getText().toString();
                boolean multiple = false;
                ArrayList<String> copy;
                if(category==0){
                    copy = incomeList;
                }else{
                    copy = expenseList;
                }
                for(int i=0;i<copy.size();i++){
                    if(text.equals(copy.get(i))){
                        multiple = true;
                    }
                }
                if(multiple){
                    Toast.makeText(CategoryActivity.this,"Same Category is in the list",Toast.LENGTH_SHORT).show();
                }else if(text.equals("")){
                    Toast.makeText(CategoryActivity.this,"Write the category",Toast.LENGTH_SHORT).show();
                }else
                {
                    database = piggyBankDBHelper.getWritableDatabase();
                    ContentValues contentValues = piggyBankDBHelper.makeCategory(text,category,userid);
                 float result = database.insert("Category",null,contentValues);
                 if(result !=-1){
                     Toast.makeText(CategoryActivity.this,"Category is added",Toast.LENGTH_SHORT).show();
                    if(category == 0) {
                        incomeList.add(text);
                    }else{
                        expenseList.add(text);
                    }
                    newcategory.setText("");
                 }

                 adapterforOption.notifyDataSetChanged();
                }
                }
        });

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.close();
                finish();
            }
        });



    }

    public  void show(final SQLiteDatabase sqLiteDatabase, final int id, final ArrayList<String> array,final int index, final AdapterforOption option){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Prompt");
        builder.setMessage("Do you want to Delete this category?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        piggyBankDBHelper.deletecategory(sqLiteDatabase,id);
                        array.remove(index);
                        option.notifyDataSetChanged();
                    }
                }
        );
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();


    }

    @Override
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
