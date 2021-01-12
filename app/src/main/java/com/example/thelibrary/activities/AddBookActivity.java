package com.example.thelibrary.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class AddBookActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText authorEditText, briefEditText, publishing_yearEditText, nameEditText, amountEditText;
    private Button add_btn,addImage_btn;
    private ImageView bookImage;
    private Spinner genreSpinner, languageSpinner;
    private NumberPicker amountPicker;
    private StorageReference mStorageRef;
    public Uri imguri;
    private String imageURL;
    private int amount=0;
    private StorageTask uploadTask;
    FireBaseDBBook fbb = new FireBaseDBBook();
    private static final String TAG = "AddBook";
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        mStorageRef = FirebaseStorage.getInstance().getReference();
        nameEditText = (EditText) findViewById(R.id.bookName);
        authorEditText = (EditText) findViewById(R.id.author);
        briefEditText = (EditText) findViewById(R.id.brief);
        genreSpinner = findViewById(R.id.genre);
        genreSpinner.setAdapter(new ArrayAdapter<BookObj.Genre>(this, android.R.layout.simple_spinner_item, BookObj.Genre.values()));;
        languageSpinner = findViewById(R.id.language);
        languageSpinner.setAdapter(new ArrayAdapter<BookObj.Lang>(this, android.R.layout.simple_spinner_item, BookObj.Lang.values()));;
        publishing_yearEditText = (EditText) findViewById(R.id.publishing_year);
        //amountEditText = (EditText) findViewById(R.id.amount);
        amountPicker = (NumberPicker) findViewById(R.id.amountPicker);
        amountPicker.setMinValue(1);
        amountPicker.setMaxValue(100);
        amountPicker.setWrapSelectorWheel(false);
        amountPicker.setOnValueChangedListener(new MyListener());
        bookImage =findViewById(R.id.bookImage);
        add_btn = (Button) findViewById(R.id.add);
        add_btn.setOnClickListener(this);
        addImage_btn = (Button) findViewById(R.id.addBookImage);
        addImage_btn.setOnClickListener(this);

    }//onCreate

    public void onClick(View view) {
        if(view == addImage_btn){
            FileChooser();
        }
        if (view == add_btn) {
            if(imguri==null){
                Toast.makeText(getApplicationContext(), "בחר תמונה עבור הספר", Toast.LENGTH_LONG).show();
                return;

            }
            fileUploader();
            String bookName = nameEditText.getText().toString().trim();
            String author = authorEditText.getText().toString().trim();
            String brief = briefEditText.getText().toString().trim();
            String genre = genreSpinner.getSelectedItem().toString().trim();
            String language = languageSpinner.getSelectedItem().toString().trim();
            String publishing_year = publishing_yearEditText.getText().toString().trim();
            int amount = amountPicker.getValue();
            Toast.makeText(getApplicationContext(), amount+"", Toast.LENGTH_LONG).show();


//            if (amountEditText.getText().toString().trim().isEmpty()){
//                amountEditText.setError("בחר כמות  רצויה");
//                return;
//            }
//            else
//                amount =  Integer.parseInt(amountEditText.getText().toString().trim());
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
                    TextUtils.isEmpty(language)||TextUtils.isEmpty(publishing_year)) {
                Toast.makeText(getApplicationContext(), "One of the fields is missing", Toast.LENGTH_LONG).show();
                return;
            }
            if(publishing_year.length()>4){
                Toast.makeText(getApplicationContext(), "Please correct the publishing year", Toast.LENGTH_LONG).show();
                return;
            }

            fbb.addBookToDB(bookName, author, brief, genre, language, publishing_year, amount,imageURL,AddBookActivity.this);
            Intent addIntent = new Intent(AddBookActivity.this, MenuAdminActivity.class);
            startActivity(addIntent);

        }
    }


    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }



    private void fileUploader(){

        imageURL=System.currentTimeMillis()+"."+getExtension(imguri);
        StorageReference Ref=mStorageRef.child(imageURL);

        Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get a URL to the uploaded content
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Upload imageFailed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode ==RESULT_OK&& data!=null && data.getData()!=null){
            imguri=data.getData();
            Glide.with(AddBookActivity.this).load(imguri).into(bookImage);
        }
    }

    private class MyListener implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            //get new value and convert it to String
            //if you want to use variable value elsewhere, declare it as a field
            //of your main function
            amount= newVal;
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