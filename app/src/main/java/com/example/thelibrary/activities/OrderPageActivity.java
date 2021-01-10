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

    private TextView OrderId, UserId, collect, endOfOrder;
    private Button OrderComplete, UserDetails, changeOrderTypeBtn;
    String orderID;
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
        UserId = (TextView) findViewById(R.id.detUserID);
        collect = (TextView) findViewById(R.id.detDeliver);
        endOfOrder = (TextView) findViewById(R.id.detEndOfOrder);

        OrderComplete = findViewById(R.id.OrderComplete);
        UserDetails = findViewById(R.id.UserDetails);
        changeOrderTypeBtn = findViewById(R.id.ChanegOrderType);

        OrderComplete.setOnClickListener(this);
        UserDetails.setOnClickListener(this);
        changeOrderTypeBtn.setOnClickListener(this);

//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                orderID= null;
//            } else {
//                orderID= extras.getString("orderID");
//            }
//        } else {
//            orderID= (String) savedInstanceState.getSerializable("orderID");
//        }
        Intent intent = getIntent();
        OrderId.append( intent.getExtras().getString("orderID"));
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

//    @Override
//    protected void onResume(){
//        super.onResume();
//        Toast.makeText(getApplicationContext(),"הספר נוסף למועדפים שלי", Toast.LENGTH_SHORT).show();
//        this.onCreate(null);
//    }
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
                            DatabaseReference order = FirebaseDatabase.getInstance().getReference("orders");
                                order.child(orderID).child("collect").setValue(newType);
                            recreate();
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
}