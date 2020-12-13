package com.example.thelibrary.fireBase.model;

import com.example.thelibrary.fireBase.model.dataObj.UserObj;
import com.google.firebase.database.DatabaseReference;

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
}
