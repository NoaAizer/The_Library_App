package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;

public class AddBookActivity extends AppCompatActivity {

    private EditText authorEditText, briefEditText, publishing_yearEditText, nameEditText, amountEditText;
    private Button add_btn;
    private Spinner genreSpinner, languageSpinner;
    FireBaseDBBook fbb = new FireBaseDBBook();
    private static final String TAG = "AddBook";
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        nameEditText = (EditText) findViewById(R.id.BookName);
        authorEditText = (EditText) findViewById(R.id.Author);
        briefEditText = (EditText) findViewById(R.id.Brief);
        genreSpinner = findViewById(R.id.Genre);
        genreSpinner.setAdapter(new ArrayAdapter<BookObj.Genre>(this, android.R.layout.simple_spinner_item, BookObj.Genre.values()));;
        languageSpinner = findViewById(R.id.Language);
        languageSpinner.setAdapter(new ArrayAdapter<BookObj.Lang>(this, android.R.layout.simple_spinner_item, BookObj.Lang.values()));;
        publishing_yearEditText = (EditText) findViewById(R.id.Publishing_year);
        amountEditText = (EditText) findViewById(R.id.amount);

        add_btn = (Button) findViewById(R.id.add);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == add_btn) {
                    String bookName = nameEditText.getText().toString().trim();
                    String author = authorEditText.getText().toString().trim();
                    String brief = briefEditText.getText().toString().trim();
                    String genre = genreSpinner.getSelectedItem().toString().trim();
                    String language = languageSpinner.getSelectedItem().toString().trim();
                    String publishing_year = publishing_yearEditText.getText().toString().trim();
                    int amount=0;

                    if (amountEditText.getText().toString().trim().isEmpty()){
                        amountEditText.setError("בחר כמות  רצויה");
                        return;
                    }
                    else
                        amount =  Integer.parseInt(amountEditText.getText().toString().trim());
                    if (genre.equals("ז'אנר")){
                        Toast.makeText(getApplicationContext(), "בחר ז'אנר מהרשימה", Toast.LENGTH_LONG).show();
                        return;

                    }
                    if (language.equals("שפה")){
                        Toast.makeText(getApplicationContext(), "בחר שפה מהרשימה", Toast.LENGTH_LONG).show();
                        return;

                    }

                    if (TextUtils.isEmpty(bookName) || TextUtils.isEmpty(author) ||
                            TextUtils.isEmpty(brief)||TextUtils.isEmpty(genre)||
                            TextUtils.isEmpty(language)||TextUtils.isEmpty(publishing_year)||amount==0) {
                        Toast.makeText(getApplicationContext(), "One of the fields is missing", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(publishing_year.length()>4){
                        Toast.makeText(getApplicationContext(), "Please correct the publishing year", Toast.LENGTH_LONG).show();
                        return;
                    }

                    fbb.addBookToDB(bookName, author, brief, genre, language, publishing_year, amount,AddBookActivity.this);
                    Intent addIntent = new Intent(AddBookActivity.this, MenuAdminActivity.class);
                    startActivity(addIntent);

                }
            }
        });
    }//onCreate


}