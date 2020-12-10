package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.thelibrary.R;

public class MenuAdminActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loansList_btn,booksList_btn,deliveres_btn, clientsList_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loansList_btn = (Button)findViewById(R.id.loansList);
        booksList_btn = (Button)findViewById(R.id.booksList);
        deliveres_btn = (Button)findViewById(R.id.deliveres);
        clientsList_btn = (Button)findViewById(R.id.clientsList);


        loansList_btn.setOnClickListener(this);
        booksList_btn.setOnClickListener(this);
        deliveres_btn.setOnClickListener(this);
        clientsList_btn.setOnClickListener(this);

    }
    public void onClick(View v) {
        if(v==loansList_btn){
            Intent intent = new Intent(MenuAdminActivity.this, TreatmentOrdersActivity.class);
            startActivity(intent);
        }
        if(v==booksList_btn){
            Intent intent = new Intent(MenuAdminActivity.this, BooksListAdminActivity.class);
            startActivity(intent);

        }
        if(v==deliveres_btn){
            Intent intent = new Intent(MenuAdminActivity.this, AddBookActivity.class);
            startActivity(intent);

        }
        if(v==clientsList_btn){

        }
    }

}