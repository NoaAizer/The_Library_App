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
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.example.thelibrary.fireBase.model.FireBaseDBShoppingList;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.firebase.auth.FirebaseAuth;
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
    String userID, shopID;
    int amountOfBooksRemains=0, booklistSize=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        save_order = findViewById(R.id.finish);
        TA = findViewById(R.id.listTA);
        deliver = findViewById(R.id.deliver);

        save_order.setOnClickListener(this);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopID= dataSnapshot.child("users").child(userID).child("shoppingID").getValue(String.class);
                amountOfBooksRemains= dataSnapshot.child("users").child(userID).child("amountOfBooksRemains").getValue(Integer.class);
                ArrayList<String> bookList = (ArrayList<String>)dataSnapshot.child("shoppingList").child(shopID).child("bookList").getValue();

                ArrayAdapter adapter = new ArrayAdapter(MyOrderActivity.this, android.R.layout.simple_list_item_multiple_choice);
                ListView list = (ListView) findViewById(R.id.listBooks);

                for (int i=0; i<bookList.size(); i++) {
                    BookObj book = (BookObj) dataSnapshot.child("books").child(bookList.get(i)).getValue();
                    adapter.add(book.getName());
                    adapter.add(book.getauthor());
                    // להוסיף שורה אם קיים במלאי או לא
//                    if(book.getCount() == 0)
//                    {
//                        adapter.add("הספר לא במלאי");
//                    }

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
                Toast.makeText(getApplicationContext(), "ההזמנה הושלמה בהצלחה ונמצאת בטיפול", Toast.LENGTH_LONG).show();

                String[] listOfBooks = new String[amountOfBooksRemains];

//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                UserObj user=  new FireBaseDBUser().getUserObjFromDBByID(userID);
//                String shopID = user.getShoppingID();

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> bookList = (ArrayList<String>)dataSnapshot.child("shoppingList").child(shopID).child("bookList").getValue();
                        if(bookList.size() >= amountOfBooksRemains) // ######### לבדוק שהוא לא ממשיך בהזמנה כל עוד הכמות לא נכונה ##########
                        {
                            Toast.makeText(getApplicationContext(), "כמות הספרים גדולה מכמות הספרים שאת/ה יכול/ה לקחת", Toast.LENGTH_LONG).show();
                            return;
                        }
//                        for (int i=0; i<bookList.size(); i++) {
//                            listOfBooks[i] = bookList.get(i);
//                            BookObj book = (BookObj) dataSnapshot.child("books").child(bookList.get(i)).getValue();
//                            book.setCount();
//                            // לבדוק האם מעדכן גם בFB או שצריך לקרוא לפונ' ב- FireBaseDBBook שתעדכן
//                        }
                        booklistSize=bookList.size();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

               // String userID = user.getUid();

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
                //updates amount of books to user
                myRef.child("users").child(userID).child("amountOfBooksRemains").setValue(amountOfBooksRemains-booklistSize);

            }
            else
            {
                Toast.makeText(getApplicationContext(), "בחר צורת משלוח", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

}