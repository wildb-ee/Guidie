package com.fayaz.guidie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fayaz.guidie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PersonalDetailsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private Toolbar toolbar;
    private EditText fullNameText;
    private EditText phoneText;
    private TextView emailText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_details);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.settings_toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        fullNameText = findViewById(R.id.edit_full_name);
        phoneText = findViewById(R.id.edit_phone_number);
        emailText = findViewById(R.id.text_email);
        saveButton = findViewById(R.id.button_save_new_profile);

        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        saveButton.setOnClickListener(v->{
            Map<String, Object> docData = new HashMap<>();
            docData.put("fullName", fullNameText.getText().toString());
            docData.put("contact", phoneText.getText().toString());
            db.collection("users").document(user.getUid()).set(docData).addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("LOG", "DocumentSnapshot successfully written!");
                            showUserUpdatedDialog();
                        }
                    }
            );
        });
        db.collection("users").document(user.getUid()).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document  = task.getResult();
                        String fullName = document.getString("fullName");
                        String phone = document.getString("contact");
                        updateUi(fullName, phone);
                    }
                }
        );

    }
    private void showUserUpdatedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_user_updated, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button okButton = dialogView.findViewById(R.id.dialog_button_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(PersonalDetailsActivity.this, NavActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
    }
    private void updateUi(String fullName, String phone){
        emailText.setText(mAuth.getCurrentUser().getEmail());
        fullNameText.setText(fullName);
        phoneText.setText(phone);
    }
}