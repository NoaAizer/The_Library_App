package com.example.thelibrary.activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BooksListUserActivity extends AppCompatActivity {
    ListView lvBook;
    ArrayList<BookObj> books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list_user);


        DatabaseReference booksRef = new FireBaseDBBook().getBookListRef();
        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    books.add(data.getValue(BookObj.class));
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        lvBook = findViewById(R.id.lvBookU);

        // Initialize adapter and set adapter to list view
        BookUserAdapter bookAd = new BookUserAdapter(this, BooksListUserActivity.this, books);
        lvBook.setAdapter(bookAd);
        //bookAd.notifyDataSetChanged();
    }

}