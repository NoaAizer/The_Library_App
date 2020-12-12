package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class OrderPageActivity extends AppCompatActivity implements View.OnClickListener{

    FireBaseDBOrder fbOr = new FireBaseDBOrder();
    private TextView OrderId, UserId, collect, endOfOrder;
    private Button OrderComplete, UserDetails;
    String orderID;
    String collectStr;

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

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                orderID= null;
            } else {
                orderID= extras.getString("orderID");
            }
        } else {
            orderID= (String) savedInstanceState.getSerializable("orderID");
        }

        OrderId.append(orderID);

        ArrayAdapter adapter = new ArrayAdapter(OrderPageActivity.this, android.R.layout.simple_list_item_1);
        ListView list = (ListView) findViewById(R.id.detListOfBooks);

        DatabaseReference order = FirebaseDatabase.getInstance().getReference("orders").child(orderID);
        order.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectStr = dataSnapshot.child("collect").getValue(String.class);

                UserId.append(dataSnapshot.child("userID").getValue(String.class));
                collect.append(dataSnapshot.child("collect").getValue(String.class));
                endOfOrder.append(dataSnapshot.child("endOfOrder").getValue(String.class));

                Iterable<DataSnapshot> listOfBook = dataSnapshot.child("listOfBooks").getChildren();
                Iterator<DataSnapshot> it = listOfBook.iterator();
                while(it.hasNext()) {
                    adapter.add(it.next().getValue(String.class));
                }
//                for(int i=0; i<listOfBook.iterator(); i++)
//                {
//                    adapter.add(listOfBook[i]);
//                }
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void onClick(View v) {
        if (v == UserDetails) {
            Intent intent = new Intent(OrderPageActivity.this, UserDetailsActivity.class);
            startActivity(intent.putExtra("userID", UserId.toString()));
        }
        else if(v == OrderComplete)
        {
            Toast.makeText(getApplicationContext(), "ההזמנה הושלמה בהצלחה", Toast.LENGTH_LONG).show();
            DatabaseReference order = FirebaseDatabase.getInstance().getReference("orders");
            order.child(orderID).child("complete").setValue(true);
            if(collectStr.equals("איסוף עצמי"))
            {
                order.child(orderID).child("statusDeliver").setValue("ההזמנה הושלמה, ניתן לאסוף בשעות הפתיחה");
            }
            else {
                order.child(orderID).child("statusDeliver").setValue("ההזמנה ממתינה למשלוח");
            }
        }
    }
}