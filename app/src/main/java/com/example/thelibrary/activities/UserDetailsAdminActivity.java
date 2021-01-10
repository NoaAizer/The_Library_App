package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsAdminActivity extends AppCompatActivity {
    private TextView fname, lname, address, phone, email, sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_admin);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        fname = (TextView) findViewById(R.id.detFName);
        lname = (TextView) findViewById(R.id.detLName);
        address = (TextView) findViewById(R.id.detAdd);
        phone = (TextView) findViewById(R.id.detPhone);
        email = (TextView) findViewById(R.id.detEmail);
        sub = (TextView) findViewById(R.id.detSub);
    }

    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String userID = intent.getExtras().getString("userID");
        FireBaseDBUser fu = new FireBaseDBUser();
        DatabaseReference userRef = fu.getUserFromDB(userID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fname.setText(dataSnapshot.child("firstName").getValue(String.class));
                lname.setText(dataSnapshot.child("lastName").getValue(String.class));
                address.setText(dataSnapshot.child("address").getValue(String.class));
                phone.setText(dataSnapshot.child("phone").getValue(String.class));
                email.setText(dataSnapshot.child("email").getValue(String.class));
                sub.setText(dataSnapshot.child("subscription").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back_home, menu);
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
            Intent menu = new Intent(UserDetailsAdminActivity.this, MenuAdminActivity.class);
            startActivity(menu);
        }
        return super.onOptionsItemSelected(item);
    }
}
