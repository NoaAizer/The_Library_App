package com.example.thelibrary.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.UserDetailsAdminActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserListAdapter extends BaseAdapter {
    Context context;
    int layoutResourceId;
    ArrayList<String> users;
    ArrayList<String> usersTZ;
    ArrayList<String> filteredData;
    private ItemFilter mFilter = new ItemFilter();

    public UserListAdapter(Context context, int layoutResourceId, ArrayList<String> users, ArrayList<String> usersTZ) {
        super();
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.users = users;
        this.usersTZ = usersTZ;
        this.filteredData = usersTZ;
    }

    @Override
    public int getCount() { return users.size();   }

    @Override
    public String getItem(int position) {
        int c=0;
        for(String user : users) {
            if (c == position) return user;
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
        UserListAdapter.AppInfoHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new UserListAdapter.AppInfoHolder();

            holder.nameText  = (TextView) row.findViewById(R.id.single_Name);
            holder.IDText  = (TextView) row.findViewById(R.id.single_ID);
            holder.subscriptionText  = (TextView) row.findViewById(R.id.single_subscription);
            row.setTag(holder);

        } else {
            holder = (UserListAdapter.AppInfoHolder) row.getTag();
        }
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users");
        UserListAdapter.AppInfoHolder finalHolder = holder;

        String userID = getItem(position);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fName = dataSnapshot.child(userID).child("firstName").getValue(String.class);
                String lName = dataSnapshot.child(userID).child("lastName").getValue(String.class);
                finalHolder.nameText.append("  " + fName + " " + lName);
                finalHolder.IDText.append("  " + dataSnapshot.child(userID).child("tz").getValue(String.class));
                finalHolder.subscriptionText.append("  " + dataSnapshot.child(userID).child("subscription").getValue(String.class) + "\n");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        row.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent userDetails = new Intent(context, UserDetailsAdminActivity.class);
                userDetails.putExtra("userID", userID);
                context.startActivity(userDetails);
            }
        });
        return row;
    }

    static class AppInfoHolder {
        TextView nameText, IDText, subscriptionText;
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
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> filterList = new ArrayList<>();
                for (int i = 0; i < filteredData.size(); i++) {
                    if ((filteredData.get(i)).contains(constraint.toString())) {
                        filterList.add(filteredData.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = filteredData.size();
                results.values = filteredData;
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            usersTZ = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
