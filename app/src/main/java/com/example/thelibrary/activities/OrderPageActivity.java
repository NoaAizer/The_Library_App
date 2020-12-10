package com.example.thelibrary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderPageActivity extends AppCompatActivity implements View.OnClickListener{

    FireBaseDBOrder fbOr = new FireBaseDBOrder();
    private TextView OrderId, UserId, collect, endOfOrder;
    private Button OrderComplete, UserDetails;
    private String[] listOfBook;
    String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        OrderId = (TextView) findViewById(R.id.detOrderID);
        UserId = (TextView) findViewById(R.id.detUserID);
        collect = (TextView) findViewById(R.id.detDeliver);
        endOfOrder = (TextView) findViewById(R.id.detEndOfOrder);

        OrderComplete = findViewById(R.id.OrderComplete);
        UserDetails = findViewById(R.id.UserDetails);

        OrderComplete.setOnClickListener(this);
        UserDetails.setOnClickListener(this);

        orderID = (String) savedInstanceState.getSerializable("orderID");
        OrderId.append(orderID);

        ScrollView list = (ScrollView) findViewById(R.id.detListOfBooks);

        DatabaseReference order = FirebaseDatabase.getInstance().getReference("orders").child(orderID);
        order.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserId.append(dataSnapshot.child("UserId").getValue(String.class));
                collect.append(dataSnapshot.child("collect").getValue(String.class));
                endOfOrder.append(dataSnapshot.child("endOfOrder").getValue(String.class));

                LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
                listOfBook = dataSnapshot.child("listOfBooks").getValue(String[].class);
                TextView tv = new TextView(OrderPageActivity.this);
                for(int i=0; i<listOfBook.length; i++)
                {
                    tv.setText(listOfBook[i]);
                    ll.addView(tv);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void onClick(View v) {
        if (v == UserDetails) {
            Intent intent = new Intent(OrderPageActivity.this, UserDetailsActivity.class);
            startActivity(intent);
        }
        else if(v == OrderComplete)
        {
            Toast.makeText(getApplicationContext(), "ההזמנה הושלמה בהצלחה", Toast.LENGTH_LONG).show();
            DatabaseReference order = FirebaseDatabase.getInstance().getReference("orders");
            order.child(orderID).child("complete").setValue(true);
        }
    }
}