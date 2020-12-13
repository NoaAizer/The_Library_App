package com.example.thelibrary.fireBase.model;

import androidx.annotation.NonNull;

import com.example.thelibrary.fireBase.model.dataObj.OrderObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseDBOrder extends FireBaseModel {
    public void addOrderToDB(ArrayList<String> list, String userTZ,String userID, String collect, String endOfOrder){
        writeNewOrder(list, userTZ,userID, collect, endOfOrder);
    }
    public void writeNewOrder(ArrayList<String> list, String userTZ,String userID, String collect, String endOfOrder){
        OrderObj order = new OrderObj(list,userTZ, userID, collect, endOfOrder);
        String keyId= myRef.push().getKey();
        myRef.child("orders").child(keyId).setValue(order);
    }
    public DatabaseReference getOrderFromDB(String orderID){
        return myRef.getRef().child("orders").child(orderID);
    }

    public DatabaseReference getOrdersListFromDB(){
        return myRef.child("orders");
    }

    public void removeBookFromOrderList(String orderID, String bookID) {
        final ArrayList<String>[] bookList = new ArrayList[1];
        final Long[] amountBook = new Long[1];
        final Long[] amountUser = new Long[1];
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookList[0] = (ArrayList<String>) dataSnapshot.child("orders").child(orderID).child("listOfBooks").getValue();
                    bookList[0].remove(bookID);
                    amountBook[0]= dataSnapshot.child("books").child(bookID).child("amount").getValue(Long.class);//UPDATE BOOK AMOUNT
                    myRef.child("books").child(bookID).child("amount").setValue((amountBook[0])+1);
                amountUser[0]= dataSnapshot.child("users").child(userID).child("amountOfBooksRemains").getValue(Long.class);//UPDATE USER REMAINS BOOK AMOUNT
                    myRef.child("users").child(userID).child("amountOfBooksRemains").setValue((amountUser[0])+1);

                if( bookList[0].isEmpty())
                    myRef.child("orders").child(orderID).removeValue();
                else
                    myRef.child("orders").child(orderID).child("listOfBooks").setValue(bookList[0]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
