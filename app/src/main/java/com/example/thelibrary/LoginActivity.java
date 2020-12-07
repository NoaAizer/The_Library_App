package com.example.thelibrary;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {
    private EditText emailEditText, passwordEditText;
    private Button register_now_btn, login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register_now_btn = findViewById(R.id.registUser);
        login = findViewById(R.id.login);
        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        register_now_btn.setOnClickListener(this);
        login.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();


    }

    public void onClick(View v) {
        if (v == login) {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Password is required");
                return;
            } else {
                validation(email, password);
            }
        }
            if (v == register_now_btn) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

    }

    public void validation(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginActivity", "signInWithEmail:success");
                            //mAuth.getCurrentUser();
                            Intent loginIntent = new Intent(LoginActivity.this, AddBookActivity.class);// ######## need to be changed #########
                            startActivity(loginIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LoginActivity", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }




}


