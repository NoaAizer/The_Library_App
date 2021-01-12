package com.example.thelibrary.activities;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.thelibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderListAdapter extends ArrayAdapter<String> implements CompoundButton.OnCheckedChangeListener {
    SparseBooleanArray mCheckStates;
    Context context;
    ArrayList<String> books;
    int layoutResourceId;


    public OrderListAdapter(Context context, int layoutResourceId, ArrayList<String> books) {
        super(context, layoutResourceId, books);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.books = books;
        mCheckStates = new SparseBooleanArray(books.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AppInfoHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AppInfoHolder();

            holder.txtTitle1 = (TextView) row.findViewById(R.id.singleOrderName);
            holder.txtTitle2 = (TextView) row.findViewById(R.id.singleOrderAuthor);
            holder.chkSelect = (CheckBox) row.findViewById(R.id.menu_source_check_box);
            row.setTag(holder);

        } else {
            holder = (AppInfoHolder) row.getTag();
        }
        String bookID = books.get(position);
        final String[] bookName = new String[1];
        final String[] authorName = new String[1];
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        AppInfoHolder finalHolder = holder;

        myRef.child("books").child(bookID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                finalHolder.txtTitle1.setText( dataSnapshot.child("name").getValue(String.class));
                finalHolder.txtTitle2.setText("  " + dataSnapshot.child("author").getValue(String.class));
                finalHolder.chkSelect.setTag(position);
                finalHolder.chkSelect.setChecked(mCheckStates.get(position, false));
                finalHolder.chkSelect.setOnCheckedChangeListener(OrderListAdapter.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return row;
    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);

    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {

        mCheckStates.put((Integer) buttonView.getTag(), isChecked);

    }

    static class AppInfoHolder {
        TextView txtTitle1, txtTitle2;
        CheckBox chkSelect;

    }
}
