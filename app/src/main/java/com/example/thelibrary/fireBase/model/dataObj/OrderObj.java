package com.example.thelibrary.fireBase.model.dataObj;

public class OrderObj {

    private String[] listOfBooks;
    private String userID;
    private String collect; // TA or deliver
    private String endOfOrder;
    private boolean complete;
    private String shoopingId;
    private boolean arrivedToUser;

    public OrderObj() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public OrderObj(String[] list, String userID, String collect, String endOfOrder)
    {
        this.listOfBooks = list;
        this.userID = userID;
        this.collect = collect;
        this.endOfOrder = endOfOrder;
        this.complete = false;
        this.arrivedToUser = false;
    }

    public String[] getListOfBooks()
    {
        return listOfBooks;
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

    public boolean getarrivedToUser()
    {
        return arrivedToUser;
    }
    public void setarrivedToUser(boolean arrived)
    {
        this.arrivedToUser = arrived;
    }
}
