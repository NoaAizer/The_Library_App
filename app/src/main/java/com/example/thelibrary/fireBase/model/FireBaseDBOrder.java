package com.example.thelibrary.fireBase.model;

import com.example.thelibrary.fireBase.model.dataObj.OrderObj;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FireBaseDBOrder extends FireBaseModel {
    public void addOrderToDB(ArrayList<String> list, String userID, String collect, String endOfOrder){
        writeNewOrder(list, userID, collect, endOfOrder);
    }
    public void writeNewOrder(ArrayList<String> list, String userID, String collect, String endOfOrder){
        OrderObj order = new OrderObj(list, userID, collect, endOfOrder);
        String keyId= myRef.push().getKey();
        myRef.child("orders").child(keyId).setValue(order);
    }
    public DatabaseReference getOrderFromDB(String orderID){
        return myRef.getRef().child("orders").child(orderID);
    }
}
