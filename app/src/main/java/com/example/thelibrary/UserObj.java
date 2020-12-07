package com.example.thelibrary;

public class UserObj
{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phone;
   // private String gender;
    private int isAdmin;

    public UserObj() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserObj( String firstName,String lastName,String email,String password,String address, String phone, int isAdmin)
    {
        this.firstName=firstName;
        this.lastName=lastName;
        this.email = email;
        this.password=password;
        this.address = address;
        this.phone=phone;
    //    this.gender = gender;
        this.isAdmin = isAdmin;
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

//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}