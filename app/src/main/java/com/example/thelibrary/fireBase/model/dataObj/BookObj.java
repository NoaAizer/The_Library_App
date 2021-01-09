package com.example.thelibrary.fireBase.model.dataObj;

import org.threeten.bp.LocalDate;

import java.io.Serializable;

public class BookObj implements Serializable
{
    public enum Genre {
        HINT("ז'אנר"),
        ENUM1("מתח"),
        ENUM2("דרמה"),
        ENUM3("נוער"),
        ENUM4("קומדיה"),
        ENUM5("ילדים"),
        ENUM6("רומן"),
        ENUM7("ביוגרפיה"),
        ENUM8("עיון"),
        ENUM9("מדע בדיוני");
        private String friendlyName;

        private Genre(String friendlyName){
            this.friendlyName = friendlyName;
        }

        @Override public String toString(){
            return friendlyName;
        }
    }
    public enum Lang {
        HINT("שפה"),
        ENUM1("עברית"),
        ENUM2("אנגלית"),
        ENUM3("צרפתית"),
        ENUM4("רוסית");
        private String friendlyName;

        private Lang(String friendlyName){
            this.friendlyName = friendlyName;
        }

        @Override public String toString(){
            return friendlyName;
        }
    }
    public String name;
    public String author;
    public String brief;
    public String genre;
    public String language;
    public String publishing_year;
    public int amount;
    public String id;
    public String imageURL;
    public String added_date;


    public BookObj() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public BookObj(String name, String author, String brief,String genre,String language,String publishing_year, int amount, String imageURL)
    {
        this.name = name;
        this.author = author;
        this.brief=brief;
        this.genre=genre;
        this.language=language;
        this.publishing_year=publishing_year;
        this.amount=amount;
        this.imageURL=imageURL;
        this.added_date = LocalDate.now().toString();
    }
    public String getName() {
        return name;
    }
    public String getauthor() {
        return author;
    }
    public String getbrief() {
        return brief;
    }
    public String getgenre() { return genre; }
    public String getlanguage() {
        return language;
    }
    public String getpublishing_year() {
        return publishing_year;
    }


    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount=amount;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public LocalDate getAdded_date() {
        return LocalDate.parse(added_date);
    }
}