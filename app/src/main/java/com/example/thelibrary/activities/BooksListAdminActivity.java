package com.example.thelibrary.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BooksListAdminActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lvBook;
    ArrayList<String> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list_admin);

        DatabaseReference booksRef = new FireBaseDBBook().getBookListRef().child("name");
        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String bookName = data.getValue(String.class);
                    books.add(bookName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        lvBook = findViewById(R.id.lvBook);
        ArrayAdapter<String> bookAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,books);
        lvBook.setAdapter(bookAdapter);
        lvBook.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //String book = parent.getItemIdAtPosition(position).toString();
        Toast.makeText(getApplicationContext(),"Clicked: "+books, Toast.LENGTH_SHORT).show();
    }
}