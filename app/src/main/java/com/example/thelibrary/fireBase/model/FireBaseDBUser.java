package com.example.thelibrary.fireBase.model;

import com.example.thelibrary.fireBase.model.dataObj.UserObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
    public UserObj getUserObjFromDBByID(String userID){
        final UserObj[] u = new UserObj[1];
        getUserFromDB(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 u[0] = dataSnapshot.getValue(UserObj.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return u[0];
    }
}
