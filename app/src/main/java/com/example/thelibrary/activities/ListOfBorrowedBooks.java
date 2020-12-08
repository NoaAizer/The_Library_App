package com.example.thelibrary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListOfBorrowedBooks extends AppCompatActivity {

    FireBaseDBOrder fbOr = new FireBaseDBOrder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_borrowed_books);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String thisUserID = user.getUid();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");
        ref.orderByChild("UserID").equalTo(thisUserID).addListenerForSingleValueEvent(new ValueEventListener() {

            ScrollView list = (ScrollView) findViewById(R.id.listBB);

            BookObj[] listOfBooks;
            String orderID;
            String endOfOrder;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    listOfBooks = userSnapshot.child("listOfBooks").getValue(BookObj[].class);
                    orderID = userSnapshot.getKey();
                    endOfOrder = userSnapshot.child("endOfOrder").getValue(String.class);

                    LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
                    TextView tv = new TextView(ListOfBorrowedBooks.this);
                    tv.setText(orderID);
                    ll.addView(tv);

                    for(int i=0; i<listOfBooks.length; i++)
                    {
                        TextView tv2 = new TextView(ListOfBorrowedBooks.this);
                        tv2.setText(listOfBooks[i].getName());
                        ll.addView(tv2);

                        TextView tv3 = new TextView(ListOfBorrowedBooks.this);
                        tv3.setText(listOfBooks[i].getauthor());
                        ll.addView(tv3);
                    }

                    TextView tv4 = new TextView(ListOfBorrowedBooks.this);
                    tv4.setText(endOfOrder);
                    ll.addView(tv4);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
            });
    }
}