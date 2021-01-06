package com.example.thelibrary.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private StorageReference mStorageRef;
    public Uri imguri;
    private String imageURL;
    private StorageTask uploadTask;
    FireBaseDBBook fbb = new FireBaseDBBook();
    private static final String TAG = "AddBook";
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        nameEditText = (EditText) findViewById(R.id.BookName);
        authorEditText = (EditText) findViewById(R.id.Author);
        briefEditText = (EditText) findViewById(R.id.Brief);
        genreSpinner = findViewById(R.id.Genre);
        genreSpinner.setAdapter(new ArrayAdapter<BookObj.Genre>(this, android.R.layout.simple_spinner_item, BookObj.Genre.values()));;
        languageSpinner = findViewById(R.id.Language);
        languageSpinner.setAdapter(new ArrayAdapter<BookObj.Lang>(this, android.R.layout.simple_spinner_item, BookObj.Lang.values()));;
        publishing_yearEditText = (EditText) findViewById(R.id.Publishing_year);
        amountEditText = (EditText) findViewById(R.id.amount);
        bookImage =findViewById(R.id.bookImage);
        add_btn = (Button) findViewById(R.id.add);
        add_btn.setOnClickListener(this);
        addImage_btn = (Button) findViewById(R.id.addImage);
        addImage_btn.setOnClickListener(this);

    }//onCreate

    public void onClick(View view) {
        if(view == addImage_btn){
            FileChooser();
        }
        if (view == add_btn) {
            fileUploader();
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
//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
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
    }
    }


}