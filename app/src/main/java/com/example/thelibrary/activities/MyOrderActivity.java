package com.example.thelibrary.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.google.firebase.database.DatabaseReference;


public class MyOrderActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    //mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        loadBooks();
    }

    public void loadBooks(int len)
    {

    }
}
