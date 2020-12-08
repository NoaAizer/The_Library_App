package com.example.thelibrary.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBOrder;
import com.example.thelibrary.fireBase.model.FireBaseDBUser;
import com.example.thelibrary.fireBase.model.dataObj.BookObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;


public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private Button save_order;
    private RadioButton TA, deliver;
    FireBaseDBOrder fbOr = new FireBaseDBOrder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        save_order = findViewById(R.id.finish);
        TA = findViewById(R.id.TA);
        deliver = findViewById(R.id.deliver);

        save_order.setOnClickListener(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice);
        ListView list = (ListView) findViewById(R.id.listBooks);
        for (int i=0; i<len; i++) {
            adapter.add("one");
        }
        list.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {
        if (v == save_order) {
            if(TA.isChecked() || deliver.isChecked()) {
                Toast.makeText(getApplicationContext(), "ההזמנה הושלמה בהצלחה", Toast.LENGTH_LONG).show();
                BookObj[] listOfBooks = ;

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userID = user.getUid();

                String collect;
                if (TA.isChecked()) {
                    collect = "איסוף עצמי";
                }
                else {
                    collect = "משלוח";
                }

                LocalDate today = LocalDate.now();
                String endOfOrder = today.plusDays(30).toString();
                fbOr.addOrderToDB(listOfBooks, userID, collect, endOfOrder,MyOrderActivity.this);
                // מחיקת הספרים
            }
            else
            {
                Toast.makeText(getApplicationContext(), "בחר צורת משלוח", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

}
