package com.example.thelibrary;

public class BookObj
{
    public String name;
    public String author;
    public String brief;
    public String genre;
    public String language;
    public String publishing_year;




    public BookObj() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public BookObj(String name, String author, String brief,String genre,String language,String publishing_year)
    {
        this.name = name;
        this.author = author;
        this.brief=brief;
        this.genre=genre;
        this.language=language;
        this.publishing_year=publishing_year;


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
    public String getgenre() {
        return genre;
    }
    public String getlanguage() {
        return language;
    }
    public String getpublishing_year() {
        return publishing_year;
    }



}