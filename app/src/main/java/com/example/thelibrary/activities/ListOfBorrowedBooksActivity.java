package com.example.thelibrary.activities;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.adapters.BorrowListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfBorrowedBooksActivity extends AppCompatActivity {

    ArrayList<Pair<String,String>> bookList = new ArrayList<>(); // key= orderID , value=bookID
    String thisUserID;
    BorrowListAdapter borrowListAdapter;
    ListView borrowListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_borrowed_books);

        borrowListView = (ListView) findViewById(R.id.listBB);
        thisUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("orders");
        myRef.orderByChild("userID").equalTo(thisUserID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot bookSnapshot : userSnapshot.child("listOfBooks").getChildren()) {
                        bookList.add(new Pair(userSnapshot.getKey(), bookSnapshot.getValue(String.class)));
                    }

                }
                if (bookList == null || bookList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "אין הזמנות ברשימה", Toast.LENGTH_LONG).show();
                    return;
                }

                borrowListAdapter = new BorrowListAdapter(ListOfBorrowedBooksActivity.this, R.layout.single_book_borrow_row, bookList);
                borrowListView.setAdapter(borrowListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
