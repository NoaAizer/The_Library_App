package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.adapters.FavoriteBookAdapter;
import com.example.thelibrary.fireBase.model.FireBaseDBUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavBooksActivity extends AppCompatActivity {


    ArrayList<String> bookList = new ArrayList<>(); // key= orderID , value=bookID
    String thisUserID;
    FavoriteBookAdapter favListAdapter;
    ListView favListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_books);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        favListView = (ListView) findViewById(R.id.favList);
        thisUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("orders");
        new FireBaseDBUser().getUserFromDB(thisUserID).child("favorites").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                            bookList.add(bookSnapshot.getValue(String.class));
                        }
                if (bookList == null || bookList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "אין ספרים ברשימה", Toast.LENGTH_LONG).show();
                    return;
                }

                favListAdapter = new FavoriteBookAdapter(FavBooksActivity.this, R.layout.single_book_fav_row, bookList);
                favListView.setAdapter(favListAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back_home, menu);
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
        if (id == R.id.menuBackToHome) {
            Intent menu = new Intent (FavBooksActivity.this, MenuUserActivity.class);
            startActivity(menu);
        }
        return super.onOptionsItemSelected(item);
    }


}
