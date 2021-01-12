package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.adapters.TreatmentOrdersAdapter;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TreatmentOrdersActivity extends AppCompatActivity implements View.OnClickListener {

    private Button listTA, listDeliver;

    TreatmentOrdersAdapter treatmentOrdersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_orders);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        listTA = (Button) findViewById(R.id.listTA);
        listDeliver = (Button) findViewById(R.id.listDeliver);

        listTA.setOnClickListener(this);
        listDeliver.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ArrayAdapter adapter = new ArrayAdapter(TreatmentOrdersActivity.this, android.R.layout.simple_list_item_1);
        ListView list = (ListView) findViewById(R.id.TAorders);
        ArrayList<String> ordersID = new ArrayList<>();
        new FireBaseDBOrder().getOrdersListFromDB().orderByChild("complete").equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                   String orderID = orderSnapshot.getKey();
                    ordersID.add(orderID);
                }

                treatmentOrdersAdapter = new TreatmentOrdersAdapter(TreatmentOrdersActivity.this, R.layout.single_treatment_orders_row, ordersID);
                list.setAdapter(treatmentOrdersAdapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedFromList = (String) (list.getItemAtPosition(position));
                        Intent intent = new Intent(TreatmentOrdersActivity.this, OrderPageActivity.class);
                        intent.putExtra("orderID", selectedFromList);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }

    public void onClick(View v) {
        if (v == listTA) {
            Intent intent = new Intent(TreatmentOrdersActivity.this, TAListActivity.class);
            startActivity(intent);
        } else if (v == listDeliver) {
            Intent intent = new Intent(TreatmentOrdersActivity.this, deliveryListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuBack) {
            finish();
        }
        if (id == R.id.menuBackToHome) {
            Intent home = new Intent(TreatmentOrdersActivity.this, MenuAdminActivity.class);
            startActivity(home);
        }
        return super.onOptionsItemSelected(item);
    }
}
