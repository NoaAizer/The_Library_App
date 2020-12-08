package com.example.thelibrary.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.thelibrary.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MenuUserActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loan_btn,myDetails_btn,myBooks_btn, searchBook_btn;
    private ExtendedFloatingActionButton editBooks_btn ,labInf_btn;
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
        editBooks_btn = (ExtendedFloatingActionButton) findViewById(R.id.editBooks);
        labInf_btn = (ExtendedFloatingActionButton) findViewById(R.id.information);

        loan_btn.setOnClickListener(this);
        myDetails_btn.setOnClickListener(this);
        myBooks_btn.setOnClickListener(this);
        searchBook_btn.setOnClickListener(this);
        editBooks_btn.setOnClickListener(this);
        labInf_btn.setOnClickListener(this); //library information
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
        if(v==labInf_btn){

        }
    }

}