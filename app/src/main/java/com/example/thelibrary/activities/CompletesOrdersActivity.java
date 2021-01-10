package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.adapters.CompleteOrdersAdapter;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.example.thelibrary.fireBase.model.dataObj.OrderObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompletesOrdersActivity extends AppCompatActivity {
    ListView lvOrder;
    ArrayList<OrderObj> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completes_orders);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);



        DatabaseReference ordersRef = new FireBaseDBOrder().getOrdersListFromDB();
        ordersRef.orderByChild("arrivedToUser").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    orders.add(data.getValue(OrderObj.class));
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        lvOrder = findViewById(R.id.listOC);

        // Initialize adapter and set adapter to list view
        CompleteOrdersAdapter ordersAd = new CompleteOrdersAdapter(this, orders);
        lvOrder.setAdapter(ordersAd);
        //bookAd.notifyDataSetChanged();
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
           Intent home = new Intent (CompletesOrdersActivity.this, MenuAdminActivity.class);
            startActivity(home);
        }
        return super.onOptionsItemSelected(item);
    }

}