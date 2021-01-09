package com.example.thelibrary.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBUser;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MenuUserActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loan_btn, myDetails_btn, myBooks_btn, searchBook_btn;
    private TextView hello;
    private ImageView ratingBooksImg, newBooksImg;
    private ExtendedFloatingActionButton editBooks_btn, labInf_btn;
    FireBaseDBUser fu = new FireBaseDBUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        loan_btn = (Button) findViewById(R.id.checkALoan);
        myDetails_btn = (Button) findViewById(R.id.userDetails);
        myBooks_btn = (Button) findViewById(R.id.myBooks);
        searchBook_btn = (Button) findViewById(R.id.searchAbook);
        editBooks_btn = (ExtendedFloatingActionButton) findViewById(R.id.editBooks);
        labInf_btn = (ExtendedFloatingActionButton) findViewById(R.id.information);
        hello = (TextView) findViewById(R.id.hello);
        ratingBooksImg = (ImageView) findViewById(R.id.menuImageRating);
        newBooksImg = (ImageView) findViewById(R.id.menuImageNew);

        ArrayList<BookObj> newBooks = new ArrayList<BookObj>();
        ArrayList<BookObj> ratingBooks = new ArrayList<BookObj>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
      //  DatabaseReference userRef = fu.getUserFromDB(user.getUid());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name =dataSnapshot.child("users").child(user.getUid()).child("firstName").getValue(String.class);
                hello.append(name + ",");
                DataSnapshot bookDS = dataSnapshot.child("books");
                for (DataSnapshot book : bookDS.getChildren()) {
                    newBooks.add(book.getValue(BookObj.class));
                    ratingBooks.add(book.getValue(BookObj.class));
                }
                Collections.sort(newBooks, new Comparator<BookObj>() {
            public int compare(BookObj b1, BookObj b2) {
                if (b1.getAdded_date() == null || b2.getAdded_date() == null)
                    return 0;
                return -b1.getAdded_date().compareTo(b2.getAdded_date());
            }
        });
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
                /// NEED TO BE CHANGE ACCORDING TO RATING!!!!
                Collections.sort(ratingBooks, new Comparator<BookObj>() {
                    public int compare(BookObj b1, BookObj b2) {
                        if (b1.getName() == null || b2.getName() == null)
                            return 0;
                        return -b1.getName().compareTo(b2.getName());
                    }
                });
                showImages(newBooks, newBooksImg);
                showImages(ratingBooks, ratingBooksImg);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//

        loan_btn.setOnClickListener(this);
        myDetails_btn.setOnClickListener(this);
        myBooks_btn.setOnClickListener(this);
        searchBook_btn.setOnClickListener(this);
        editBooks_btn.setOnClickListener(this);
        labInf_btn.setOnClickListener(this); //library information
        ratingBooksImg.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v == loan_btn) {
            Intent intent = new Intent(MenuUserActivity.this, orderTrackingActivity.class);
            startActivity(intent);
        }

        if (v == myDetails_btn) {
            Intent intent = new Intent(MenuUserActivity.this, UserDetailsActivity.class);
            startActivity(intent);

        }
        if (v == myBooks_btn) {
            Intent intent = new Intent(MenuUserActivity.this, ListOfBorrowedBooksActivity.class);
            startActivity(intent);
        }
        if (v == searchBook_btn) {
            Intent intent = new Intent(MenuUserActivity.this, SearchBookActivity.class);
            intent.putExtra("type","user");
            startActivity(intent);

        }
        if (v == labInf_btn) {
            Intent intent = new Intent(MenuUserActivity.this, LocationActivity.class);
            startActivity(intent);

        }
        if (v == editBooks_btn) {
            Intent intent = new Intent(MenuUserActivity.this, MyOrderActivity.class);
            startActivity(intent);

        }

    }

    // Show images in the imageView - 5 books
private void showImages(ArrayList <BookObj> books, ImageView imageView){

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        int i = 0;
        public void run() {
            StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
            StorageReference ref = mImageStorage.child(books.get(i).getImageURL());
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri).into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MenuUserActivity.this, BookDetailsActivity.class);
                            int index=0;
                            if(i==0) index =4;
                            else index = i-1;
                            intent.putExtra("bookID", books.get(index).getId());
                            startActivity(intent);
                        }
                    });
                    i++;
                    if (i > 4) {
                        i = 0;
                    }

                } });
            handler.postDelayed(this, 5000);

        }
    };
    handler.postDelayed(runnable, 1);


}
}