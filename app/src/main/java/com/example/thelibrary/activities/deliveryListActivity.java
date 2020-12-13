package com.example.thelibrary.activities;

import android.os.Bundle;
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

public class deliveryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");
        ref.orderByChild("complete").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {

            ArrayAdapter adapter = new ArrayAdapter(deliveryListActivity.this, android.R.layout.simple_list_item_1);
            ListView list = (ListView) findViewById(R.id.deliverOrders);
            String orderID;
            String status;

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    if (orderSnapshot.child("collect").getValue().equals("משלוח") && orderSnapshot.child("arrivedToUser").getValue().equals(false)) {
                        orderID = orderSnapshot.getKey();
                        status = orderSnapshot.child("statusDeliver").getValue(String.class);
                        adapter.add(orderID + "\n" + status);
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
}