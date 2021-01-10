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

public class LateListAdapter extends BaseAdapter {
    Context context;
    int layoutResourceId;
    ArrayList<String> lates;

    public LateListAdapter(Context context, int layoutResourceId, ArrayList<String> lates) {
        super();
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.lates = lates;
    }

    @Override
    public int getCount() { return lates.size(); }

    @Override
    public String getItem(int position) {
        int c=0;
        for(String late : lates) {
            if (c == position) return late;
            c++;
        }
        return null;
    }

    @Override
    public long getItemId(int position) { return position;  }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LateListAdapter.AppInfoHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new LateListAdapter.AppInfoHolder();

            holder.orderIDText  = (TextView) row.findViewById(R.id.singleOrderId);
            holder.nameText  = (TextView) row.findViewById(R.id.singleFlName);
            holder.phoneText  = (TextView) row.findViewById(R.id.singlePhonenum);
            holder.endText  = (TextView) row.findViewById(R.id.singleEndDate);
            holder.bookListText  = (TextView) row.findViewById(R.id.singleListBooks);
            row.setTag(holder);

        } else {
            holder = (LateListAdapter.AppInfoHolder) row.getTag();
        }
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        LateListAdapter.AppInfoHolder finalHolder = holder;

        String orderID = getItem(position);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                finalHolder.orderIDText.append("  " + orderID);
                String userID = dataSnapshot.child("orders").child(orderID).child("userID").getValue(String.class);
                String fName = dataSnapshot.child("users").child(userID).child("firstName").getValue(String.class);
                String lName = dataSnapshot.child("users").child(userID).child("lastName").getValue(String.class);
                finalHolder.nameText.append("  " + fName + " " + lName);
                finalHolder.phoneText.append("  " + dataSnapshot.child("users").child(userID).child("phone").getValue(String.class));
                finalHolder.endText.append("  " + dataSnapshot.child("orders").child(orderID).child("endOfOrder").getValue(String.class));
                ArrayList<String> bookList = (ArrayList<String>) dataSnapshot.child("orders").child(orderID).child("listOfBooks").getValue();
                String ListOfNames = "";
                for(int i=0; i<bookList.size(); i++)
                {
                    ListOfNames += dataSnapshot.child("books").child(bookList.get(i)).child("name").getValue(String.class) + "\n";
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
        TextView orderIDText, nameText, phoneText, endText, bookListText;
    }
}
