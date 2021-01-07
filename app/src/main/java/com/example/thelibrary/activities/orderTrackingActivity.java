package com.example.thelibrary.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.adapters.orderTrackingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class orderTrackingActivity extends AppCompatActivity {
    String thisUserID;
    String orderID;
    ArrayList<String> ordersID =new ArrayList<>();
    orderTrackingAdapter OrderTrackingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        thisUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            ListView list = (ListView) findViewById(R.id.trackList);

            boolean exist = false;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.child("orders").getChildren()) {
                    if (orderSnapshot.child("userID").getValue().equals(thisUserID) && orderSnapshot.child("arrivedToUser").getValue().equals(false)) {
                        exist = true;
                        orderID = orderSnapshot.getKey();
                        ordersID.add(orderID);
                    }
                }
                if (!exist) {
                    Toast.makeText(getApplicationContext(), "אין הזמנות ברשימה", Toast.LENGTH_LONG).show();
                    return;
                }
                OrderTrackingAdapter = new orderTrackingAdapter(orderTrackingActivity.this, R.layout.single_order_tracking, ordersID);
                list.setAdapter(OrderTrackingAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
}