package com.example.thelibrary.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.example.thelibrary.fireBase.model.FireBaseDBShoppingList;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BookAdminAdapter extends BaseAdapter {
    Context context;
    ArrayList<BookObj> books;
    AppCompatActivity activity;


    public BookAdminAdapter(AppCompatActivity activity, Context context, ArrayList<BookObj> books) {
        super();
        this.context = context;
        this.books = books;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // inflate the layout for each item of listView
        BookObj book = books.get(position);
        String bookID = book.getId();

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.single_book_amdin_row, parent, false);

        // get the reference of textView and button
        FloatingActionButton addBookToOrder;
        TextView bookNameTextView, authorNameTextView, genreTextView, amountTextView;

        addBookToOrder = view.findViewById(R.id.addBookToStock);
        bookNameTextView = view.findViewById(R.id.singleAdminName);
        genreTextView = view.findViewById(R.id.singleAdminGenre);
        authorNameTextView = view.findViewById(R.id.singleAdminAuthor);
        amountTextView = view.findViewById(R.id.singleAdminAmount);

        // Set the title and button name
        authorNameTextView.append(book.getauthor());
        bookNameTextView.append(book.getName());
        genreTextView.append(book.getgenre());
        amountTextView.append(book.getAmount()+"");

        addBookToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = book.getAmount();
                if (amount > 0) {
                    new FireBaseDBShoppingList().addBookToShoppingList(bookID);
                    new FireBaseDBBook().setAmount(bookID, (amount - 1)); //update in fireBase
                    book.setAmount(amount - 1);
                    Toast.makeText(context.getApplicationContext(), "הספר נוסף להזמנה שלך", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "הספר לא זמין במלאי", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addBookToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Add To Stock");
                alertDialog.setMessage("הכנס את כמות הספרים שברצונך להוסיף למלאי:");

                final EditText input = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alertDialog.setView(input);
                alertDialog.setPositiveButton("הוסף למלאי",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String amountToAdd = input.getText().toString();
                                if (amountToAdd.equals("")){
                                    Toast.makeText(context.getApplicationContext(),"Invalid Amount",Toast.LENGTH_LONG).show();
                                    return;
                                }
                                else{
                                    new FireBaseDBBook().setAmount(bookID, (book.getAmount() +Integer.parseInt(amountToAdd))); //update in fireBase
                                    book.setAmount(book.getAmount() +Integer.parseInt(amountToAdd));
                                    Toast.makeText(context.getApplicationContext(), "העדכון בוצע בהצלחה", Toast.LENGTH_LONG).show();
                                }
                            }});
                alertDialog.setNegativeButton("בטל",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent bookDetails = new Intent(activity, MenuUserActivity.class); //#########CHANGE TO DETAILS##########
                bookDetails.putExtra("bookID", bookID);
                activity.startActivity(bookDetails);
            }
        });

        return view;
    }


}
