package com.example.thelibrary.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class listOfLateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_late);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            ArrayAdapter adapter = new ArrayAdapter(listOfLateActivity.this, android.R.layout.simple_list_item_1);
            ListView list = (ListView) findViewById(R.id.listLate);
            String orderID, userID, fName, lName, phone;
            String endDate;
            boolean exist = false;

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot orderSnapshot : dataSnapshot.child("orders").getChildren()) {
                        if (orderSnapshot.child("arrivedToUser").getValue().equals(true)) {
                            exist = true;
                            endDate = orderSnapshot.child("endOfOrder").getValue(String.class);
                            String today = LocalDate.now().toString();
                            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                            Date d1 = sdformat.parse(today);
                            Date d2 = sdformat.parse(endDate);
                            if (d1.compareTo(d2) > 0) {
                                orderID = orderSnapshot.getKey();
                                userID = orderSnapshot.child("userID").getValue(String.class);
                                DataSnapshot userSnapshot = dataSnapshot.child("users").child(userID);
                                fName = userSnapshot.child("firstName").getValue(String.class);
                                lName = userSnapshot.child("lastName").getValue(String.class);
                                phone = userSnapshot.child("phone").getValue(String.class);
                                adapter.add("order num: " +orderID + "\nname: " + fName + " " + lName + "\nphone: " + phone + "\nend: " + endDate);
                            }
                        }
                    }
                    if (!exist) {
                        Toast.makeText(getApplicationContext(), "אין הזמנות ברשימה", Toast.LENGTH_LONG).show();
                        return;
                    }

                    list.setAdapter(adapter);
                }
                catch(Exception e) {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
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