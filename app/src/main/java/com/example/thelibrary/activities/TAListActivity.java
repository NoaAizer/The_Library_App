package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TAListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_list);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");
        ref.orderByChild("complete").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {

            ArrayAdapter adapter = new ArrayAdapter(TAListActivity.this, android.R.layout.simple_list_item_1);
            ListView list = (ListView) findViewById(R.id.TAorders);
            String orderID;

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    if (orderSnapshot.child("collect").getValue().equals("איסוף עצמי") && orderSnapshot.child("arrivedToUser").getValue().equals(false)) {
                        orderID = orderSnapshot.getKey();
                        adapter.add(orderID);
                    }
                }
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getApplicationContext(), "הזמנה מספר: " + orderID + " הושלמה והגיעה ליעדה", Toast.LENGTH_SHORT).show();
                        ref.child(orderID).child("arrivedToUser").setValue(true);
                        adapter.notifyDataSetChanged();
                    }

                });
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
            Intent home = new Intent (TAListActivity.this, MenuAdminActivity.class);
            startActivity(home);
        }
        return super.onOptionsItemSelected(item);
    }

}
