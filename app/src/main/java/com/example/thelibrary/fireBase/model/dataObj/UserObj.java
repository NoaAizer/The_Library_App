package com.example.thelibrary.fireBase.model.dataObj;

import java.io.Serializable;

public class UserObj implements Serializable
{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String subscription;
    private String shoppingListId;
    private int amountOfBooksRemains;



    public UserObj() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserObj( String firstName,String lastName,String email,String password,String address, String phone, String sub)
    {
        this.firstName=firstName;
        this.lastName=lastName;
        this.email = email;
        this.password=password;
        this.address = address;
        this.phone=phone;
        this.subscription = sub;
        this.shoppingListId="";
        if(sub.equals("בסיסי")) this.amountOfBooksRemains =2;
        else if (sub.equals("מורחב")) this.amountOfBooksRemains =5;
        else if (sub.equals("משפחתי")) this.amountOfBooksRemains =10;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String sub) { this.subscription=sub;}

    public String getShoppingID(){return shoppingListId;}

    public void setShoppingListId(String shoppingListId) {this.shoppingListId = shoppingListId;}

    public int getAmountOfBooksRemains() { return amountOfBooksRemains; }

    public void setAmountOfBooksRemains(int amountOfBooksRemains) { this.amountOfBooksRemains = amountOfBooksRemains; }
}