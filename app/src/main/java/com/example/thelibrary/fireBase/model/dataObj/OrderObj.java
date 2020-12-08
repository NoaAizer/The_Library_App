package com.example.thelibrary.fireBase.model.dataObj;

public class OrderObj {

    private BookObj[] listOfBooks;
    private String userID;
    private String collect; // TA or deliver
    private String endOfOrder;
    private boolean complete;

    public OrderObj() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public OrderObj(BookObj[] list, String userID, String collect, String endOfOrder)
    {
        this.listOfBooks = null;
        this.userID = userID;
        this.collect = collect;
        this.endOfOrder = endOfOrder;
        this.complete = false;
    }

    public BookObj[] getListOfBooks()
    {
        return listOfBooks;
    }
    public void addBookToList(BookObj b)
    {
        // if numOfBooks < lengthOfList
        // error
        // else
        // add the book to the list
    }
    public String getUserID()
    {
        return userID;
    }
    public String getCollect()
    {
        return collect;
    }
    public String getEndOfOrder()
    {
        return endOfOrder;
    }

    public boolean getComplete()
    {
        return complete;
    }
    public void setComplete(boolean com)
    {
        this.complete = com;
    }
}
