package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;

public class AddBookActivity extends AppCompatActivity {

        private EditText authorEditText, briefEditText, genreEditText, languageEditText, publishing_yearEditText, nameEditText;
        private Button add_btn;
        private BookObj book;
        FireBaseDBBook fbb = new FireBaseDBBook();
        private static final String TAG = "AddBook";
        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        nameEditText = (EditText) findViewById(R.id.BookName);
        authorEditText = (EditText) findViewById(R.id.Author);
        briefEditText = (EditText) findViewById(R.id.Brief);
        genreEditText = (EditText) findViewById(R.id.Genre);
        languageEditText = (EditText) findViewById(R.id.Language);
        publishing_yearEditText = (EditText) findViewById(R.id.Publishing_year);

        add_btn = (Button) findViewById(R.id.add);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == add_btn) {
                    String bookName = nameEditText.getText().toString().trim();
                    String author = authorEditText.getText().toString().trim();
                    String brief = briefEditText.getText().toString().trim();
                    String genre = genreEditText.getText().toString().trim();
                    String language = languageEditText.getText().toString().trim();
                    String publishing_year = publishing_yearEditText.getText().toString().trim();

                    if (TextUtils.isEmpty(bookName) || TextUtils.isEmpty(author) ||
                            TextUtils.isEmpty(brief)||TextUtils.isEmpty(genre)||
                            TextUtils.isEmpty(language)||TextUtils.isEmpty(publishing_year)) {
                        Toast.makeText(getApplicationContext(), "One of the fields is missing", Toast.LENGTH_LONG).show();
                        return;
                    }
                    fbb.addBookToDB(bookName, author, brief, genre, language, publishing_year,AddBookActivity.this);
                    Intent addIntent = new Intent(AddBookActivity.this, MenuAdminActivity.class);
                    startActivity(addIntent);

                }
            }
        });
    }//onCreate


    }
