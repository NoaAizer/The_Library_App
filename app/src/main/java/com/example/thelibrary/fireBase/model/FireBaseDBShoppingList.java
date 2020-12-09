package com.example.thelibrary.fireBase.model;

import com.example.thelibrary.fireBase.model.dataObj.shoppingListObj;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FireBaseDBShoppingList extends FireBaseModel {

    public void addShoppingListToDB(String userID){
        writeNewShoppingList(userID);
    }
    public void writeNewShoppingList(String userID){
        shoppingListObj shop = new shoppingListObj(userID);
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
}
