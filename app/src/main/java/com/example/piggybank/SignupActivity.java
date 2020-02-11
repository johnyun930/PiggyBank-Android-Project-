package com.example.piggybank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    final String dbname = "piggybank";
    PiggyBankDBHelper loginHelper;
    SQLiteDatabase database;
    int version = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText id = findViewById(R.id.signupId);
        final EditText password = findViewById(R.id.signupPassword);
        final EditText email = findViewById(R.id.signupEmail);
        final EditText phone = findViewById(R.id.signupPhone);
        TextView tologintext = findViewById(R.id.tologintxt);
        Button signupbtn = findViewById(R.id.signupButton);

        tologintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean createresult = false;
                String idtxt = id.getText().toString();
                String passwordtxt = password.getText().toString();
                String emailtxt = email.getText().toString();
                String phonetxt = phone.getText().toString();
                boolean emailcheck = Patterns.EMAIL_ADDRESS.matcher(emailtxt).matches();
                boolean idcheck = Pattern.matches("^[a-zA-Z]\\w{5,20}$",idtxt);
                boolean phonecheck = Pattern.matches("^\\d{10}$",phonetxt);
                boolean passwordcheck = Pattern.matches("\\w{5,20}",passwordtxt);
                if (idtxt.equals("") || passwordtxt.equals("") || emailtxt.equals("") || phonetxt.equals("")) {
                    Toast.makeText(SignupActivity.this, "Please fill all the information please", Toast.LENGTH_SHORT).show();
                }else if(!idcheck){
                   Toast.makeText(SignupActivity.this, "Id need to start with alphabet and less than 20 character", Toast.LENGTH_SHORT).show();
                } else if(!passwordcheck){
                    Toast.makeText(SignupActivity.this, "Password length is between 5 to 20", Toast.LENGTH_SHORT).show();

                }else if(!emailcheck){
                    Toast.makeText(SignupActivity.this, "please check your email", Toast.LENGTH_SHORT).show();

                }else if(!phonecheck) {
                    Toast.makeText(SignupActivity.this, "You do not need to use '-', 10 numbers only ", Toast.LENGTH_SHORT).show();

                } else {

                    database = openOrCreateDatabase(dbname, MODE_PRIVATE, null);
                    loginHelper = new PiggyBankDBHelper(SignupActivity.this, dbname, null, version);
                    loginHelper.onCreate(database);
                    database = loginHelper.getReadableDatabase();
                    boolean multipleresult = loginHelper.multipleuser(database, idtxt, emailtxt, phonetxt);

                    if (multipleresult) {
                        Toast.makeText(SignupActivity.this, "You already have the account", Toast.LENGTH_SHORT).show();
                    } else {
                        database = loginHelper.getWritableDatabase();
                        ContentValues contentValues = loginHelper.makeuser(idtxt, passwordtxt, emailtxt, phonetxt);
                        ArrayList<String> incomecategory = new ArrayList<>();
                        ArrayList<String> outcomecategory = new ArrayList();

                        incomecategory.add("Wage");
                        incomecategory.add("Fund");
                        incomecategory.add("Bank");
                        incomecategory.add("ETC");
                        outcomecategory.add("Food");
                        outcomecategory.add("Restaurant");
                        outcomecategory.add("Utility");
                        outcomecategory.add("Rent");
                        long result = database.insert("User", null, contentValues);
                            for(int i =0; i<incomecategory.size();i++){
                                ContentValues contentValues1 = new ContentValues();
                                ContentValues contentValues2 = new ContentValues();
                                contentValues1 = loginHelper.makeCategory(incomecategory.get(i),0,idtxt);
                                contentValues2 = loginHelper.makeCategory(outcomecategory.get(i),1,idtxt);
                                database.insert("Category",null,contentValues1);
                                database.insert("Category",null,contentValues2);
                            }
                        database.close();
                        if (result != -1) {
                            Toast.makeText(SignupActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                            finish();

                        }else{
                            Toast.makeText(SignupActivity.this, "Fail to Sign up, try again", Toast.LENGTH_SHORT).show();
                        }
                        //database.execSQL(query);

                    }
                }
//                    if(createresult){
//                        finish();
//                    }

//                }
            }
            });
        }

    @Override
    public void onBackPressed() {
        finish();
    }
}


