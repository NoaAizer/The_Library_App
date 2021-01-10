package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.adapters.ReturnListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReturnBookActivity extends AppCompatActivity {
    ArrayList<Pair<String,String>> bookList = new ArrayList<>(); // key= orderID , value=bookID
    String thisUserTZ;
    ReturnListAdapter returnListAdapter;
    ListView ordersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        ordersListView = (ListView) findViewById(R.id.listRB);
        Intent intent = getIntent();
        thisUserTZ = intent.getExtras().getString("userTZ");
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("orders");
        myRef.orderByChild("userTZ").equalTo(thisUserTZ).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if(userSnapshot.child("arrivedToUser").getValue().equals(true))
                    for (DataSnapshot bookSnapshot : userSnapshot.child("listOfBooks").getChildren()) {
                        bookList.add(new Pair(userSnapshot.getKey(), bookSnapshot.getValue(String.class)));
                    }

                }
                if (bookList == null || bookList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "אין הזמנות ברשימה", Toast.LENGTH_LONG).show();
                    return;
                }

                returnListAdapter = new ReturnListAdapter(ReturnBookActivity.this, R.layout.single_book_return_row, bookList);
                ordersListView.setAdapter(returnListAdapter);


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