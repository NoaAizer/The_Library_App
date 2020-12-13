package com.example.thelibrary.activities;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReturnListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Pair<String,String>> books;
    private String[] mKeys;
    int layoutResourceId;

    public ReturnListAdapter(Context context, int layoutResourceId,  ArrayList<Pair<String,String>> books) {
        super();
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.books = books;
    }

    @Override
    public int getCount() { return books.size(); }

    @Override
    public Pair<String,String> getItem(int position) {
        int c=0;
        for(Pair<String, String> book : books) {
            if (c == position) return book;
            c++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        String orderID = getItem(position).first;
        String bookID =getItem(position).second;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(layoutResourceId, parent, false);


        // get the reference of textView and button
        Button returnBook_btn;
        TextView nameText, authorText, orderIDText, endOfOrderText, statusOrderText;

        returnBook_btn = view.findViewById(R.id.returnBookBtn);
        nameText = view.findViewById(R.id.singleReturnName);
        authorText = view.findViewById(R.id.singleReturnAuthor);
        orderIDText = view.findViewById(R.id.singleReturnOrderID);
        endOfOrderText = view.findViewById(R.id.singleReturnDate);
        statusOrderText = view.findViewById(R.id.singleReturnStatus);



        // Set the title and button name
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(); myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameText.setText(dataSnapshot.child("books").child(bookID).child("name").getValue(String.class));
                authorText.append("  " + dataSnapshot.child("books").child(bookID).child("author").getValue(String.class));
                orderIDText.append("  " + orderID);
                endOfOrderText.append("  " + dataSnapshot.child("orders").child(orderID).child("endOfOrder").getValue(String.class));
                statusOrderText.append("  " + dataSnapshot.child("orders").child(orderID).child("collect").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        returnBook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FireBaseDBOrder().removeBookFromOrderList(orderID,bookID);
                Toast.makeText(context.getApplicationContext(), "הספר הוחזר", Toast.LENGTH_SHORT).show();
                //remove position from the book list
                books.remove(position);
                //let the adapter know that there was a change in the dataset
                notifyDataSetChanged();
            }
        });


        return view;
    }

}
