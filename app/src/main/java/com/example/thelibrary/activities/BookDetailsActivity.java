package com.example.thelibrary.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BookDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name, author, genre, year, lan, amount, briefTxt;
    private Button brief,submit;
    private ImageView image;
    private Dialog d;
    private FloatingActionButton back;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        back = (FloatingActionButton) findViewById(R.id.backToUserMenu);
        name = (TextView) findViewById(R.id.hedBookName);
        author = (TextView) findViewById(R.id.detBookAuthor);
        genre = (TextView) findViewById(R.id.detBookGenre);
        year = (TextView) findViewById(R.id.detBookYear);
        lan = (TextView) findViewById(R.id.detBookLan);
        amount = (TextView) findViewById(R.id.detBookAmount);
        image = (ImageView) findViewById(R.id.bookImageDet);
        brief = (Button) findViewById((R.id.detBookBrief));
        ratingBar = findViewById((R.id.ratingBar));
        submit = findViewById((R.id.bt_submit));
        brief.setOnClickListener(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = String.valueOf(ratingBar.getRating());
                Toast.makeText(getApplicationContext(),s+ "Star", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        String bookID = intent.getExtras().getString("bookID");

        DatabaseReference bookRef = new FireBaseDBBook().getBookFromDB(bookID);
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // System.out.println( dataSnapshot.child("name").getValue(String.class)+"");
                name.setText(dataSnapshot.child("name").getValue(String.class));
                author.setText(dataSnapshot.child("author").getValue(String.class));
                genre.setText(dataSnapshot.child("genre").getValue(String.class));
                year.setText(dataSnapshot.child("publishing_year").getValue(String.class));
                lan.setText(dataSnapshot.child("language").getValue(String.class));
          //      brief.setText(dataSnapshot.child("brief").getValue(String.class));
                amount.setText(dataSnapshot.child("amount").getValue(Long.class).toString());
                StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
                StorageReference ref = mImageStorage.child(dataSnapshot.child("imageURL").getValue(String.class));
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(BookDetailsActivity.this).load(uri).into(image);
                            } }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        back.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == back) {
            finish();
        }
        if(v==brief){
            createBriefDialog();
        }
    }
    public void createBriefDialog(){
        Dialog d= new Dialog(this);
        d.setContentView(R.layout.brief_dialog);
        d.setTitle("תקציר");
        d.setCancelable(true);
        TextView brieftxt= d.findViewById(R.id.all_brief);
        Intent intent = getIntent();
        String bookID = intent.getExtras().getString("bookID");
        DatabaseReference bookRef = new FireBaseDBBook().getBookFromDB(bookID);
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               brieftxt.setText(dataSnapshot.child("brief").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        d.show();

    }
}
