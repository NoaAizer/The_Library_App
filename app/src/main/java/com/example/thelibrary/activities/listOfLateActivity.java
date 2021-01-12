package com.example.thelibrary.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.adapters.LateListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class listOfLateActivity extends AppCompatActivity {

    String orderID;
    ArrayList<String> lates =new ArrayList<>();
    LateListAdapter lateListAdapter;

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

            ListView list = (ListView) findViewById(R.id.listLate);
            boolean exist = false;

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot orderSnapshot : dataSnapshot.child("orders").getChildren()) {
                        if (orderSnapshot.child("arrivedToUser").getValue().equals(true)) {
                            String endDate = orderSnapshot.child("endOfOrder").getValue(String.class);
                            String today = LocalDate.now().toString();
                            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                            Date d1 = sdformat.parse(today);
                            Date d2 = sdformat.parse(endDate);
                            if (d1.compareTo(d2) > 0) {
                                exist = true;
                                orderID = orderSnapshot.getKey();
                                lates.add(orderID);
                            }
                        }
                    }
                    if (!exist) {
                        Toast.makeText(getApplicationContext(), "אין מנויים ברשימה", Toast.LENGTH_LONG).show();
                        return;
                    }

                    lateListAdapter = new LateListAdapter(listOfLateActivity.this, R.layout.single_late_list_row, lates);
                    list.setAdapter(lateListAdapter);
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