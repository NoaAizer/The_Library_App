package com.example.thelibrary.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class orderTrackingActivity extends AppCompatActivity {
    String thisUserID;
    String orderID, status;
    ArrayList<String> bookList =new ArrayList<>();
    ArrayList<String> nameOfBooks =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        thisUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            ArrayAdapter adapter = new ArrayAdapter(orderTrackingActivity.this, android.R.layout.simple_list_item_1);
            ListView list = (ListView) findViewById(R.id.trackList);

            boolean exist = false;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot orderSnapshot : dataSnapshot.child("orders").getChildren()) {
                        if (orderSnapshot.child("userID").getValue().equals(thisUserID) && orderSnapshot.child("arrivedToUser").getValue().equals(false)) {
                            exist = true;
                            orderID = orderSnapshot.getKey();
                            status = orderSnapshot.child("statusDeliver").getValue(String.class);
                            bookList = (ArrayList<String>) orderSnapshot.child("listOfBooks").getValue();
                            for(int i=0; i<bookList.size(); i++)
                            {
                                nameOfBooks.add(dataSnapshot.child("books").child(bookList.get(i)).child("name").getValue(String.class));
                            }
                            adapter.add(orderID + "\n" + status + "\n" + nameOfBooks);
                        }
                    }
                    if (!exist) {
                        Toast.makeText(getApplicationContext(), "אין הזמנות ברשימה", Toast.LENGTH_LONG).show();
                        return;
                    }
                    list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
}