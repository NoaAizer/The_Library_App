package com.example.thelibrary.fireBase.model;

import com.example.thelibrary.fireBase.model.dataObj.UserObj;
import com.google.firebase.database.DatabaseReference;

public class FireBaseDBUser extends FireBaseModel {
    public void addUserToDB(String firstName,String lastName,String email,String password,String address, String phone,String subscription, String id){
        writeNewUser(firstName,lastName,email,password,address,phone,subscription, id);
    }
    public void writeNewUser(String firstName,String lastName,String email,String password,String address, String phone,String subscription, String id){
        UserObj user = new UserObj(firstName,lastName,email,password,address,phone,subscription);
              myRef.child("users").child(id).setValue(user);
    }
    public DatabaseReference getUserFromDB(String userID){
        return myRef.getRef().child("users").child(userID);
    }
}
