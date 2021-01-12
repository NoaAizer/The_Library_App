package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;

public class SearchBookActivity extends AppCompatActivity implements View.OnClickListener  {

    private EditText authorEditText, nameEditText;
    private Button search_btn, all_books;
    private Spinner genreSpinner, languageSpinner;
    private Intent searchIntent;
    private static final String TAG = "SearchBook";
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        nameEditText = (EditText) findViewById(R.id.nameOfBook);
        authorEditText = (EditText) findViewById(R.id.nameOfAuthor);
        genreSpinner = findViewById(R.id.genre);
        genreSpinner.setAdapter(new ArrayAdapter<BookObj.Genre>(this, android.R.layout.simple_spinner_item, BookObj.Genre.values()));;
        languageSpinner = findViewById(R.id.language);
        languageSpinner.setAdapter(new ArrayAdapter<BookObj.Lang>(this, android.R.layout.simple_spinner_item, BookObj.Lang.values()));;
        all_books = (Button) findViewById(R.id.all_books);
        all_books.setOnClickListener(this);
        search_btn = (Button) findViewById(R.id.but_search);
        search_btn.setOnClickListener(this);
        if( getIntent().getExtras().getString("type").equals("user")) {
            searchIntent = new Intent(SearchBookActivity.this, SearchResultActivity.class);
        }
        else{
            searchIntent = new Intent(SearchBookActivity.this, SearchResAdminActivity.class);
        }



    }//onCreate


    @Override
    public void onClick(View v) {
        if(v == all_books){
            searchIntent.putExtra("allBooks", true);
            searchIntent.putExtra("type", getIntent().getExtras().getString("type"));
            startActivity(searchIntent);
        }
        if (v == search_btn){
            String nameOfBook = nameEditText.getText().toString().trim();
            String nameOfAuthor = authorEditText.getText().toString().trim();
            String language = languageSpinner.getSelectedItem().toString().trim();
            String genre = genreSpinner.getSelectedItem().toString().trim();

            if (nameOfBook.isEmpty() && nameOfAuthor.isEmpty() && genre.equals("ז'אנר") && language.equals("שפה")) {
                Toast.makeText(getApplicationContext(), "עליך למלא שדה אחד לפחות", Toast.LENGTH_LONG).show();
                return;
            }
            if (!nameOfBook.isEmpty())
                searchIntent.putExtra("bookName", nameOfBook);
            if (!nameOfAuthor.isEmpty())
                searchIntent.putExtra("author", nameOfAuthor);
            if (!genre.equals("ז'אנר"))
                searchIntent.putExtra("genre", genre);
            if (!language.equals("שפה")) {
                searchIntent.putExtra("language", language);
            }
            searchIntent.putExtra("type", getIntent().getExtras().getString("type"));
            startActivity(searchIntent);
        }

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
