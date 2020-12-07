package com.example.thelibrary.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.thelibrary.R;

public class MenuUserActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loan_btn,myDetails_btn,myBooks_btn, searchBook_btn;
    private ImageButton editBooks_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loan_btn = (Button)findViewById(R.id.checkALoan);
        myDetails_btn = (Button)findViewById(R.id.userDetails);
        myBooks_btn = (Button)findViewById(R.id.myBooks);
        searchBook_btn = (Button)findViewById(R.id.searchAbook);
        editBooks_btn =(ImageButton) findViewById(R.id.editBooks);

        loan_btn.setOnClickListener(this);
        myDetails_btn.setOnClickListener(this);
        myBooks_btn.setOnClickListener(this);
        searchBook_btn.setOnClickListener(this);
        editBooks_btn.setOnClickListener(this);
    }
    public void onClick(View v) {
        if(v==loan_btn){

        }
        if(v==myDetails_btn){

        }
        if(v==myBooks_btn){

        }
        if(v==searchBook_btn){

        }
    }

}