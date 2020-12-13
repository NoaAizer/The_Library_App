package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class BookDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name, author, genre, year, lan, brief, amount;
    private FloatingActionButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        back = (FloatingActionButton) findViewById(R.id.backToUserMenu);
        name = (TextView) findViewById(R.id.hedBookName);
        author = (TextView) findViewById(R.id.detBookAuthor);
        genre = (TextView) findViewById(R.id.detBookGenre);
        year = (TextView) findViewById(R.id.detBookYear);
        lan = (TextView) findViewById(R.id.detBookLan);
        brief = (TextView) findViewById(R.id.detBookBrief);
        amount = (TextView) findViewById(R.id.detBookAmount);

        Intent intent = getIntent();
        String bookID = intent.getExtras().getString("bookID");

        DatabaseReference bookRef = new FireBaseDBBook().getBookFromDB(bookID);
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // System.out.println( dataSnapshot.child("name").getValue(String.class)+"");
                name.setText(dataSnapshot.child("name").getValue(String.class));
                author.setText(dataSnapshot.child("author").getValue(String.class));
                genre.setText(dataSnapshot.child("genre").getValue(String.class));
                year.setText(dataSnapshot.child("publishing_year").getValue(String.class));
                lan.setText(dataSnapshot.child("language").getValue(String.class));
                brief.setText(dataSnapshot.child("brief").getValue(String.class));
                amount.setText(dataSnapshot.child("amount").getValue(Long.class).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        back.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == back) {
            finish();
        }
    }
}
