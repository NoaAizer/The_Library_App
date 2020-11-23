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

public class RegisterActivity extends AppCompatActivity
{
    private EditText emailEditText,passwordEditText,password2EditText;
    private Button register_now_btn;
  //  private FirebaseDatabase database;
   // private  DatabaseReference mDatebase;
    private FirebaseAuth mAuth;
    private UserObj user;
    private static final String TAG="RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText=(EditText)findViewById(R.id.username);
        passwordEditText=(EditText)findViewById(R.id.password);
        register_now_btn = (Button)findViewById(R.id.login);

        mAuth=FirebaseAuth.getInstance();


        register_now_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(view == register_now_btn)
                {
                    String email=emailEditText.getText().toString().trim();
                    String password=passwordEditText.getText().toString().trim();
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
                    user =new UserObj(email,password);
                    registerUser(email,password);
                }
            }
        });

    }//onCreate

    private void registerUser(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult> ()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void updateUI(FirebaseUser currentUser)
    {
       // mDatebase.child(keyId).setValue(user);
        Intent loginIntent=new Intent(this,MainActivity.class);
        startActivity(loginIntent);
    }



}