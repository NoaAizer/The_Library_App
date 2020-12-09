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
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

public class TreatmentOrders extends AppCompatActivity {

    FireBaseDBOrder fbOr = new FireBaseDBOrder();
    private Button listTA, listDeliver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_orders);

        listTA = findViewById(R.id.listTA);
        listDeliver = findViewById(R.id.listDeliver);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");
        ref.orderByChild("complete").equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {

            ScrollView list = (ScrollView) findViewById(R.id.orders);
            String orderID;

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
                    TextView tv = new TextView(TreatmentOrders.this);
                    tv.setText(orderID);
                    ll.addView(tv);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void onClick(View v){
        if (v == listTA) {
//            Intent intent = new Intent(MenuAdminActivity.this, BooksListAdminActivity.class);
//            startActivity(intent);
        } else if (v == listDeliver) {
//            Intent intent = new Intent(MenuAdminActivity.this, BooksListAdminActivity.class);
//            startActivity(intent);
        }
    }
}