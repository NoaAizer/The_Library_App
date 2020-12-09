package com.example.thelibrary.fireBase.model;

import com.example.thelibrary.fireBase.model.dataObj.ShoppingListObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseDBShoppingList extends FireBaseModel {

    public void addShoppingListToDB(String userID){
        writeNewShoppingList(userID);
    }
    public void writeNewShoppingList(String userID){
        ShoppingListObj shop = new ShoppingListObj(userID);
        myRef=myRef.child("shoppingList");
        String keyId= myRef.push().getKey();
        myRef.child(keyId).setValue(shop);

    }
    public DatabaseReference getShoppingListFromDB(String shopID) {
        return myRef.getRef().child("shoppingList").child(shopID);
    }


    public void clearShopListDB(String shopID){
        DatabaseReference shop = myRef.getRef().child("shoppingList").child(shopID).child("bookList");
        shop.setValue(new ArrayList<String>());
    }

    public ShoppingListObj getShoppingListByUserID(String userID){
        final ShoppingListObj[] sl = new ShoppingListObj[1];
        getShoppingListFromDB(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sl[0] = dataSnapshot.getValue(ShoppingListObj.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return sl[0];
    }
}
