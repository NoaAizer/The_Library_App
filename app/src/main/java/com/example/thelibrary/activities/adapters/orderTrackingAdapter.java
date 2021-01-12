package com.example.thelibrary.activities.adapters;

import android.app.Activity;
import android.content.Context;
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

public class orderTrackingAdapter extends BaseAdapter {
    Context context;
    int layoutResourceId;
    ArrayList<String> orders;

    public orderTrackingAdapter(Context context, int layoutResourceId, ArrayList<String> orders) {
        super();
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public String getItem(int position) {
        int c=0;
        for(String order : orders) {
            if (c == position) return order;
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
        orderTrackingAdapter.AppInfoHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new orderTrackingAdapter.AppInfoHolder();

            holder.orderIDText  = (TextView) row.findViewById(R.id.singelOrder);
            holder.statusText  = (TextView) row.findViewById(R.id.singleStatus);
            holder.bookListText  = (TextView) row.findViewById(R.id.bookList);
            row.setTag(holder);

        } else {
            holder = (orderTrackingAdapter.AppInfoHolder) row.getTag();
        }
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        orderTrackingAdapter.AppInfoHolder finalHolder = holder;

        String orderID = getItem(position);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                finalHolder.orderIDText.append("  " + orderID);
                finalHolder.statusText.append(dataSnapshot.child("orders").child(orderID).child("statusDeliver").getValue(String.class));
                ArrayList<String> bookList = (ArrayList<String>) dataSnapshot.child("orders").child(orderID).child("listOfBooks").getValue();
                String ListOfNames = "";
                for(int i=0; i<bookList.size(); i++)
                {
                    ListOfNames += "- "+dataSnapshot.child("books").child(bookList.get(i)).child("name").getValue(String.class) + "\n";
                }
                finalHolder.bookListText.append("  " + ListOfNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return row;
    }

    static class AppInfoHolder {
        TextView orderIDText, statusText, bookListText;
    }
}
