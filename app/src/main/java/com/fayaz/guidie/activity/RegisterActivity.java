package com.fayaz.guidie.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fayaz.guidie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG ="LoginActvity";

    private EditText fullNameInput;

    private EditText passwordInput;
    private EditText emailInput;
    private EditText contactPhoneInput;

    private Button registerBtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        contactPhoneInput = findViewById(R.id.register_phone);
        registerBtn = findViewById(R.id.buttonRegister);
        fullNameInput = findViewById(R.id.register_fullName);

        registerBtn.setOnClickListener(v -> {
            String mail = String.valueOf(emailInput.getText());
            String pswrd = String.valueOf(passwordInput.getText());
            String fullName = String.valueOf(fullNameInput.getText());
            String contact = String.valueOf(contactPhoneInput.getText());

            if(TextUtils.isEmpty(mail)){
                Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(pswrd)){
                Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(fullName)){
                Toast.makeText(RegisterActivity.this, "Enter Full Name", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(contact)){
                Toast.makeText(RegisterActivity.this, "Enter Contact", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(mail, pswrd)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> docData = new HashMap<>();
                                docData.put("fullName", fullName);
                                docData.put("contact", contact);

                                db.collection("users").document(user.getUid())
                                        .set(docData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(LOG, "DocumentSnapshot successfully written!");
                                                Intent intent = new Intent(RegisterActivity.this, NavActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(LOG, "Error writing document", e);
                                            }
                                        });

                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        });










}}