package com.example.thelibrary.fireBase.model;

import com.example.thelibrary.fireBase.model.dataObj.ShoppingListObj;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FireBaseDBShoppingList extends FireBaseModel {

    public String addShoppingListToDB(String userID) {
        return writeNewShoppingList(userID);
    }

    public String writeNewShoppingList(String userID) {
        ShoppingListObj shop = new ShoppingListObj(userID);
        myRef = myRef.child("shoppingList");
        String keyId = myRef.push().getKey();
        myRef.child(keyId).setValue(shop);
        return keyId;


    }

    public DatabaseReference getShoppingListFromDB(String shopID) {
        return myRef.getRef().child("shoppingList").child(shopID);
    }


    public void clearShopListDB(String shopID) {
        DatabaseReference shop = myRef.child("shoppingList").child(shopID).child("bookList");
        shop.setValue(new ArrayList<String>());
    }

    public DatabaseReference getShoppingListFromDBByUserID(String userID) {
        return myRef.child("shoppingList").child("userID").child(userID);
    }
}
