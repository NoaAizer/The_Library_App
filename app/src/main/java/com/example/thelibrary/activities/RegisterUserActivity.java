package com.example.thelibrary.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.FireBaseDBUser;
import com.example.thelibrary.fireBase.model.mAuthUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText tzEditText, firstNameEditText, lastNameEditText, addressEditText, phoneEditText, emailEditText, passwordEditText;
    private Button register;
    private Spinner subscriptionSpinner;
    ArrayList<String> tzUsers = new ArrayList<>();
    mAuthUser auth = new mAuthUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logolab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tzEditText = (EditText) findViewById(R.id.tz);
        firstNameEditText = (EditText) findViewById(R.id.firstName);
        lastNameEditText = (EditText) findViewById(R.id.lastName);
        addressEditText = (EditText) findViewById(R.id.address);
        phoneEditText = (EditText) findViewById(R.id.phone);
        emailEditText = (EditText) findViewById(R.id.emailReg);
        passwordEditText = (EditText) findViewById(R.id.passReg);
        register = (Button) findViewById(R.id.reg);
        subscriptionSpinner = findViewById(R.id.spinner_userSub);
        ArrayAdapter<String> adapterSub = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]
                {"בסיסי: 2 ספרים- 20 שח", "מורחב: 5 ספרים- 45 שח", "משפחתי: 10 ספרים- 80 שח"});
        subscriptionSpinner.setAdapter(adapterSub);

        getTZlist();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tz = tzEditText.getText().toString().trim();
                String fName = firstNameEditText.getText().toString().trim();
                String lName = lastNameEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String subscription = subscriptionSpinner.getSelectedItem().toString().trim().split(":")[0];
                Double amountToPay = Double.parseDouble(subscriptionSpinner.getSelectedItem().toString().trim().split(" ")[3]);
                String subType= subscriptionSpinner.getSelectedItem().toString().trim().split("-")[0];
                if (tz.isEmpty()) {
                    tzEditText.setError("שדה חובה");
                }
                if (fName.isEmpty()) {
                    firstNameEditText.setError("שדה חובה");
                }
                if (lName.isEmpty()) {
                    lastNameEditText.setError("שדה חובה");
                }
                if (address.isEmpty()) {
                    addressEditText.setError("שדה חובה");
                }
                if (email.isEmpty()) {
                    emailEditText.setError("שדה חובה");
                }
                if (password.isEmpty()) {
                    passwordEditText.setError("שדה חובה");
                }
                if (phone.isEmpty()) {
                    phoneEditText.setError("שדה חובה");
                }
                if (subscription.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "חובה לבחור סוג מנוי", Toast.LENGTH_LONG).show();
                    return;
                }
                if (phone == null || !Patterns.PHONE.matcher(phone).matches() || phone.length() < 9 || phone.length() > 13) {
                    phoneEditText.setError("מספר נייד לא חוקי");
                    return;
                }
                if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("כתובת מייל לא חוקית");
                    return;
                }
                if (password == null || password.trim().length() <= 5) {
                    passwordEditText.setError("סיסמה חייבת להכיל לפחוץ 6 תווים");
                }
                if (tz == null || tz.trim().length() != 9) {
                    tzEditText.setError("תז לא חוקית");
                    return;
                }
                if (tzUsers.contains(tz)) {
                    tzEditText.setError("קיים מנוי עבור תעודת זהות זו");
                    return;
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterUserActivity.this);
                alertDialog.setTitle("השלמת הרשמה");
                alertDialog.setMessage("הנך מועבר לתשלום בסך "+amountToPay+" ₪"+"\n"
                +"עבור מנוי "+subType+"\n"
                +"לצורך השלמת ההרשמה");
                alertDialog.setPositiveButton("המשך",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegisterUserActivity.this, CreditCardActivity.class);
                                intent.putExtra("amount",amountToPay);
                                intent.putExtra("type","register");
                                auth.activity= RegisterUserActivity.this;
                                intent.putExtra("tz",tz);
                                intent.putExtra("fName",fName);
                                intent.putExtra("lName",lName);
                                intent.putExtra("phone",phone);
                                intent.putExtra("address",address);
                                intent.putExtra("email",email);
                                intent.putExtra("password",password);
                                intent.putExtra("sub",subscription);
                                startActivity(intent);
                                }

                            });
                alertDialog.setNegativeButton("ביטול",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });

    }//onCreate
 private void getTZlist(){
     new FireBaseDBUser().getUsersListRef().addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
             for (DataSnapshot data : dataSnapshot.getChildren()) {
                   tzUsers.add(data.child("tz").getValue(String.class));
             }
         }
         @Override
         public void onCancelled(DatabaseError databaseError) {
         }
     });
 }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
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
        return super.onOptionsItemSelected(item);
    }
}