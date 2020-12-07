package com.example.thelibrary.fireBase.model;

import com.example.thelibrary.fireBase.model.dataObj.UserObj;
import com.google.firebase.database.DatabaseReference;

public class FireBaseDBUser extends FireBaseModel {
    public void addUserToDB(String firstName,String lastName,String email,String password,String address, String phone){
        writeNewUser(firstName,lastName,email,password,address,phone);
    }
    public void writeNewUser(String firstName,String lastName,String email,String password,String address, String phone){
        UserObj user = new UserObj(firstName,lastName,email,password,address,phone);
        myRef=myRef.child("users");
        String keyId= myRef.push().getKey();
        myRef.child(keyId).setValue(user);
    }
    public DatabaseReference getUserFromDB(String userID){
        return myRef.getRef().child("users").child(userID);
    }
}
