package com.example.thelibrary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;

public class TreatmentOrders extends AppCompatActivity {

    FireBaseDBOrder fbOr = new FireBaseDBOrder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_orders);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");

        ScrollView list = (ScrollView) findViewById(R.id.orders);
        String orderID;

        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
                TextView tv = new TextView(this);
                tv.setText(orderID);
                ll.addView(tv);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            throw databaseError.toException();
        }
    }
}