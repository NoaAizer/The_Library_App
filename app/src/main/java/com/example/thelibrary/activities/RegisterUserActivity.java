package com.example.thelibrary.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.mAuthUser;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText tzEditText, firstNameEditText, lastNameEditText, addressEditText, phoneEditText, emailEditText, passwordEditText;
    private Button register;
    private Spinner subscriptionSpinner;
    mAuthUser auth = new mAuthUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                if (tz.isEmpty()) {
                    tzEditText.setError("Teudat Zehut is required");
                }
                if (fName.isEmpty()) {
                    firstNameEditText.setError("First Name is required");
                }
                if (lName.isEmpty()) {
                    lastNameEditText.setError("Last Name is required");
                }
                if (address.isEmpty()) {
                    addressEditText.setError("Address is required");
                }
                if (email.isEmpty()) {
                    emailEditText.setError("Email is required");
                }
                if (password.isEmpty()) {
                    passwordEditText.setError("Password is required");
                }
                if (subscription.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Subscription Type is required", Toast.LENGTH_LONG).show();
                    return;
                }
                if (phone == null || !Patterns.PHONE.matcher(phone).matches() || phone.length() < 9 || phone.length() > 13) {
                    Toast.makeText(getApplicationContext(), "Invalid phone", Toast.LENGTH_LONG).show();
                    return;
                }
                if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password == null || password.trim().length() <= 5) {
                    Toast.makeText(getApplicationContext(), "Password length must be at least 6", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tz == null || tz.trim().length() != 9) {
                    Toast.makeText(getApplicationContext(), "Invalid Teudat Zehut", Toast.LENGTH_LONG).show();
                    return;
                }
                auth.registerUserToDB(tz, fName, lName, email, password, address, phone, subscription, RegisterUserActivity.this);
            }
        });

    }//onCreate

}