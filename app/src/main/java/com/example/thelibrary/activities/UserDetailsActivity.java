package com.example.thelibrary.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBUser;
import com.example.thelibrary.fireBase.model.dataObj.UserObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    FireBaseDBUser fu = new FireBaseDBUser();
    private TextView fname, lname, address, phone, email, sub;
    private Button update, favorites;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userRef = fu.getUserFromDB(user.getUid());
    UserObj userObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        fname = (TextView) findViewById(R.id.detFName);
        lname = (TextView) findViewById(R.id.detLName);
        address = (TextView) findViewById(R.id.detAdd);
        phone = (TextView) findViewById(R.id.detPhone);
        email = (TextView) findViewById(R.id.detEmail);
        sub = (TextView) findViewById(R.id.detSub);
        update = (Button) findViewById(R.id.updateDetails);
        update.setOnClickListener(this);
        favorites = (Button) findViewById(R.id.myFavorits);
        favorites.setOnClickListener(this);

    }

    protected void onResume() {
        super.onResume();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userObj = dataSnapshot.getValue(UserObj.class);
                fname.setText(dataSnapshot.child("firstName").getValue(String.class));
                lname.setText(dataSnapshot.child("lastName").getValue(String.class));
                address.setText(dataSnapshot.child("address").getValue(String.class));
                phone.setText(dataSnapshot.child("phone").getValue(String.class));
                email.setText(dataSnapshot.child("email").getValue(String.class));
                sub.setText(dataSnapshot.child("subscription").getValue(String.class));
//                    fname.append(userObj.getFirstName());
//                    lname.append(userObj.getLastName());
//                    address.append(userObj.getAddress());
//                    phone.append(userObj.getPhone());
//                    email.append(userObj.getEmail());
//                    sub.append(userObj.getSubscription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void onClick(View v) {
        if (v == update) {
            createUpdateDialog();
        }
        if (v == favorites) {
            Intent favIntent = new Intent(UserDetailsActivity.this, FavBooksActivity.class);
            startActivity(favIntent);
        }
    }

    public void createUpdateDialog() {
        final Dialog d = new Dialog(this);
        EditText nameEdit, lnameEdit, addressEdit, phoneEdit, oldPassEdit, newPassEdit;
        Button updateBtn, cancelBtn;
        d.setContentView(R.layout.edit_profile_dialog);
        d.setTitle("עדכון פרטים:");
        d.setCancelable(true);

        nameEdit = d.findViewById(R.id.editName);
        lnameEdit = d.findViewById(R.id.editLName);
        addressEdit = d.findViewById(R.id.editAdd);
        phoneEdit = d.findViewById(R.id.editPhone);
        oldPassEdit = d.findViewById(R.id.editPassOld);
        newPassEdit = d.findViewById(R.id.editPassNew);
        updateBtn = d.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newFName = nameEdit.getText().toString().trim();
                String newLName = lnameEdit.getText().toString().trim();
                String newAddress = addressEdit.getText().toString().trim();
                String newPhone = phoneEdit.getText().toString().trim();
                String oldPass = oldPassEdit.getText().toString().trim();
                String newPass = newPassEdit.getText().toString().trim();

                if (oldPass.isEmpty() && !newPass.isEmpty()) {
                    oldPassEdit.setError("הכנס סיסמה נוכחית");
                }
                if (!newPhone.isEmpty() && (!Patterns.PHONE.matcher(newPhone).matches() || newPhone.length() < 9 || newPhone.length() > 13)) {
                    Toast.makeText(getApplicationContext(), "מספר טלפון לא חוקי", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!oldPass.isEmpty() && !newPass.isEmpty() && !oldPass.trim().equals(userObj.getPassword())) {
                    oldPassEdit.setError("הסיסמה לא תואמת לסיסמה הנוכחית");
                    return;
                }
                if (!oldPass.isEmpty() && !newPass.isEmpty() && newPass.trim().length() <= 5) {
                    Toast.makeText(getApplicationContext(), "הסיסמה חייבת להכיל לפחות 6 תוים", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!newFName.isEmpty()) {
                    userRef.child("firstName").setValue(newFName);
                    fname.setText(newFName);
                }
                if (!newLName.isEmpty()) {
                    userRef.child("lastName").setValue(newLName);
                    lname.setText(newLName);
                }
                if (!newAddress.isEmpty()) {
                    userRef.child("address").setValue(newAddress);
                    address.setText(newAddress);
                }
                if (!newPhone.isEmpty()) {
                    userRef.child("phone").setValue(newPhone);
                    phone.setText(newPhone);
                }
                if (!newPass.isEmpty()) {
                    userRef.child("password").setValue(newPass);
                    user.updatePassword(newPass);
                }
                d.dismiss();

            }
        });
        cancelBtn = (Button) d.findViewById(R.id.cancelUpdateBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
            }

        });
        d.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuBack) {
            finish();
        }

        if (id == R.id.menuCart) {
            Intent order = new Intent(UserDetailsActivity.this, MyOrderActivity.class);
            startActivity(order);
        }
        return super.onOptionsItemSelected(item);
    }
}