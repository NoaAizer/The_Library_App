package com.example.thelibrary.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.thelibrary.fireBase.model.FireBaseDBUser;
import com.example.thelibrary.fireBase.model.dataObj.UserObj;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name, author, genre, year, lan, amount;
    private Button brief,submit;
    private ImageView image;
    private MaterialFavoriteButton favoriteBookBtn;
    private RatingBar ratingBar;
    private double rating=0;
    FireBaseDBUser fu=new FireBaseDBUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        Intent intent = getIntent();
        String bookID = intent.getExtras().getString("bookID");
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        name = (TextView) findViewById(R.id.hedBookName);
        author = (TextView) findViewById(R.id.detBookAuthor);
        genre = (TextView) findViewById(R.id.detBookGenre);
        year = (TextView) findViewById(R.id.detBookYear);
        lan = (TextView) findViewById(R.id.detBookLan);
        amount = (TextView) findViewById(R.id.detBookAmount);
        image = (ImageView) findViewById(R.id.bookImageDet);
        brief = (Button) findViewById((R.id.detBookBrief));
        favoriteBookBtn = findViewById(R.id.like);
        fu.getUserFromDB(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserObj user = dataSnapshot.getValue(UserObj.class);
                ArrayList<String> favorites=user.getFavorites();
                if(favorites.contains(bookID)) {
                    favoriteBookBtn.setFavorite(true);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ratingBar = findViewById((R.id.ratingBar));
        submit = findViewById((R.id.bt_submit));
        brief.setOnClickListener(this);
        submit.setOnClickListener(this);
        favoriteBookBtn.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        fu.getUserFromDB(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserObj user = dataSnapshot.getValue(UserObj.class);
                                ArrayList<String> favorites=user.getFavorites();
                                if(!favorites.contains(bookID) &&favorite) {
                                    Toast.makeText(getApplicationContext(),"הספר נוסף למועדפים שלי", Toast.LENGTH_SHORT).show();
                                    fu.addToFavorites(bookID);
                                }
                                else if(!favorite && favorites.contains(bookID) )
                                {
                                    new FireBaseDBUser().removeFromFavorites(bookID);
                                    Toast.makeText(getApplicationContext(),"הספר הוסר מהמועדפים שלי", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                });

        DatabaseReference bookRef = new FireBaseDBBook().getBookFromDB(bookID);
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue(String.class));
                author.setText(dataSnapshot.child("author").getValue(String.class));
                genre.setText(dataSnapshot.child("genre").getValue(String.class));
                year.setText(dataSnapshot.child("publishing_year").getValue(String.class));
                lan.setText(dataSnapshot.child("language").getValue(String.class));
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

    }

    public void onClick(View v) {
        if (v == brief) {
            createBriefDialog();
        }
        if (v == submit) {
            Intent intent = getIntent();
            String bookID = intent.getExtras().getString("bookID");
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String s = String.valueOf(ratingBar.getRating());
            Toast.makeText(getApplicationContext(), s + " כוכבים", Toast.LENGTH_SHORT).show();
            rating = Double.parseDouble(s);
            DatabaseReference bookRef = new FireBaseDBBook().getBookFromDB(bookID);
            bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Double currRating = dataSnapshot.child("bookRating").getValue(Double.class);
                    Map<String, Double> usersRating = (Map<String, Double>) dataSnapshot.child("usersRating").getValue();
                    if (usersRating == null) usersRating = new HashMap<>();
                    if (usersRating.containsKey(userID))
                        Toast.makeText(getApplicationContext(), "הדירוג שלך עודכן", Toast.LENGTH_SHORT).show();
                    usersRating.put(userID, rating);
                    bookRef.child("usersRating").setValue(usersRating);
                    Double updatedRating = (currRating + rating) / usersRating.size();
                    bookRef.child("bookRating").setValue(updatedRating);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
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
            Intent menu = new Intent (BookDetailsActivity.this, MenuUserActivity.class);
            startActivity(menu);
        }

        if(id == R.id.menuCart){
            Intent cart = new Intent (BookDetailsActivity.this, MyOrderActivity.class);
            startActivity(cart);

        }
        return super.onOptionsItemSelected(item);
    }


}
