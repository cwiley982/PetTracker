package com.caitlynwiley.pettracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordRepeated;
    private TextView mErrorText;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    private FirebaseAuth mAuth;
    private ArrayList<Account> accounts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        mAuth = FirebaseAuth.getInstance();
        accounts = new ArrayList<>();
        mEmail = findViewById(R.id.email_field);
        mPassword = findViewById(R.id.password_field_one);
        mPasswordRepeated = findViewById(R.id.password_field_two);
        mErrorText = findViewById(R.id.error_msg);
        Button createBtn = findViewById(R.id.create_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                    // check passwords
                    if (!mPassword.getText().toString().equals(mPasswordRepeated.getText().toString())) {
                        hideKeyboard();
                        showError("Passwords do not match. Please try again.");
                        return;
                    }

                    // create account
                    Account a = new Account(email, mPassword.getText().toString());
                    ref.child("accounts").push().setValue(a);

                    Task<AuthResult> t = mAuth.createUserWithEmailAndPassword(email, mPassword.getText().toString());
                    t.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                ref.child("users").child(mAuth.getCurrentUser().getUid()).child("num_pets").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() == null || dataSnapshot.getValue().equals(0)) {
                                            startActivity(new Intent(CreateAccountActivity.this, AddPetActivity.class));
                                        } else {
                                            startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                showError("Account creation failed with message: " + task.getException().getMessage());
                                hideKeyboard();
                            }
                        }
                    });
                }
        });
    }

    private void showError(String errorMessage) {
        mErrorText.setText(errorMessage);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
