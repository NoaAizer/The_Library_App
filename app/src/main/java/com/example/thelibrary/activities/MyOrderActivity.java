package com.example.thelibrary.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.example.thelibrary.fireBase.model.FireBaseDBShoppingList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;


public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Button save_order, cancel_order;
    private RadioButton TA, deliver;



    ArrayList<String> orderList,notChecked, bookList=new ArrayList<>();
    int amountOfBooksRemains;
    String userTZ, userID, shopID,type = "";
    OrderListAdapter orderAdapter;
    ListView orderListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        orderListView = (ListView) findViewById(R.id.listBooks);

        save_order = findViewById(R.id.finish);
        cancel_order = findViewById(R.id.cancelOrder);
        TA = findViewById(R.id.listTA);
        deliver = findViewById(R.id.deliver);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        save_order.setOnClickListener(this);
        cancel_order.setOnClickListener(this);
        TA.setOnCheckedChangeListener(this);
        deliver.setOnCheckedChangeListener(this);

        //update the bookList by the shopping list
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userTZ = dataSnapshot.child("users").child(userID).child("tz").getValue(String.class);
                shopID = dataSnapshot.child("users").child(userID).child("shoppingID").getValue(String.class);
                amountOfBooksRemains = dataSnapshot.child("users").child(userID).child("amountOfBooksRemains").getValue(Long.class).intValue();
                bookList = (ArrayList<String>) dataSnapshot.child("shoppingList").child(shopID).child("bookList").getValue();


                if (bookList == null || bookList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "אין ספרים ברשימה", Toast.LENGTH_LONG).show();
                    return;
                }

                orderAdapter = new OrderListAdapter(MyOrderActivity.this, R.layout.single_book_order_row, bookList);
                orderListView.setAdapter(orderAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView == TA)
            if (isChecked) type = "איסוף עצמי";

        if(buttonView == deliver){
            if (isChecked) type = "משלוח";
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {
        if (v == save_order) {
            orderList = new ArrayList<>();
            notChecked = new ArrayList<>();
            for (int i = 0; i < orderAdapter.mCheckStates.size(); i++) {
                if (orderAdapter.mCheckStates.get(i) == true) {
                    orderList.add(bookList.get(i));
                } else {
                    notChecked.add(bookList.get(i));
                }
            }
            if (orderList.isEmpty()){
                Toast.makeText(getApplicationContext(),"ההזמנה ריקה, אנא בחר את הספרים שהנך רוצה לקחת או בטל את ההזמנה.", Toast.LENGTH_LONG).show();
                return;
            }

            if (orderList.size() > amountOfBooksRemains) {
                Toast.makeText(getApplicationContext(),"כמות הספרים בהזמנה גדולה מכמות הספרים במנוי שלך, נותרו לך "+amountOfBooksRemains+" ספרים", Toast.LENGTH_LONG).show();
                return;
            } else {
                if (!type.equals("")) {
                Toast.makeText(getApplicationContext(), "ההזמנה הושלמה בהצלחה ונמצאת בטיפול", Toast.LENGTH_LONG).show();

                //update the amount in stock of the books that haven't taken after finish order
                for (int i = 0; i < orderAdapter.mCheckStates.size(); i++) {
                    if (notChecked.contains(bookList.get(i))) {
                        String bookID = bookList.get(i);
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int amount = dataSnapshot.child("books").child(bookID).child("amount").getValue(Long.class).intValue();//UPDATE AMOUNT
                                myRef.child("books").child(bookID).child("amount").setValue((amount) + 1);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }

                    bookList = orderList;
                    LocalDate today = LocalDate.now(); // update the order date.
                    String endOfOrder = today.plusDays(30).toString();
                    new FireBaseDBOrder().addOrderToDB(bookList, userTZ, userID, type, endOfOrder);
                    // Delete books from shopping list
                    new FireBaseDBShoppingList().clearShopListDB(shopID);
                    //updates amount of books to user
                    FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("amountOfBooksRemains").setValue(amountOfBooksRemains - bookList.size());
                    Intent intent = new Intent(MyOrderActivity.this, MenuUserActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "בחר צורת משלוח", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        if (v == cancel_order){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyOrderActivity.this);
            alertDialog.setTitle("CANCEL ORDER");
            alertDialog.setMessage("האם ברצונך לבטל את ההזמנה ולהסיר את כל הספרים מהסל?");


            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            alertDialog.setPositiveButton("בטל הזמנה",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    shopID = dataSnapshot.child("users").child(userID).child("shoppingID").getValue(String.class);
                                    for(String bookID: bookList) {
                                        int amount = dataSnapshot.child("books").child(bookID).child("amount").getValue(Long.class).intValue();//UPDATE AMOUNT
                                        myRef.child("books").child(bookID).child("amount").setValue((amount) + 1);
                                    }
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                            new FireBaseDBShoppingList().clearShopListDB(shopID);
                            Intent intent = new Intent(MyOrderActivity.this, MenuUserActivity.class);
                            startActivity(intent);
                        }
                        });
            alertDialog.setNegativeButton("חזור להזמנה",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();

        }
    }

}



