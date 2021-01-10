package com.example.thelibrary.activities;

import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.adapters.BorrowListAdminAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BorrowListAdminActivity extends AppCompatActivity {

    ArrayList<Pair<Pair<String,String>,String>> bookList = new ArrayList<>(); // key= order , value=bookID
    ListView borrowListView;
    SearchView searchBookView;
   BorrowListAdminAdapter borrowListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_list_admin);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        borrowListView = (ListView) findViewById(R.id.listBB);
        searchBookView = (SearchView) findViewById(R.id.searchBook) ;
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.child("orders").getChildren()) {
                    for (DataSnapshot bookSnapshot : userSnapshot.child("listOfBooks").getChildren()) {
                        if(userSnapshot.child("arrivedToUser").getValue().equals(true)) {
                            String bookName =   dataSnapshot.child("books").child(bookSnapshot.getValue(String.class)).child("name").getValue(String.class);
                            bookList.add(new Pair(new Pair(userSnapshot.getKey(), bookSnapshot.getValue(String.class)), bookName));

                        }
                    }

                }
                if (bookList == null || bookList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "אין ספרים מושאלים", Toast.LENGTH_LONG).show();
                    return;
                }
                Collections.sort(bookList, new Comparator<Pair<Pair<String,String>,String>>() {
                    public int compare(Pair<Pair<String,String>,String> b1, Pair<Pair<String,String>,String> b2) {
                        if (b1.second == null || b2.second == null)
                            return 0;
                        return b1.second.compareTo(b2.second);
                    }
                });
                borrowListAdapter= new BorrowListAdminAdapter(BorrowListAdminActivity.this, R.layout.single_book_borrow_admin, bookList);
                borrowListView.setAdapter(borrowListAdapter);

                searchBookView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        borrowListAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        borrowListAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuBack) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
