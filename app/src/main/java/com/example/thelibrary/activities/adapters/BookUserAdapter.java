package com.example.thelibrary.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.MenuUserActivity;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.example.thelibrary.fireBase.model.FireBaseDBShoppingList;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BookUserAdapter extends BaseAdapter {
    Context context;
    ArrayList<BookObj> books;
    AppCompatActivity activity;


    //    public bookAdapter(Context context, ArrayList<BookObj> books) {
//        super(context, R.layout.singke_book_line, R.id.singleName ,R.id.addBookToOrder, books);
//        this.context = context;
//        this.books = books;
//    }
    public BookUserAdapter(AppCompatActivity activity, Context context, ArrayList<BookObj> books) {
        super();
        this.context = context;
        this.books = books;
        this.activity = activity;
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
        view = layoutInflater.inflate(R.layout.single_book_row, parent, false);

        // get the reference of textView and button
        FloatingActionButton addBookToOrder, removeBookFromOrder;
        TextView bookNameTextView, authorNameTextView, genreTextView;

        addBookToOrder = view.findViewById(R.id.addBookToOrder);
        removeBookFromOrder = view.findViewById(R.id.removeBookFromOrder);
        bookNameTextView = view.findViewById(R.id.singleName);
        genreTextView = view.findViewById(R.id.singleGenre);
        authorNameTextView = view.findViewById(R.id.singleAuthor);

        // Set the title and button name
        authorNameTextView.append(book.getauthor());
        bookNameTextView.append(book.getName());
        genreTextView.append(book.getgenre());

        addBookToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = book.getAmount();
                if (amount > 0) {
                    new FireBaseDBShoppingList().addBookToShoppingList(bookID);
                    new FireBaseDBBook().setAmount(bookID, (amount - 1)); //update in fireBase
                    book.setAmount(amount - 1);
                    Toast.makeText(activity.getApplicationContext(), "הספר נוסף להזמנה שלך", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity.getApplicationContext(), "הספר לא זמין במלאי", Toast.LENGTH_SHORT).show();
                }
            }
        });
        removeBookFromOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FireBaseDBShoppingList fsl = new FireBaseDBShoppingList();
                fsl.removeBookFromShoppingList(bookID, book, activity);
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
