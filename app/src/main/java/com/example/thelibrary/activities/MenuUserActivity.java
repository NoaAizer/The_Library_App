package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.thelibrary.PaymentActivity;
import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBUser;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MenuUserActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loan_btn,myDetails_btn,myBooks_btn, searchBook_btn;
    private TextView hello;
    private ExtendedFloatingActionButton editBooks_btn ,labInf_btn;
    FireBaseDBUser fu = new FireBaseDBUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loan_btn = (Button)findViewById(R.id.checkALoan);
        myDetails_btn = (Button)findViewById(R.id.userDetails);
        myBooks_btn = (Button)findViewById(R.id.myBooks);
        searchBook_btn = (Button)findViewById(R.id.searchAbook);
        editBooks_btn = (ExtendedFloatingActionButton) findViewById(R.id.editBooks);
        labInf_btn = (ExtendedFloatingActionButton) findViewById(R.id.information);

        hello = (TextView) findViewById(R.id.hello);
        FirebaseUser user  =FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = fu.getUserFromDB(user.getUid());
        userRef.child("firstName").addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              String name = dataSnapshot.getValue(String.class);
              hello.append(name + ",");
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
          }
      });

        loan_btn.setOnClickListener(this);
        myDetails_btn.setOnClickListener(this);
        myBooks_btn.setOnClickListener(this);
        searchBook_btn.setOnClickListener(this);
        editBooks_btn.setOnClickListener(this);
        labInf_btn.setOnClickListener(this); //library information
    }
    public void onClick(View v) {
        if(v==loan_btn){

        }

        if(v==myDetails_btn){
            Intent intent = new Intent(MenuUserActivity.this, UserDetailsActivity.class);
            startActivity(intent);

        }
        if(v==myBooks_btn){
            Intent intent = new Intent(MenuUserActivity.this, ListOfBorrowedBooks.class);
            startActivity(intent);
        }
        if(v==searchBook_btn){
            Intent intent = new Intent(MenuUserActivity.this, PaymentActivity.class);
//            startActivity(intent);
        }
        if(v==labInf_btn){

        }
        if(v==editBooks_btn){
            Intent intent = new Intent(MenuUserActivity.this, MyOrderActivity.class);
            startActivity(intent);

        }
    }



}