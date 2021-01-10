package com.example.thelibrary.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.thelibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BorrowListAdminAdapter extends BaseAdapter  implements Filterable {
    Context context;
    ArrayList<Pair<Pair<String,String>,String>> books;
    int layoutResourceId;
    protected  ArrayList<Pair<Pair<String,String>,String>> filteredData;
    private ItemFilter mFilter = new ItemFilter();

    public BorrowListAdminAdapter(Context context, int layoutResourceId,  ArrayList<Pair<Pair<String,String>,String>> books) {
        super();
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.books = books;
        filteredData=books;
      //  getFilter();
    }


    @Override
    public int getCount() { return books.size(); }

    @Override
    public Pair<Pair<String,String>,String> getItem(int position) {
        int c=0;
        for(Pair<Pair<String,String>,String> book : books) {
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
            holder.bookIDText  = (TextView) row.findViewById(R.id.singleBorrowID);
            holder.endOfOrderText  = (TextView) row.findViewById(R.id.singleBorrowDate);
            holder.amountText  = (TextView) row.findViewById(R.id.singleBorrowAmount);
            row.setTag(holder);

        } else {
            holder = (AppInfoHolder) row.getTag();
        }
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        AppInfoHolder finalHolder = holder;

        String orderID = getItem(position).first.first;
        String bookID = getItem(position).first.second;
        String bookName = getItem(position).second;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                finalHolder.nameText.setText(bookName);
                finalHolder.authorText.setText("  " + dataSnapshot.child("books").child(bookID).child("author").getValue(String.class));
                finalHolder.bookIDText.setText("  " + bookID);
                finalHolder.endOfOrderText.setText("  " + dataSnapshot.child("orders").child(orderID).child("endOfOrder").getValue(String.class));
                finalHolder.amountText.setText("  " + dataSnapshot.child("books").child(bookID).child("amount").getValue(Long.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return row;
    }


    static class AppInfoHolder {
    TextView nameText, authorText ,bookIDText, endOfOrderText , amountText;
}

    public Filter getFilter() {
        if(mFilter==null) {

            mFilter=new ItemFilter();
        }

        return mFilter;
    }

private class ItemFilter extends Filter {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint!=null && constraint.length()>0){
            ArrayList<Pair<Pair<String,String>,String>> filterList=new ArrayList<Pair<Pair<String,String>,String>>();
            for(int i=0;i<filteredData.size();i++){
                if((filteredData.get(i).second)
                        .contains(constraint.toString())) {
                    filterList.add(filteredData.get(i));
                }
            }
            results.count=filterList.size();
            results.values=filterList;
        }else{
            results.count=filteredData.size();
            results.values=filteredData;
        }
        return results;
    }
    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        books = (ArrayList<Pair<Pair<String,String>,String>>) results.values;
        notifyDataSetChanged();
    }

}
}


