package com.example.thelibrary.fireBase.model;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseModel  {
    DatabaseReference myRef;

    public FireBaseModel(){
        myRef= FirebaseDatabase.getInstance().getReference();
    }
}
