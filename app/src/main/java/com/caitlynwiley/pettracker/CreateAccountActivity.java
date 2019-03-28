package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordRepeated;
    private Button mCreateBtn;
    private String TAG = CreateAccountActivity.class.getSimpleName();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    private FirebaseAuth mAuth;
    private ArrayList<Account> accounts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        accounts = new ArrayList<>();
        mEmail = findViewById(R.id.email_field);
        mPassword = findViewById(R.id.password_field_one);
        mPasswordRepeated = findViewById(R.id.password_field_two);
        mCreateBtn = findViewById(R.id.create_btn);
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email and password
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if (password.equals(mPasswordRepeated.getText().toString())) {
                    mAuth.createUserWithEmailAndPassword(email, password);/*
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        // log in and go to main activity
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        // go back to login activity
                                    }
                                }
                            });*/
                } else {
                    //passwords don't match, show toast
                    Toast.makeText(CreateAccountActivity.this, "Passwords don't match. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Attach a listener to read the data
        ref.child("accounts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for (DataSnapshot d : data) {
                    accounts.add(d.getValue(Account.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
