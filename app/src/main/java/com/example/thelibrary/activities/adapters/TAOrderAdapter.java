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

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

public class TAOrderAdapter extends BaseAdapter {
    Context context;
    int layoutResourceId;
    ArrayList<String> orders;

    public TAOrderAdapter(Context context, int layoutResourceId, ArrayList<String> orders) {
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
       AppInfoHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AppInfoHolder();

            holder.orderIDText  = (TextView) row.findViewById(R.id.singelOrder);
            holder.dateText  = (TextView) row.findViewById(R.id.singleAddedDate);
            holder.userTZText  = (TextView) row.findViewById(R.id.userTZ);
            holder.statusText  = (TextView) row.findViewById(R.id.singleStatus);
            holder.userPhone  = (TextView) row.findViewById(R.id.userPhone);
            row.setTag(holder);

        } else {
            holder = (AppInfoHolder) row.getTag();
        }
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        AppInfoHolder finalHolder = holder;

        String orderID = getItem(position);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                finalHolder.orderIDText.setText("  " + orderID);
                finalHolder.dateText.setText(""+LocalDate.parse(dataSnapshot.child("orders").child(orderID).child("endOfOrder").getValue(String.class)).minusDays(30));
                finalHolder.statusText.setText(dataSnapshot.child("orders").child(orderID).child("statusDeliver").getValue(String.class));
                finalHolder.userTZText.setText(dataSnapshot.child("orders").child(orderID).child("userTZ").getValue(String.class));
                String userID =dataSnapshot.child("orders").child(orderID).child("userID").getValue(String.class);
                finalHolder.userPhone.setText(dataSnapshot.child("users").child(userID).child("phone").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return row;
    }

    static class AppInfoHolder {
        TextView orderIDText, dateText, userTZText, userPhone, statusText;
    }
}
