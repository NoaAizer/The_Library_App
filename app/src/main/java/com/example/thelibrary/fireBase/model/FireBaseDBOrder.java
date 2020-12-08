package com.example.thelibrary.fireBase.model;

import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.example.thelibrary.fireBase.model.dataObj.OrderObj;
import com.google.firebase.database.DatabaseReference;

public class FireBaseDBOrder extends FireBaseModel {
    public void addOrderToDB(BookObj[] list, String userID, String collect, String endOfOrder){
        writeNewOrder(list, userID, collect, endOfOrder);
    }
    public void writeNewOrder(BookObj[] list, String userID, String collect, String endOfOrder){
        OrderObj order = new OrderObj(list, userID, collect, endOfOrder);
        myRef=myRef.child("orders");
        String keyId= myRef.push().getKey();
        myRef.child(keyId).setValue(order);
    }
    public DatabaseReference getOrderFromDB(String orderID){
        return myRef.getRef().child("orders").child(orderID);
    }
}
