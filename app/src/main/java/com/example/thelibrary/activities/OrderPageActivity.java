package com.example.thelibrary.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class OrderPageActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView OrderId, UserTz, collect, endOfOrder;
    private Button OrderComplete, UserDetails, changeOrderTypeBtn;
    String orderID, userTz;
    String collectStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        OrderId = (TextView) findViewById(R.id.detOrderID);
        UserTz = (TextView) findViewById(R.id.detUserTZ);
        collect = (TextView) findViewById(R.id.detDeliver);
        endOfOrder = (TextView) findViewById(R.id.detEndOfOrder);

        OrderComplete = findViewById(R.id.OrderComplete);
        UserDetails = findViewById(R.id.UserDetails);
        changeOrderTypeBtn = findViewById(R.id.ChanegOrderType);

        OrderComplete.setOnClickListener(this);
        UserDetails.setOnClickListener(this);
        changeOrderTypeBtn.setOnClickListener(this);

        fillDetails();
    }

    public void onClick(View v) {
        if (v == UserDetails) {
            Intent intent = new Intent(OrderPageActivity.this, UserDetailsAdminActivity.class);
            startActivity(intent.putExtra("userID", userTz));
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
            finish();
        }
        if(v == changeOrderTypeBtn){
            String newType;
            if(collectStr.equals("איסוף עצמי")) newType="משלוח";
            else newType = "איסוף עצמי";
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderPageActivity.this);
            alertDialog.setTitle("שינוי צורת משלוח");
            alertDialog.setMessage("האם ברצונך לשנות את צורת המשלוח מ"+collectStr+" ל"+newType+"?");
            alertDialog.setCancelable(true);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            alertDialog.setPositiveButton("עדכון",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders");
                            orderRef.child(orderID).child("collect").setValue(newType);
                            dialog.dismiss();
                            fillDetails();
                        }
                    });
            alertDialog.setNegativeButton("חזור לפרטי ההזמנה",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
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
        return super.onOptionsItemSelected(item);
    }

    private void fillDetails(){
        orderID=getIntent().getExtras().getString("orderID");
        OrderId.setText(orderID );

        ArrayAdapter adapter = new ArrayAdapter(OrderPageActivity.this, android.R.layout.simple_list_item_1);
        ListView list = (ListView) findViewById(R.id.detListOfBooks);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectStr = dataSnapshot.child("orders").child(orderID).child("collect").getValue(String.class);

                userTz = dataSnapshot.child("orders").child(orderID).child("userTZ").getValue(String.class);
                UserTz.setText(userTz);
                collect.setText(dataSnapshot.child("orders").child(orderID).child("collect").getValue(String.class));
                endOfOrder.setText(dataSnapshot.child("orders").child(orderID).child("endOfOrder").getValue(String.class));

                Iterable<DataSnapshot> listOfBook = dataSnapshot.child("orders").child(orderID).child("listOfBooks").getChildren();
                Iterator<DataSnapshot> it = listOfBook.iterator();
                while(it.hasNext()) {
                    String bookId = it.next().getValue(String.class);
                    adapter.add(dataSnapshot.child("books").child(bookId).child("name").getValue(String.class));
                }
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}