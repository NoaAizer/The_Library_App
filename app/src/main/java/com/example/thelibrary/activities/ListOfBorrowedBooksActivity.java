package com.example.thelibrary.activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfBorrowedBooksActivity extends AppCompatActivity {

    FireBaseDBOrder fbOr = new FireBaseDBOrder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_borrowed_books);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String thisUserID = user.getUid();


        final ArrayList<String>[] listOfBooks = new ArrayList[1];
        final String[] orderID = new String[1];
        final String[] endOfOrder = new String[1];
        final String[] statusDeliver = new String[1];
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");
        ref.orderByChild("UserID").equalTo(thisUserID).addListenerForSingleValueEvent(new ValueEventListener() {

            ScrollView list = (ScrollView) findViewById(R.id.listBB);


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    listOfBooks[0] = (ArrayList<String>) userSnapshot.child("listOfBooks").getValue();
                    orderID[0] = userSnapshot.getKey();
                    endOfOrder[0] = userSnapshot.child("endOfOrder").getValue(String.class);
                    statusDeliver[0] = userSnapshot.child("statusDeliver").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
            });
        if(listOfBooks[0].size() == 0)
        {
            Toast.makeText(getApplicationContext(), "אין הזמנות ברשימה", Toast.LENGTH_LONG).show();
            return;
        }

        LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
        TextView tv = new TextView(ListOfBorrowedBooksActivity.this);
        tv.setText(orderID[0]);
        ll.addView(tv);

        for(int i = 0; i< listOfBooks[0].size(); i++)
        {
            TextView tv2 = new TextView(ListOfBorrowedBooksActivity.this);
            //tv2.setText(listOfBooks[0].get(i).getName());
            ll.addView(tv2);

            TextView tv3 = new TextView(ListOfBorrowedBooksActivity.this);
           // tv3.setText(listOfBooks[0][i].getauthor());
            ll.addView(tv3);
        }

        TextView tv4 = new TextView(ListOfBorrowedBooksActivity.this);
        tv4.setText(endOfOrder[0]);
        ll.addView(tv4);

        TextView tv5 = new TextView(ListOfBorrowedBooksActivity.this);
        tv5.setText(statusDeliver[0]);
        ll.addView(tv5);
    }
    }
