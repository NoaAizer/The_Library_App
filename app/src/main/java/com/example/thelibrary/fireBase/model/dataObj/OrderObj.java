package com.example.thelibrary.fireBase.model.dataObj;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderObj implements Serializable {

    private ArrayList<String> listOfBooks;
    private String userID, userTZ;
    private String collect; // TA or deliver
    private String endOfOrder;
    private boolean complete;
    private boolean arrivedToUser;
    private String statusDeliver;
//    enum statusDeliver {TREAT, COMPLETE, COLLECT, SHIPPED, ARRIVED}
    // TA- TREAT, COMPLETE- ניתן לאיסוף, COLLECT-נאסף
    // deliver- TREAT, COMPLETE, SHIPPED- יצא מהספריה, ARRIVED

    public OrderObj() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public OrderObj(ArrayList<String> list, String userTZ,String userID, String collect, String endOfOrder)
    {
        this.listOfBooks = list;
        this.userTZ =userTZ;
        this.userID = userID;
        this.collect = collect;
        this.endOfOrder = endOfOrder;
        this.complete = false;
        this.arrivedToUser = false;
        this.statusDeliver = "בטיפול";

    }

    public ArrayList<String> getListOfBooks()
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

    public String getUserTZ() {
        return userTZ;
    }

    public void setUserTZ(String userTZ) {
        this.userTZ = userTZ;
    }
}
