package com.example.thelibrary.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.activities.adapters.BookUserAdapter;
import com.example.thelibrary.fireBase.model.FireBaseDBBook;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity {
    ArrayList<BookObj> books = new ArrayList<>();
    private TextView searchInfo;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        searchInfo = (TextView) findViewById(R.id.searchInfo);
        Map<String,String> search= new HashMap<>();
        Intent intent = getIntent();

        String lan = intent.getExtras().getString("language");
        if(lan!=null) search.put("language",lan);
        String gen = intent.getExtras().getString("genre");
        if(gen!=null) search.put("genre",gen);
        String auth = intent.getExtras().getString("author");
        if(auth!=null) search.put("author",auth);
        String name =intent.getExtras().getString("bookName");
        if(name!=null) search.put("name",name);

//
//        String [] search = {name, auth, gen, lan};
//        for(int i=0; i<search.length; i++)
//        {
//            if(search[i] != null)
//            {
//                if(first)
//                {
//                    searchInfo.append(search[i]);
//                    first=false;
//                }
//                else searchInfo.append(", "+search[i]);
//            }
//        }
        boolean first=true;
        for(String category : search.values()){
            if(first){
                searchInfo.append(category);
                first=false;
            }
            else  searchInfo.append(", "+category);

        }



//        if(name != null && auth == null && gen == null && lan == null)
//            searchInfo.append(name);
//        else if(name == null && auth != null && gen == null && lan == null)
//            searchInfo.append(auth);
//        else if(name == null && auth == null && gen != null && lan == null)
//            searchInfo.append(gen);
//        else if(name == null && auth == null && gen == null && lan != null)
//            searchInfo.append(lan);
//        else if(name != null && auth != null && gen == null && lan == null)
//            searchInfo.append(name + ", " + auth);
//        else if(name != null && auth == null && gen != null && lan == null)
//            searchInfo.append(name + ", " + gen);
//        else if(name != null && auth == null && gen == null && lan != null)
//            searchInfo.append(name + ", " + lan);
//        else if(name == null && auth != null && gen != null && lan == null)
//            searchInfo.append(auth + ", " + gen);
//        else if(name == null && auth != null && gen == null && lan != null)
//            searchInfo.append(auth + ", " + lan);
//        else if(name == null && auth == null && gen != null && lan != null)
//            searchInfo.append(gen + ", " + lan);
//        else if(name != null && auth != null && gen != null && lan == null)
//            searchInfo.append(name + ", " + auth + ", " + gen);
//        else if(name != null && auth != null && gen == null && lan != null)
//            searchInfo.append(name + ", " + auth + ", " + lan);
//        else if(name != null && auth == null && gen != null && lan != null)
//            searchInfo.append(name + ", " + gen +" , " + lan);
//        else if(name == null && auth != null && gen != null && lan != null)
//            searchInfo.append(auth + " , " +gen + ", " + lan);
//        else if(name != null && auth != null && gen != null && lan != null)
//            searchInfo.append(name + " , " + auth + " , " +gen + ", " + lan);


        DatabaseReference booksRef = new FireBaseDBBook().getBookListRef();
        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    int counter=0;
                    for(String category : search.keySet()){
                       if (data.child(category).getValue().toString().contains(search.get(category))){
                                counter++;
                                if (counter == search.size())
                                    books.add(data.getValue(BookObj.class));
                            }
                    }
//                    if(name != null && auth == null && gen == null && lan == null) {
//                        Toast.makeText(getApplicationContext(), data.child("name").getValue().toString(), Toast.LENGTH_LONG).show();
//                        if (data.child("name").getValue().toString().contains(name))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name == null && auth != null && gen == null && lan == null) {
//                        if (data.child("author").getValue().toString().contains(auth))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name == null && auth == null && gen != null && lan == null) {
//                        if (data.child("genre").getValue().equals(gen))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name == null && auth == null && gen == null && lan != null) {
//                        if (data.child("language").getValue().equals(lan))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name != null && auth != null && gen == null && lan == null) {
//                        if (data.child("name").getValue().toString().contains(name)&& data.child("author").getValue().toString().contains(auth))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name != null && auth == null && gen != null && lan == null){
//                        if (data.child("name").getValue().toString().contains(name)&& data.child("genre").getValue().equals(gen))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name != null && auth == null && gen == null && lan != null){
//                        if (data.child("name").getValue().toString().contains(name)&& data.child("language").getValue().equals(lan))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name == null && auth != null && gen != null && lan == null){
//                        if (data.child("author").getValue().toString().contains(auth)&& data.child("genre").getValue().equals(gen))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name == null && auth != null && gen == null && lan != null){
//                        if (data.child("author").getValue().toString().contains(auth)&& data.child("language").getValue().equals(lan))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name == null && auth == null && gen != null && lan != null){
//                        if (data.child("genre").getValue().equals(gen)&& data.child("language").getValue().equals(lan))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name != null && auth != null && gen != null && lan == null){
//                        if (data.child("name").getValue().toString().contains(name)&& data.child("author").getValue().toString().contains(auth)&&
//                                data.child("genre").getValue().equals(gen))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name != null && auth != null && gen == null && lan != null){
//                        if (data.child("name").getValue().toString().contains(name)&& data.child("author").getValue().toString().contains(auth)&&
//                                data.child("language").getValue().equals(lan))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name != null && auth == null && gen != null && lan != null){
//                        if (data.child("name").getValue().toString().contains(name)&& data.child("genre").getValue().equals(gen)&&
//                                data.child("language").getValue().equals(lan))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name == null && auth != null && gen != null && lan != null){
//                        if (data.child("author").getValue().toString().contains(auth)&& data.child("genre").getValue().equals(gen)&&
//                                data.child("language").getValue().equals(lan))
//                            books.add(data.getValue(BookObj.class));
//                    }
//                    else if(name != null && auth != null && gen != null && lan != null){
//                        if (data.child("name").getValue().toString().contains(name)&&data.child("author").getValue().toString().contains(auth)&&
//                                data.child("genre").getValue().equals(gen)&&
//                                data.child("language").getValue().equals(lan))
//                            books.add(data.getValue(BookObj.class));
//                    }


                }
                if (books == null || books.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "אין ספרים להצגה", Toast.LENGTH_LONG).show();
                    return;
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ListView lvBook = findViewById(R.id.resultList);

        // Initialize adapter and set adapter to list view
        BookUserAdapter bookAd = new BookUserAdapter(this, SearchResultActivity.this, books);
        lvBook.setAdapter(bookAd);
    }
}