package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button user_btn, admin_btn;
    Button check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_btn = (Button) findViewById(R.id.user);
        admin_btn = (Button) findViewById(R.id.admin);

        user_btn.setOnClickListener(this);
        admin_btn.setOnClickListener(this);
    }
    public void onClick(View v){
        if(v==user_btn){
            Intent intent = new Intent(MainActivity.this, LoginUserActivity.class);
            startActivity(intent);
        }
        if(v==admin_btn){
            Intent intent = new Intent(MainActivity.this, LoginAdminActivity.class);
            startActivity(intent);
        }
    }

    private void goToSecondActivity() {
        Intent intent = new Intent(this, LoginUserActivity.class);
        startActivity(intent);
    }



}