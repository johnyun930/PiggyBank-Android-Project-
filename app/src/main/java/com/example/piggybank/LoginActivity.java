package com.example.piggybank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    int version = 1;
    PiggyBankDBHelper loginHelper;
    SQLiteDatabase database;
    final String dbname = "piggybank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText userId = findViewById(R.id.idtxt);
        final EditText userpassword = findViewById(R.id.passwordtxt);
        Button loginbtn = findViewById(R.id.loginButton);
        TextView signup = findViewById(R.id.signuptxt);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = userId.getText().toString();
                String password = userpassword.getText().toString();
                if(id.equals("")||password.equals("")){
                    Toast.makeText(LoginActivity.this,"Please enter your ID OR password",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        database = openOrCreateDatabase(dbname, MODE_PRIVATE,null);
                        loginHelper = new PiggyBankDBHelper(LoginActivity.this, dbname, null, version);
                        loginHelper.onCreate(database);

                        database = loginHelper.getReadableDatabase();
                        boolean loginresult = loginHelper.login(database, id, password);
                        database.close();

                        if (loginresult) {
                            Toast.makeText(LoginActivity.this,"Login success",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, PiggybankList.class);
                            SharedPreferences sp = getSharedPreferences("piggybank", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("userid", id);
                            editor.remove("adapteritem");
                            editor.commit();
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.rightin_activity,R.anim.not_move_activity);
                        } else {
                            Toast.makeText(LoginActivity.this, "Check you id or password, or you do not have an account yet", Toast.LENGTH_LONG).show();
                        }
                    }catch(Exception ex){
                        Toast.makeText(LoginActivity.this, "There is wrong with DB", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);

            }
        });

    }
}
