package com.example.thelibrary.fireBase.model.dataObj;

public class OrderObj {

    private String orderID;
    private String[] listOfBooks;
    private int userID;
    private String collect; // TA or deliver
    private String endOfOrder;
    private boolean complete;

    public OrderObj() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public OrderObj(String orderID, String[] list, int userID, String collect, String endOfOrder)
    {
        this.orderID = orderID;
        this.listOfBooks = list;
        this.userID = userID;
        this.collect = collect;
        this.endOfOrder = endOfOrder;
        this.complete = false;
    }

    public String getOrderID() {
        return orderID;
    }
    public String[] getListOfBooks() {
        return listOfBooks;
    }
    public int getUserID() {
        return userID;
    }
    public String getCollect() {
        return collect;
    }
    public String getEndOfOrder() {
        return endOfOrder;
    }

    public boolean getComplete() {
        return complete;
    }

    public void setComplete(boolean com) {
        this.complete = com;
    }
}
