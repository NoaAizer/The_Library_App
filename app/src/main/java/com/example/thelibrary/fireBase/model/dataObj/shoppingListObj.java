package com.example.thelibrary.fireBase.model.dataObj;

import java.util.ArrayList;

public class shoppingListObj {

    private ArrayList<String> bookList;
    private String UserID;

    public shoppingListObj() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public shoppingListObj(String userID)
    {
        this.bookList = new ArrayList<String>();
        this.UserID = userID;
    }

    public String getUserID()
    {
        return UserID;
    }

    public ArrayList<String> getShoppingList()
    {
        return bookList;
    }

    public void addBook(String bookID)
    {
        bookList.add(bookID);
    }

    public void removeBook(String bookID)
    {
        bookList.remove(bookID);
    }

    public void clearList()
    {
        bookList.clear();
    }
}