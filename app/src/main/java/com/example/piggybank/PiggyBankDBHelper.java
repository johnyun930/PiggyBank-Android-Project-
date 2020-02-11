package com.example.piggybank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class PiggyBankDBHelper extends SQLiteOpenHelper {

        final String createquery = "CREATE TABLE IF NOT EXISTS User (userid TEXT PRIMARY KEY , password TEXT, email TEXT, phone TEXT);";
        final String queryforpiggybank = "CREATE TABLE IF NOT EXISTS PiggyBanklist (piggybank_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "title TEXT NOT NULL, amount double NOT NULL, duedate date NOT NULL, userid TEXT NOT NULL,FOREIGN KEY(userid) REFERENCES User(userid) ON DELETE CASCADE);";
        final String queryfordata = "CREATE TABLE IF NOT EXISTS SavingTable (saving_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "moneyamount double Not null, category INT NOT NULL,categorydescription TEXT NOT NuLL, savingdate TEXT NOT NULL, description TEXT, piggybank_id INTEGER NOT NULL, " +
            "FOREIGN KEY(piggybank_id) REFERENCES PiggyBanklist(piggybank_id) ON DELETE CASCADE);";
        final String queryforcategory = "CREATE TABLE IF NOT EXISTS Category (Category_id INTEGER PRIMARY KEY AUTOINCREMENT, Category TEXT NOT NULL, CategoryType int NOT NULL, userid TEXT NOT NULL, "+
                "FOREIGN KEY(userid) REFERENCES User(userid) ON DELETE CASCADE);";

        public PiggyBankDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createquery);
        sqLiteDatabase.execSQL(queryforpiggybank);
        sqLiteDatabase.execSQL(queryfordata);
        sqLiteDatabase.execSQL(queryforcategory);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void readbanks(SQLiteDatabase sqLiteDatabase, ArrayList<PiggybankInfo> info, String userid) {
    try {
        String query = "SELECT * FROM PiggyBanklist WHERE userid = ? ORDER BY duedate ASC;";
        Cursor cursor = null;
        cursor = sqLiteDatabase.rawQuery(query, new String[]{userid});

        while (cursor.moveToNext()) {
            PiggybankInfo idinfo = new PiggybankInfo();
            idinfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            idinfo.setDate(cursor.getString(cursor.getColumnIndex("duedate")));
            idinfo.setAmout(cursor.getDouble(cursor.getColumnIndex("amount")));
            idinfo.setBankid(cursor.getInt(cursor.getColumnIndex("piggybank_id")));
            info.add(idinfo);
        }
        cursor.close();
    }catch (Exception ex){

    }
    }

    public String readduedate(SQLiteDatabase sqLiteDatabase,  int piggybank_id) {
        String duedate = "";
        try {
            String query = "SELECT duedate FROM PiggyBanklist WHERE piggybank_id = ?;";
            Cursor cursor = null;
            cursor = sqLiteDatabase.rawQuery(query, new String[]{Integer.toString(piggybank_id)});

            while (cursor.moveToNext()) {

               duedate = cursor.getString(cursor.getColumnIndex("duedate"));

            }
            cursor.close();

        }catch (Exception ex){

        }
        return duedate;
    }

    public void readAllSavinginfo(SQLiteDatabase sqLiteDatabase, ArrayList<SavingInfo> info, int bankid) {
        try {
            String query = "SELECT * FROM SavingTable WHERE piggybank_id = ? ORDER BY savingdate ASC;";
            Cursor cursor = null;
            cursor = sqLiteDatabase.rawQuery(query, new String[]{Integer.toString(bankid)});

            while (cursor.moveToNext()) {
                SavingInfo savinginfo = new SavingInfo();
                savinginfo.setSavingid(cursor.getInt(cursor.getColumnIndex("saving_id")));
                savinginfo.setCategory(cursor.getInt(cursor.getColumnIndex("category")));
                savinginfo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                savinginfo.setCategorydescription(cursor.getString(cursor.getColumnIndex("categorydescription")));
                savinginfo.setDate(cursor.getString(cursor.getColumnIndex("savingdate")));
                savinginfo.setMoneyamout(cursor.getDouble(cursor.getColumnIndex("moneyamount")));
                info.add(savinginfo);
            }
            cursor.close();
        }catch (Exception ex){

        }
    }


    public void readCategryinfo(SQLiteDatabase sqLiteDatabase, ArrayList<Categoryinfo> info, String userid) {
    try {
        String query = "SELECT * FROM Category WHERE userid = ?;";
        Cursor cursor = null;
        cursor = sqLiteDatabase.rawQuery(query, new String[]{userid});

        while (cursor.moveToNext()) {
            Categoryinfo categoryinfo = new Categoryinfo();
            categoryinfo.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
            categoryinfo.setCategoryid(cursor.getInt(cursor.getColumnIndex("Category_id")));
            categoryinfo.setCategorytype(cursor.getInt(cursor.getColumnIndex("CategoryType")));
            categoryinfo.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
            info.add(categoryinfo);
        }

        cursor.close();
    }catch (Exception ex){

    }
    }

    public void readCategryonly(SQLiteDatabase sqLiteDatabase, ArrayList<String> info, ArrayList<String> info2, String userid) {
        try {
            String query = "SELECT * FROM Category WHERE userid=?;";
            Cursor cursor = null;
            cursor = sqLiteDatabase.rawQuery(query, new String[]{userid});

            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex("CategoryType")) == 0) {
                    info.add(cursor.getString(cursor.getColumnIndex("Category")));
                } else {
                    info2.add(cursor.getString(cursor.getColumnIndex("Category")));
                }
            }

            cursor.close();
        } catch (Exception ex) {

        }
    }




    public int readAllSavingamount(SQLiteDatabase sqLiteDatabase, int bankid) {
        double totalamount = 0;
            try {
            String query = "SELECT * FROM SavingTable WHERE piggybank_id=?;";
            Cursor cursor = null;
            cursor = sqLiteDatabase.rawQuery(query, new String[]{Integer.toString(bankid)});

            while (cursor.moveToNext()) {

                if (cursor.getInt(cursor.getColumnIndex("category")) == 1) {
                    totalamount -= cursor.getDouble(cursor.getColumnIndex("moneyamount"));
                } else {
                    totalamount += cursor.getDouble(cursor.getColumnIndex("moneyamount"));
                }
            }

            totalamount = Math.ceil(totalamount);
            cursor.close();

        }catch (Exception ex){

        }
        return (int) totalamount;
    }

    public boolean multipleuser(SQLiteDatabase sqLiteDatabase,String userid,  String email, String phone){
            boolean result = false;
            String query = "SELECT * FROM USER;";
            Cursor cursor = null;
            cursor = sqLiteDatabase.rawQuery(query,null);
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex("userid"));
                String emailaddress = cursor.getString(cursor.getColumnIndex("email"));
                String phonenumber = cursor.getString(cursor.getColumnIndex("phone"));
                if(id.equals(userid)||email.equals(emailaddress)||phonenumber.equals( phone)){
                   result = true;
                    break;
                }

            }
        cursor.close();
            return result;
    }

    public boolean login(SQLiteDatabase sqLiteDatabase,String userid, String password){
         boolean result = false;
            String query = "SELECT * FROM USER WHERE userid = ? AND password = ?";
        Cursor cursor = null;
        cursor = sqLiteDatabase.rawQuery(query,new String[]{userid,password});
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("userid"));
            String passwordvalue = cursor.getString(cursor.getColumnIndex("password"));
            if(id.equals(userid)&&password.equals(passwordvalue)){
                result = true;
                break;
            }
        }
        cursor.close();
        return result;
    }

    public void selectforincomepiechart(SQLiteDatabase sqLiteDatabase, int piggybank_id, ArrayList<PieEntry> pieCharts){
            try {
                String query = "SELECT SUM(moneyamount), categorydescription FROM SavingTable WHERE piggybank_id =? AND category =0 GROUP BY categorydescription;";
                Cursor cursor = null;
                cursor = sqLiteDatabase.rawQuery(query, new String[]{ Integer.toString(piggybank_id)});
                while (cursor.moveToNext()) {
                    float value = (float) cursor.getDouble(cursor.getColumnIndex("SUM(moneyamount)"));
                    String category = cursor.getString(cursor.getColumnIndex("categorydescription"));
                    pieCharts.add(new PieEntry(value, category));

                }
                cursor.close();
            }catch (Exception ex) {

            }

    }


    public void selectforexpensepiechart(SQLiteDatabase sqLiteDatabase, int piggybank_id, ArrayList<PieEntry> pieCharts) {
        try {
            String query = "SELECT SUM(moneyamount), categorydescription FROM SavingTable WHERE piggybank_id =? AND category =1 GROUP BY categorydescription;";
            Cursor cursor = null;
            cursor = sqLiteDatabase.rawQuery(query, new String[]{Integer.toString(piggybank_id)});
            while (cursor.moveToNext()) {
                float value = (float) cursor.getDouble(cursor.getColumnIndex("SUM(moneyamount)"));
                String category = cursor.getString(cursor.getColumnIndex("categorydescription"));
                pieCharts.add(new PieEntry(value, category));

            }
            cursor.close();
        } catch (Exception ex) {

        }
    }

    public void selectforincomebarchart(SQLiteDatabase sqLiteDatabase, int piggybank_id, ArrayList<BarEntry> barCharts,ArrayList<String> categorylist) {
        try {
            String query = "SELECT SUM(moneyamount), categorydescription FROM SavingTable WHERE piggybank_id =? AND category =0 GROUP BY categorydescription;";
            Cursor cursor = null;
            cursor = sqLiteDatabase.rawQuery(query, new String[]{Integer.toString(piggybank_id)});
            int i =0;
            while (cursor.moveToNext()) {
                float value = (float) cursor.getDouble(cursor.getColumnIndex("SUM(moneyamount)"));
                String category = cursor.getString(cursor.getColumnIndex("categorydescription"));
                barCharts.add(new BarEntry(i, value));
                categorylist.add(category);
                i++;

            }
            cursor.close();
        } catch (Exception ex) {

        }
    }

    public void selectforexpensebarchart(SQLiteDatabase sqLiteDatabase, int piggybank_id, ArrayList<BarEntry> barCharts,ArrayList<String> categorylist) {
        try {
            String query = "SELECT SUM(moneyamount), categorydescription FROM SavingTable WHERE piggybank_id =? AND category =1 GROUP BY categorydescription;";
            Cursor cursor = null;
            cursor = sqLiteDatabase.rawQuery(query, new String[]{Integer.toString(piggybank_id)});
            int i =0;
            while (cursor.moveToNext()) {
                float value = (float) cursor.getDouble(cursor.getColumnIndex("SUM(moneyamount)"));
                String category = cursor.getString(cursor.getColumnIndex("categorydescription"));
                barCharts.add(new BarEntry(i, value));
                categorylist.add(category);
                i++;
            }
            cursor.close();
        } catch (Exception ex) {

        }
    }



    public ContentValues makepiggybank (String title, double amount, String date,String userid) {
//        try {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",title);
        contentValues.put("amount",amount);
        contentValues.put("duedate",date);
        contentValues.put("userid",userid);
        return contentValues;
    }

    public ContentValues makeCategory(String category, int categorytype, String userid){

     //Category TEXT NOT NULL, CategoryType int NOT NULL, userid TEXT NOT NUL
            ContentValues contentValues = new ContentValues();
            contentValues.put("Category",category);
            contentValues.put("CategoryType",categorytype);
            contentValues.put("userid",userid);

            return contentValues;
    }

    public ContentValues addoruseMoney (double moneyamount, int category,String categorydescription ,String date,String description,int piggybank_id) {
//        //moneyamount double Not null, category INT NOT NULL,categorydescription TEXT NOT NuLL, savingdate datetime NOT NULL, description TEXT, piggybank_id INTEGER NOT NULL
        ContentValues contentValues = new ContentValues();
        contentValues.put("moneyamount",moneyamount);
        contentValues.put("category",category);
        contentValues.put("categorydescription",categorydescription);
        contentValues.put("savingdate",date);
        contentValues.put("description",description);
        contentValues.put("piggybank_id",piggybank_id);

        return contentValues;
    }

//
        public ContentValues makeuser(String userid, String password, String email, String phone) {
//        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("userid",userid);
            contentValues.put("password",password);
            contentValues.put("email",email);
            contentValues.put("phone", phone);
            return contentValues;
        }



        public void deletebank(SQLiteDatabase sqLiteDatabase,int id){
            String query = "DELETE FROM PiggyBanklist WHERE piggybank_id = " + id + ";";
            sqLiteDatabase.execSQL(query);
        }

        public void deletelist(SQLiteDatabase sqLiteDatabase, int id){
            String query = "DELETE FROM SavingTable WHERE saving_id =" + id +";";
            sqLiteDatabase.execSQL(query);
        }

    public void deletecategory(SQLiteDatabase sqLiteDatabase, int id){
        String query = "DELETE FROM Category WHERE Category_id =" + id +";";
        sqLiteDatabase.execSQL(query);
    }


}
