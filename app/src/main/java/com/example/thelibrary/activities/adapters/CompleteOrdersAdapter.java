package com.example.thelibrary.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.dataObj.OrderObj;

import java.util.ArrayList;

public class CompleteOrdersAdapter extends BaseAdapter {
    Context context;
    ArrayList<OrderObj> orders;


    public CompleteOrdersAdapter(Context context, ArrayList<OrderObj> orders) {
        super();
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // inflate the layout for each item of listView
        OrderObj order = orders.get(position);
        String orderID = order.getId();

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.single_order_complete_row, parent, false);

        // get the reference of textView and button
        TextView orderIDTextView, orderTypeTextView, orderDateTextView, userIDTextView;

        orderDateTextView = view.findViewById(R.id.singleCDate);
        orderTypeTextView = view.findViewById(R.id.singleCType);
        orderIDTextView = view.findViewById(R.id.singleCOrderID);
        userIDTextView = view.findViewById(R.id.singleCUserID);


        // Set the title and button name
        orderDateTextView.append(order.getEndOfOrder().toString());
        orderTypeTextView.append(order.getCollect());
        orderIDTextView.append(orderID);
        userIDTextView.append(order.getUserTZ());

        return view;
    }


}
