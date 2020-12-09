package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;

public class BooksListAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list_admin);


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        ListView list = (ListView) findViewById(R.id.listView); ///פה צריך ליצור רשימה של כל הספרים ואז להוסיף לadapter סתם עשיתי דוגמא של 1,2,3
        adapter.add("one");
        adapter.add("two");
        adapter.add("three");

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() { // פה אפשר להוסיף קישור לפרטים של הספר ברשימה
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BooksListAdminActivity.this, MenuAdminActivity.class);//########neew to be changed to book details
                startActivity(intent);
            }
        });
    }
}