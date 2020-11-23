package com.example.thelibrary;

public class UserObj
{
    public String email;
    public String password;

    public UserObj() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserObj( String email,String password)
    {
        this.email = email;
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}