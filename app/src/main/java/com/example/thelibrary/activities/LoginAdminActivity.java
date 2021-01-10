package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginAdminActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private Button login;
    private ProgressBar loading;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        loading  = (ProgressBar) findViewById(R.id.loading);
        login = findViewById(R.id.loginAdmin);
        passwordEditText = (EditText) findViewById(R.id.passwordAdmin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password is required");
                    return;
                }
                loading.setVisibility(View.VISIBLE);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference ref = firebaseDatabase.getReference("AdminPassword");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long pass = dataSnapshot.getValue(Long.class);

                         if (!password.equals(""+pass)) {
                            Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Intent loginIntent=new Intent(LoginAdminActivity.this, MenuAdminActivity.class);
                        startActivity(loginIntent);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


            }
        });
    }

    protected void onPause() {
        super.onPause();
        loading.setVisibility(View.INVISIBLE);

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
            Intent main = new Intent(LoginAdminActivity.this, MainActivity.class);
            startActivity(main);
        }
        return super.onOptionsItemSelected(item);
    }
}








