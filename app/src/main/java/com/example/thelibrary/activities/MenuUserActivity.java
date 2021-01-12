package com.example.thelibrary.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.android.gms.tasks.OnSuccessListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        loan_btn = (Button) findViewById(R.id.checkALoan);
        myDetails_btn = (Button) findViewById(R.id.userDetails);
        myBooks_btn = (Button) findViewById(R.id.myBooks);
        searchBook_btn = (Button) findViewById(R.id.searchAbook);
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
                if (b1.added_dateInDate() == null || b2.added_dateInDate() == null)
                    return 0;
                return -b1.added_dateInDate().compareTo(b2.added_dateInDate());
            }
        });

                Collections.sort(ratingBooks, new Comparator<BookObj>() {
                    public int compare(BookObj b1, BookObj b2) {
                        if (b1.getBookRating() == null || b2.getBookRating() == null)
                            return 0;
                        return -b1.getBookRating().compareTo(b2.getBookRating());
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
        ratingBooksImg.setOnClickListener(this);
        Drawable drawable = ContextCompat.getDrawable(MenuUserActivity.this,R.drawable.icon_books);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*0.2),
                (int)(drawable.getIntrinsicHeight()*0.2));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 1, 1);
        myBooks_btn.setCompoundDrawables(sd.getDrawable(), null, null, null);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuCart) {
            Intent cart = new Intent (MenuUserActivity.this, MyOrderActivity.class);
            startActivity(cart);
        }
        if (id == R.id.menuInfo) {
            Intent info = new Intent (MenuUserActivity.this, LocationActivity.class);
            startActivity(info);
        }
        if(id == R.id.menuLogOut){
            FirebaseAuth.getInstance().signOut();
            Intent login = new Intent (MenuUserActivity.this, LoginUserActivity.class);
            startActivity(login);
        }
        return super.onOptionsItemSelected(item);
    }
}