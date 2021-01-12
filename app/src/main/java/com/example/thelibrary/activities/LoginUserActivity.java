package com.example.thelibrary.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.mAuthUser;

public class LoginUserActivity extends AppCompatActivity implements View.OnClickListener  {
    private EditText emailEditText, passwordEditText;
    private Button register_now_btn, login, reset_pass_btn;
    private ProgressBar loading;
    mAuthUser auth = new mAuthUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        loading  = (ProgressBar) findViewById(R.id.loading);
        register_now_btn = findViewById(R.id.registUser);
        login = findViewById(R.id.login);
        reset_pass_btn = findViewById(R.id.resetPassword);
        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        reset_pass_btn.setOnClickListener(this);
        register_now_btn.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    protected void onPause() {
        super.onPause();
        loading.setVisibility(View.INVISIBLE);

    }

    public void onClick(View v) {
        if (v == login) {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("חובה להכניס אימייל");

            }
            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("חובה להכניס סיסמה");
            } else {
                auth.validationUser(email, password,LoginUserActivity.this);
                loading.setVisibility(View.VISIBLE);
            }


        }
            if (v == register_now_btn) {
                Intent intent = new Intent(LoginUserActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        if (v == reset_pass_btn) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginUserActivity.this);
            alertDialog.setTitle("שחזור סיסמה");
            alertDialog.setMessage("הכנס את האימייל שלך:");

            final EditText input = new EditText(LoginUserActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setPositiveButton("שחזור",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String email = input.getText().toString();
                            if (email == null||!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_LONG).show();
                                return;
                            }
                                auth.resetPassword(email,LoginUserActivity.this);

                                }});
            alertDialog.setNegativeButton("ביטול",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuBack) {
            finish();
            Intent main = new Intent(LoginUserActivity.this, MainActivity.class);
            startActivity(main);
        }
        return super.onOptionsItemSelected(item);
    }

}


