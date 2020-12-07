package com.example.thelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameEditText,lastNameEditText, addressEditText, phoneEditText, emailEditText,passwordEditText;
    private Button register;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private UserObj user;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameEditText=(EditText)findViewById(R.id.firstName);
        lastNameEditText=(EditText)findViewById(R.id.lastName);
        addressEditText=(EditText)findViewById(R.id.address);
        phoneEditText=(EditText)findViewById(R.id.phone);
        emailEditText=(EditText)findViewById(R.id.emailReg);
        passwordEditText=(EditText)findViewById(R.id.passReg);
        register = (Button)findViewById(R.id.reg);


        database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference("users");
        mAuth=FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String fName=firstNameEditText.getText().toString().trim();
                String lName=lastNameEditText.getText().toString().trim();
                String address=addressEditText.getText().toString().trim();
                String phone=phoneEditText.getText().toString().trim();
                String email=emailEditText.getText().toString().trim();
                String password=passwordEditText.getText().toString().trim();
                if (fName.isEmpty()) {
                    firstNameEditText.setError("First Name is required");
                }
                if (lName.isEmpty()) {
                    lastNameEditText.setError("Last Name is required");
                }
                if (address.isEmpty()) {
                    addressEditText.setError("Address is required");
                }
                if (phone == null||!Patterns.PHONE.matcher(phone).matches()) {
                    Toast.makeText(getApplicationContext(),"Invalid phone",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password))
                {
                    Toast.makeText(getApplicationContext(),"Enter email or password",Toast.LENGTH_LONG).show();
                    return;
                }
                if (email == null||!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_LONG).show();
                    return;
                }
                if(password == null || password.trim().length() <= 5)
                {
                    Toast.makeText(getApplicationContext(),"Password length must be at least 6",Toast.LENGTH_LONG).show();
                    return;
                }
                    user =new UserObj(fName,lName,email,password,address,phone,0);
                    registerUser(email,password);

            }
        });

    }//onCreate

    private void registerUser(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("RegisterActivity", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else
                        {
                            // If registration is failed, display a message to the user.
                            Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void updateUI(FirebaseUser currentUser)
    {
        String keyId=mDatabase.push().getKey();
        mDatabase.child(keyId).setValue(user);
        Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }



}