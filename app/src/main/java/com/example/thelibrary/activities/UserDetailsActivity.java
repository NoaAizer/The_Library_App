package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    FireBaseDBUser fu = new FireBaseDBUser();
    private TextView fname, lname, address, phone, email, sub;
    private FloatingActionButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        back = (FloatingActionButton) findViewById(R.id.backToUserMenu);
        fname = (TextView) findViewById(R.id.detFName);
        lname = (TextView) findViewById(R.id.detLName);
        address = (TextView) findViewById(R.id.detAdd);
        phone = (TextView) findViewById(R.id.detPhone);
        email = (TextView) findViewById(R.id.detEmail);
        sub = (TextView) findViewById(R.id.detSub);

        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = fu.getUserFromDB(user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fname.append(dataSnapshot.child("firstName").getValue(String.class));
                lname.append(dataSnapshot.child("lastName").getValue(String.class));
                address.append(dataSnapshot.child("address").getValue(String.class));
                phone.append(dataSnapshot.child("phone").getValue(String.class));
                email.append(dataSnapshot.child("email").getValue(String.class));
                sub.append(dataSnapshot.child("subscription").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        back.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == back) {
            Intent intent = new Intent(UserDetailsActivity.this, MenuUserActivity.class);
            startActivity(intent);
        }
    }
}