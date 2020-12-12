package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TreatmentOrdersActivity extends AppCompatActivity implements View.OnClickListener{

    FireBaseDBOrder fbOr = new FireBaseDBOrder();
    private Button listTA, listDeliver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_orders);

        listTA = (Button)findViewById(R.id.listTA);
        listDeliver = (Button)findViewById(R.id.listDeliver);

        listTA.setOnClickListener(this);
        listDeliver.setOnClickListener(this);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");
        ref.orderByChild("complete").equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {

            ArrayAdapter adapter = new ArrayAdapter(TreatmentOrdersActivity.this, android.R.layout.simple_list_item_1);
            ListView list = (ListView) findViewById(R.id.TAorders);
            String orderID;

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    orderID = orderSnapshot.getKey();
                    adapter.add(orderID);
                }

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedFromList =(String) (list.getItemAtPosition(position));
                        Intent intent = new Intent(TreatmentOrdersActivity.this, OrderPageActivity.class);
                        startActivity(intent.putExtra("orderID", selectedFromList));
                    }
                });
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void onClick(View v){
        if (v == listTA) {
            Intent intent = new Intent(TreatmentOrdersActivity.this, TAListActivity.class);
            startActivity(intent);
        } else if (v == listDeliver) {
            Intent intent = new Intent(TreatmentOrdersActivity.this, deliveryListActivity.class);
            startActivity(intent);
        }
    }
}