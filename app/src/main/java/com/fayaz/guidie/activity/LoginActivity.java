package com.fayaz.guidie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fayaz.guidie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG ="LoginActvity";

    private EditText emailInput;
    private EditText passwordInput;
    private TextView regBtn;

    private Button loginBtn;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            Intent intent = new Intent(LoginActivity.this, NavActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.emailLoginText);
        passwordInput = findViewById(R.id.passwordText);
        loginBtn = findViewById(R.id.loginBtn);
        regBtn = findViewById(R.id.registerAccountBtn);

        loginBtn.setOnClickListener(v -> {
            String mail = String.valueOf(emailInput.getText());
            String pswrd = String.valueOf(passwordInput.getText());

            if(TextUtils.isEmpty(mail)){
                Toast.makeText(LoginActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(pswrd)){
                Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(mail, pswrd)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, NavActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        });

        regBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
        });
    }



}
