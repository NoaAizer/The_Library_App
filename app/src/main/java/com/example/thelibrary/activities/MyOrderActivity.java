package com.example.thelibrary.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.example.thelibrary.fireBase.model.FireBaseDBShoppingList;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;


public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private Button save_order;
    private RadioButton TA, deliver;
    FireBaseDBOrder fbOr = new FireBaseDBOrder();
    FireBaseDBShoppingList fbSl = new FireBaseDBShoppingList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        save_order = findViewById(R.id.finish);
        TA = findViewById(R.id.listTA);
        deliver = findViewById(R.id.deliver);

        save_order.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String shopID = user.getShoopingID();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> bookList = (ArrayList<String>)dataSnapshot.child("shoppingList").child(shopID).child("bookList").getValue();

                ArrayAdapter adapter = new ArrayAdapter(MyOrderActivity.this, android.R.layout.simple_list_item_multiple_choice);
                ListView list = (ListView) findViewById(R.id.listBooks);

                for (int i=0; i<bookList.size(); i++) {
                    BookObj book = (BookObj) dataSnapshot.child("books").child(bookList.get(i)).getValue();
                    adapter.add(book.getName());
                    adapter.add(book.getauthor());
                    // להוסיף שורה אם קיים במלאי או לא
                    if(book.getCount() == 0)
                    {
                        adapter.add("הספר לא במלאי");
                    }
                }
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {
        if (v == save_order) {
            if(TA.isChecked() || deliver.isChecked()) {
                Toast.makeText(getApplicationContext(), "ההזמנה הושלמה בהצלחה", Toast.LENGTH_LONG).show();

                String[] listOfBooks = new String[10];

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String shopID = user.getShoopingID();

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> bookList = (ArrayList<String>)dataSnapshot.child("shoppingList").child(shopID).child("bookList").getValue();
                        if(bookList.size() > 10)
                        {
                            Toast.makeText(getApplicationContext(), "המנוי שלך מוגבל ל-10 ספרים. בבקשה תוריד כמה מהרשימה", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for (int i=0; i<bookList.size(); i++) {
                            listOfBooks[i] = bookList.get(i);
                            BookObj book = (BookObj) dataSnapshot.child("books").child(bookList.get(i)).getValue();
                            book.setCount();
                            // לבדוק האם מעדכן גם בFB או שצריך לקרוא לפונ' ב- FireBaseDBBook שתעדכן
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                String userID = user.getUid();

                String collect;
                if (TA.isChecked()) {
                    collect = "איסוף עצמי";
                }
                else {
                    collect = "משלוח";
                }

                LocalDate today = LocalDate.now();
                String endOfOrder = today.plusDays(30).toString();
                fbOr.addOrderToDB(listOfBooks, userID, collect, endOfOrder);
                // מחיקת הספרים
                fbSl.clearShopListDB(shopID);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "בחר צורת משלוח", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

}
