package com.example.thelibrary.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MenuAdminActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loansList_btn, booksList_btn, returnBook_btn, clientsList_btn, add_btn, finishOrder_btn, laterList_btn;
    private boolean hasOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        loansList_btn = (Button) findViewById(R.id.loansList);
        booksList_btn = (Button) findViewById(R.id.booksList);
        returnBook_btn = (Button) findViewById(R.id.returnBook);
        clientsList_btn = (Button) findViewById(R.id.clientsList);
        add_btn = (Button) findViewById(R.id.adding);
        finishOrder_btn = (Button) findViewById(R.id.ordersCompletes);
        laterList_btn = (Button) findViewById(R.id.laterList);


        loansList_btn.setOnClickListener(this);
        booksList_btn.setOnClickListener(this);
        returnBook_btn.setOnClickListener(this);
        clientsList_btn.setOnClickListener(this);
        add_btn.setOnClickListener(this);
        finishOrder_btn.setOnClickListener(this);
        laterList_btn.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v == loansList_btn) {
            Intent intent = new Intent(MenuAdminActivity.this, TreatmentOrdersActivity.class);
            startActivity(intent);
        }
        if (v == booksList_btn) {
            Intent intent = new Intent(MenuAdminActivity.this, SearchBookActivity.class);
            intent.putExtra("type","admin");
            startActivity(intent);

        }
        if (v == laterList_btn) {
            Intent intent = new Intent(MenuAdminActivity.this, listOfLateActivity.class);
            startActivity(intent);

        }
        if (v == returnBook_btn) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuAdminActivity.this);
            alertDialog.setTitle("Return Books");
            alertDialog.setMessage("הכנס את תז של המנוי המחזיר את הספרים:");

            final EditText input = new EditText(MenuAdminActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setRawInputType(Configuration.KEYBOARD_12KEY);
            alertDialog.setView(input);
            alertDialog.setPositiveButton("המשך", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String userTZ = input.getText().toString();

                    if (userTZ == null || userTZ.length() != 9) {
                        Toast.makeText(getApplicationContext(), "תז לא חוקית, אנא נסה שוב", Toast.LENGTH_LONG).show();
                        return;
                    } else { //VALID USER TZ
                        DatabaseReference ordersRef = new FireBaseDBOrder().getOrdersListFromDB();
                        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    if (data.child("userTZ").getValue(String.class).equals(userTZ) && data.child("arrivedToUser").getValue().equals(true)) { // the user has orders to return
                                        hasOrder = true;
                                        break;
                                    }
                                }
                                if (!hasOrder) {
                                    Toast.makeText(getApplicationContext(), "למנוי זה אין הזמנות להחזרה במערכת. נסה שוב!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                else {
                                    Intent intent = new Intent(MenuAdminActivity.this, ReturnBookActivity.class);
                                    intent.putExtra("userTZ", userTZ);
                                    startActivity(intent);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });

                    } //VALID USER TZ
                }//onClick
            });//positiveButton
            alertDialog.setNegativeButton("בטל",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }

        if (v == clientsList_btn) {
        }

        if (v == add_btn) {
            Intent intent = new Intent(MenuAdminActivity.this, AddBookActivity.class);
            startActivity(intent);
        }

        if(v == finishOrder_btn){
            Intent intent = new Intent(MenuAdminActivity.this, CompletesOrdersActivity.class);
            startActivity(intent);

        }

    }

}