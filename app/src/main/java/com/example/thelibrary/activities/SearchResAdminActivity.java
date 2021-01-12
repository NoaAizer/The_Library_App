package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.adapters.BookAdminAdapter;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchResAdminActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    ArrayList<BookObj> books = new ArrayList<>();
    private TextView searchInfo;
    private ListView lvBook;
    private CheckBox inStock, notInStock;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_res_admin);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        inStock = (CheckBox) findViewById(R.id.inStockCheck);
        inStock.setOnCheckedChangeListener(this);
        notInStock = (CheckBox) findViewById(R.id.notInStockCheck);
        notInStock.setOnCheckedChangeListener(this);
        searchInfo = (TextView) findViewById(R.id.searchInfo);
        lvBook = (ListView) findViewById(R.id.resultList);
        Map<String, String> search = new HashMap<>();
        Intent intent = getIntent();

        Boolean allBooks = intent.getExtras().getBoolean("allBooks");
        userType = intent.getExtras().getString("type");
        if (!allBooks) {
            String lan = intent.getExtras().getString("language");
            if (intent.getExtras().getString("language") != null) search.put("language", lan);
            String gen = intent.getExtras().getString("genre");
            if (gen != null) search.put("genre", gen);
            String auth = intent.getExtras().getString("author");
            if (auth != null) search.put("author", auth);
            String name = intent.getExtras().getString("bookName");
            if (name != null) search.put("name", name);

            boolean first = true;
            for (String category : search.values()) {
                if (first) {
                    searchInfo.append(category);
                    first = false;
                } else searchInfo.append(", " + category);

            }
        } else {
            searchInfo.append(" הכל");
        }
        DatabaseReference booksRef = new FireBaseDBBook().getBookListRef();
        ArrayList<BookObj> filtered = new ArrayList<>();
        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (allBooks) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        books.add(data.getValue(BookObj.class));
                    }
                } else {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        int counter = 0;
                        for (String category : search.keySet()) {
                            if (data.child(category).getValue().toString().contains(search.get(category))) {
                                counter++;
                                if (counter == search.size())
                                    books.add(data.getValue(BookObj.class));
                            }
                        }
                    }
                }
                if (books == null || books.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "אין ספרים להצגה", Toast.LENGTH_LONG).show();
                    return;
                }
                initializeAdapter(books);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ArrayList<BookObj> filtered = new ArrayList<>();
        if (inStock.isChecked()) {
            for (BookObj b : books)
                if (b.getAmount() != 0)
                    filtered.add(b);
            initializeAdapter(filtered);

        }
        else if (inStock.isChecked()){
            for (BookObj b : books)
                if (b.getAmount() == 0)
                    filtered.add(b);
            initializeAdapter(filtered);
        }
        else {
            initializeAdapter(books);
        }
    }

    private void initializeAdapter(ArrayList<BookObj> books) {
        // Initialize adapter and set adapter to list view
        BookAdminAdapter bookAd = new BookAdminAdapter(this, SearchResAdminActivity.this, books);
        lvBook.setAdapter(bookAd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menuBack) {
            finish();
        }
        if (id == R.id.menuBackToHome) {
            Intent menu = new Intent(SearchResAdminActivity.this, MenuAdminActivity.class);
            startActivity(menu);
        }

        return super.onOptionsItemSelected(item);
    }
}