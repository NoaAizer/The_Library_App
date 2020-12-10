package com.example.thelibrary.fireBase.model;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.example.thelibrary.fireBase.model.dataObj.ShoppingListObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FireBaseDBShoppingList extends FireBaseModel {

    public String addShoppingListToDB(String userID) {
        return writeNewShoppingList(userID);
    }

    public String writeNewShoppingList(String userID) {
        ShoppingListObj shop = new ShoppingListObj(userID);
        String keyId = myRef.push().getKey();
        myRef.child("shoppingList").child(keyId).setValue(shop);
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

    public void addBookToShoppingList(String bookID) {
        final String[] shopID = new String[1];
        final ArrayList<String>[] bookList = new ArrayList[1];
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopID[0] = dataSnapshot.child("users").child(userID).child("shoppingID").getValue(String.class);
                bookList[0] = (ArrayList<String>) dataSnapshot.child("shoppingList").child(shopID[0]).child("bookList").getValue();
                if (bookList[0] == null) // there are no books in the shopping list of the user
                    bookList[0] = new ArrayList<>();
                bookList[0].add(bookID);
                myRef.child("shoppingList").child(shopID[0]).child("bookList").setValue(bookList[0]);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }



    public void removeBookFromShoppingList(String bookID, BookObj book, AppCompatActivity activity) {
        final String[] shopID = new String[1];
        final ArrayList<String>[] bookList = new ArrayList[1];
        final Long[] amount = new Long[1];
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shopID[0] = dataSnapshot.child("users").child(userID).child("shoppingID").getValue(String.class);
                bookList[0] = (ArrayList<String>) dataSnapshot.child("shoppingList").child(shopID[0]).child("bookList").getValue();

                if (bookList[0] != null && bookList[0].contains(bookID)) {
                    bookList[0].remove(bookID);
                    amount[0]= dataSnapshot.child("books").child(bookID).child("amount").getValue(Long.class);//UPDATE AMOUNT
                    myRef.child("books").child(bookID).child("amount").setValue((amount[0])+1);
                    book.setAmount(amount[0].intValue() + 1);// update the amount of book by 1
                    Toast.makeText(activity.getApplicationContext(), "הספר הוסר מההזמנה שלך", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(activity.getApplicationContext(), "הספר לא קיים ברשימת ההזמנות שלך", Toast.LENGTH_SHORT).show();
                myRef.child("shoppingList").child(shopID[0]).child("bookList").setValue(bookList[0]);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
