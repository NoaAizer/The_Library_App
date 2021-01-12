package com.example.thelibrary.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.OrderPageActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TreatmentOrdersAdapter extends BaseAdapter {

    Context context;
    int layoutResourceId;
    ArrayList<String> orders;

    public TreatmentOrdersAdapter(Context context, int layoutResourceId, ArrayList<String> orders) {
        super();
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public String getItem(int position) {
        int c = 0;
        for (String order : orders) {
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

            holder.orderIdText = (TextView) row.findViewById(R.id.single_orderId);
            holder.dateText = (TextView) row.findViewById(R.id.single_date);
            holder.tzText = (TextView) row.findViewById(R.id.single_tz);
            row.setTag(holder);

        } else {
            holder = (AppInfoHolder) row.getTag();
        }
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("orders");
        AppInfoHolder finalHolder = holder;

        String orderID = getItem(position);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                finalHolder.orderIdText.setText("  " + orderID);

                /*String endOrder = dataSnapshot.child(orderID).child("endOfOrder").getValue(String.class);
                SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdformat.parse(endOrder);*/
                finalHolder.dateText.setText("  " + dataSnapshot.child(orderID).child("endOfOrder").getValue(String.class));
                finalHolder.tzText.setText("  " + dataSnapshot.child(orderID).child("userTZ").getValue(String.class) + "\n");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        row.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent orderDetails = new Intent(context, OrderPageActivity.class);
                orderDetails.putExtra("orderID", orderID);
                context.startActivity(orderDetails);
            }
        });
        return row;
    }

    static class AppInfoHolder {
        TextView orderIdText, dateText, tzText;
    }
}
