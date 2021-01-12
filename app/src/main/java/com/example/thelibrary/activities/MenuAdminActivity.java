package com.example.thelibrary.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MenuAdminActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loansList_btn, booksList_btn, returnBook_btn,
            clientsList_btn, add_btn, laterList_btn;
    private boolean hasOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        loansList_btn = (Button) findViewById(R.id.loansList);
        booksList_btn = (Button) findViewById(R.id.booksList);
        returnBook_btn = (Button) findViewById(R.id.returnBook);
        clientsList_btn = (Button) findViewById(R.id.clientsList);
        add_btn = (Button) findViewById(R.id.adding);
        laterList_btn = (Button) findViewById(R.id.laterList);

        loansList_btn.setOnClickListener(this);
        booksList_btn.setOnClickListener(this);
        returnBook_btn.setOnClickListener(this);
        clientsList_btn.setOnClickListener(this);
        add_btn.setOnClickListener(this);
        laterList_btn.setOnClickListener(this);

        Drawable drawable_book = ContextCompat.getDrawable(MenuAdminActivity.this,R.drawable.icon_books);
        drawable_book.setBounds(0, 0, (int)(drawable_book.getIntrinsicWidth()*0.2),
                (int)(drawable_book.getIntrinsicHeight()*0.2));
        ScaleDrawable sd_book = new ScaleDrawable(drawable_book, 0, 1, 1);
        booksList_btn.setCompoundDrawables(sd_book.getDrawable(), null, null, null);

        Drawable drawable_cart = ContextCompat.getDrawable(MenuAdminActivity.this,R.drawable.icon_cart);
        drawable_cart.setBounds(0, 0, (int)(drawable_cart.getIntrinsicWidth()*0.2),
                (int)(drawable_cart.getIntrinsicHeight()*0.2));
        ScaleDrawable sd_cart = new ScaleDrawable(drawable_cart, 0, 1, 1);
        loansList_btn.setCompoundDrawables(sd_cart.getDrawable(), null, null, null);

        Drawable drawable_hurry = ContextCompat.getDrawable(MenuAdminActivity.this,R.drawable.icon_hurry);
        drawable_hurry.setBounds(0, 0, (int)(drawable_hurry.getIntrinsicWidth()*0.6),
                (int)(drawable_cart.getIntrinsicHeight()*0.2));
        ScaleDrawable sd_hurry = new ScaleDrawable(drawable_hurry, 0, 1, 1);
        laterList_btn.setCompoundDrawables(sd_hurry.getDrawable(), null, null, null);

    }

    public void onClick(View v) {
        if (v == loansList_btn) {
            createOrderDialog();
        }
        if (v == booksList_btn) {
            createBooksDialog();
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
                                } else {
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
            Intent intent = new Intent(MenuAdminActivity.this, UsersListActivity.class);
            startActivity(intent);
        }

        if (v == add_btn) {
            Intent intent = new Intent(MenuAdminActivity.this, com.example.thelibrary.activities.AddBookActivity.class);
            startActivity(intent);
        }

    }

    public void createOrderDialog() {
        final Dialog d = new Dialog(this);
        Button treatmentBtn, completeBtn;
        d.setContentView(R.layout.orders_admin_dialog);
        d.setTitle("מרכז ההזמנות:");
        d.setCancelable(true);

        treatmentBtn = d.findViewById(R.id.treatOrders);
        treatmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuAdminActivity.this, TreatmentOrdersActivity.class);
                startActivity(intent);
            }
        });
        completeBtn = (Button) d.findViewById(R.id.completeOrders);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuAdminActivity.this, CompletesOrdersActivity.class);
                startActivity(intent);
            }

        });
        d.show();

    }
    public void createBooksDialog() {
        final Dialog d = new Dialog(this);
        Button stockBtn, borrowedBtn;
        d.setContentView(R.layout.admin_book_dialog);
        d.setTitle("מאגר הספרים:");
        d.setCancelable(true);

        stockBtn = d.findViewById(R.id.stockBooks);
        stockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuAdminActivity.this, SearchBookActivity.class);
                intent.putExtra("type","admin");
                startActivity(intent);
            }
        });
        borrowedBtn = (Button) d.findViewById(R.id.borrowedBooks);
        borrowedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuAdminActivity.this, BorrowListAdminActivity.class);
                startActivity(intent);
            }

        });
        d.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuLogOut) {
            finish();
            Intent login = new Intent(MenuAdminActivity.this, LoginAdminActivity.class);
            startActivity(login);
        }
        return super.onOptionsItemSelected(item);
    }
}