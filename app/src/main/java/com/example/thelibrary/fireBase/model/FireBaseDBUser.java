package com.example.thelibrary.fireBase.model;

import com.example.thelibrary.fireBase.model.dataObj.UserObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseDBUser extends FireBaseModel {
    public void addUserToDB(String tz,String firstName,String lastName,String email,String password,String address, String phone,String subscription, String id){
        writeNewUser(tz,firstName,lastName,email,password,address,phone,subscription, id);
    }
    public void writeNewUser(String tz,String firstName,String lastName,String email,String password,String address, String phone,String subscription, String id){
        UserObj user = new UserObj(tz,firstName,lastName,email,password,address,phone,subscription);
      String shopListId= new  FireBaseDBShoppingList().addShoppingListToDB(id);
      user.setShoppingListId(shopListId);
              myRef.child("users").child(id).setValue(user);

    }
    public DatabaseReference getUserFromDB(String userID){
        return myRef.child("users").child(userID);
    }
    public DatabaseReference getUsersListRef(){
        return myRef.child("users");
    }

    public void addToFavorites(String bookID){
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println("*******************"+userID);
        getUserFromDB(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserObj user = dataSnapshot.getValue(UserObj.class);
                ArrayList<String> favorites=user.getFavorites();
                favorites.add(bookID);
                System.out.println("*******************"+favorites.toString());
                getUserFromDB(userID).child("favorites").setValue(favorites);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void removeFromFavorites(String bookID){
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserFromDB(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserObj user = dataSnapshot.getValue(UserObj.class);
                ArrayList<String> favorites=user.getFavorites();
                if(!favorites.contains(bookID)) return;
                favorites.remove(bookID);
                getUserFromDB(userID).child("favorites").setValue(favorites);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
