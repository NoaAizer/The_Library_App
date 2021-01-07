package com.example.thelibrary.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.BookDetailsActivity;
import com.example.thelibrary.fireBase.model.FireBaseDBUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteBookAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> books;
    int layoutResourceId;

    public FavoriteBookAdapter(Context context, int layoutResourceId,  ArrayList<String> books) {
        super();
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.books = books;
    }

    @Override
    public int getCount() { return books.size(); }

    @Override
    public String getItem(int position) {
        int c=0;
        for(String book : books) {
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

            holder.nameText = (TextView) row.findViewById(R.id.singleFavName);
            holder.authorText = (TextView) row.findViewById(R.id.singleFavAuthor);
            holder.genreText  = (TextView) row.findViewById(R.id.singleFavGenre);
            holder.rateText  = (TextView) row.findViewById(R.id.singleFavRate);
            holder.stockText  = (TextView) row.findViewById(R.id.singleFavStock);
            holder.removeBtn = (Button) row.findViewById(R.id.singleFavRemoveBtn);
            row.setTag(holder);

        } else {
            holder = (AppInfoHolder) row.getTag();
        }

        AppInfoHolder finalHolder = holder;


        String bookID =getItem(position);


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               DataSnapshot bookDS= dataSnapshot.child("books").child(bookID);
                finalHolder.nameText.setText(bookDS.child("name").getValue(String.class));
                finalHolder.authorText.setText("  " + bookDS.child("author").getValue(String.class));
                finalHolder.genreText.setText("  " + bookDS.child("genre").getValue(String.class));
               //???NEED TO BE CHANGE????????? finalHolder.rateText.append("  " + dataSnapshot.child("rate").getValue(String.class));
               if(bookDS.child("amount").getValue(Integer.class)>0)
                finalHolder.stockText.setText(" כן" );
               else
                   finalHolder.stockText.setText(" לא" );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Remove Book From Favorite List
        finalHolder.removeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new FireBaseDBUser().removeFromFavorites(bookID);
                books.remove(position);
                FavoriteBookAdapter.this.notifyDataSetChanged();

            }
        });
        // Move to book details
        row.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent bookDetails = new Intent(context, BookDetailsActivity.class);
                bookDetails.putExtra("bookID", bookID);
                context.startActivity(bookDetails);
            }
        });
        return row;
    }

    static class AppInfoHolder {
        TextView nameText, authorText ,genreText, rateText , stockText;
        Button removeBtn;

    }
}
