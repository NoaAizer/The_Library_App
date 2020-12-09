package com.example.thelibrary.fireBase.model;

import com.example.thelibrary.fireBase.model.dataObj.UserObj;
import com.google.firebase.database.DatabaseReference;

public class FireBaseDBUser extends FireBaseModel {
    public void addUserToDB(String firstName,String lastName,String email,String password,String address, String phone,String subscription, String id){
        writeNewUser(firstName,lastName,email,password,address,phone,subscription, id);
    }
    public void writeNewUser(String firstName,String lastName,String email,String password,String address, String phone,String subscription, String id){
        UserObj user = new UserObj(firstName,lastName,email,password,address,phone,subscription);
      String shopListId= new  FireBaseDBShoppingList().addShoppingListToDB(id);
      user.setShoppingListId(shopListId);
              myRef.child("users").child(id).setValue(user);

    }
    public DatabaseReference getUserFromDB(String userID){
        return myRef.child("users").child(userID);
    }
//    public String getUserObjFromDBByID(String userID){
//        final String[] shoppingListID = new String[1];
//        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                    if (userSnapshot.getKey().equals(userID)) {
//                        shoppingListID[0] =  userSnapshot.getValue(UserObj.class).getShoppingID();
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//        return shoppingListID[0];
//    }
}
