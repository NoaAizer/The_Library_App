package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.adapters.BookAdminAdapter;
import com.example.thelibrary.activities.adapters.BookUserAdapter;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    ArrayList<BookObj> books = new ArrayList<>();
    private TextView searchInfo;
    private CheckBox inStock;
    private String userType;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        inStock = (CheckBox) findViewById(R.id.inStockCheck);
        inStock.setOnCheckedChangeListener(this);
        searchInfo = (TextView) findViewById(R.id.searchInfo);
        Map<String,String> search= new HashMap<>();
        Intent intent = getIntent();

        Boolean allBooks =  intent.getExtras().getBoolean("allBooks");
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
        }
        DatabaseReference booksRef = new FireBaseDBBook().getBookListRef();
        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(allBooks)
                {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        books.add(data.getValue(BookObj.class));
                    }
                }
                else {
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
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        initializeAdapter(books);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ArrayList<BookObj> filtered = new ArrayList<>();
            if(inStock.isChecked()) {
                for (BookObj b : books)
                    if (b.getAmount() != 0)
                        filtered.add(b);
                initializeAdapter(filtered);

            }
        else{
             initializeAdapter(books);
        }

    }

    private void initializeAdapter(ArrayList<BookObj> books){
        ListView lvBook = findViewById(R.id.resultList);

        // Initialize adapter and set adapter to list view
        if(userType.equals("user")) {
            BookUserAdapter bookAd = new BookUserAdapter(this, SearchResultActivity.this, books);
            lvBook.setAdapter(bookAd);
        }
        else{
            BookAdminAdapter bookAd = new BookAdminAdapter(this, SearchResultActivity.this, books);
            lvBook.setAdapter(bookAd);
        }
    }
}