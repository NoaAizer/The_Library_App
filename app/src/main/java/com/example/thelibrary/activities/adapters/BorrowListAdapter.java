package com.example.thelibrary.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.thelibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BorrowListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Pair<String,String>> books;
    private String[] mKeys;
    int layoutResourceId;

    public BorrowListAdapter(Context context, int layoutResourceId,  ArrayList<Pair<String,String>> books) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AppInfoHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AppInfoHolder();

            holder.nameText = (TextView) row.findViewById(R.id.singleBorrowName);
            holder.authorText = (TextView) row.findViewById(R.id.singleBorrowAuthor);
            holder.orderIDText  = (TextView) row.findViewById(R.id.singleBorrowOrderID);
            holder.endOfOrderText  = (TextView) row.findViewById(R.id.singleBorrowDate);
            holder.statusOrderText  = (TextView) row.findViewById(R.id.singleBorrowStatus);
            row.setTag(holder);

        } else {
            holder = (AppInfoHolder) row.getTag();
        }
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        AppInfoHolder finalHolder = holder;

        String orderID = getItem(position).first;
        String bookID =getItem(position).second;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                finalHolder.nameText.setText(dataSnapshot.child("books").child(bookID).child("name").getValue(String.class));
                finalHolder.authorText.append("  " + dataSnapshot.child("books").child(bookID).child("author").getValue(String.class));
                finalHolder.orderIDText.append("  " + orderID);
                finalHolder.endOfOrderText.append("  " + dataSnapshot.child("orders").child(orderID).child("endOfOrder").getValue(String.class));
                finalHolder.statusOrderText.append("  " + dataSnapshot.child("orders").child(orderID).child("collect").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return row;
    }

    static class AppInfoHolder {
        TextView nameText, authorText ,orderIDText, endOfOrderText , statusOrderText;
    }
}
