package com.caitlynwiley.pettracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mPasswordRepeated;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    private ArrayList<Account> accounts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        accounts = new ArrayList<>();
        mUsername = findViewById(R.id.username_field);
        mPassword = findViewById(R.id.password_field_one);
        mPasswordRepeated = findViewById(R.id.password_field_two);
        Button createBtn = findViewById(R.id.create_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean found = false;
                String username = mUsername.getText().toString();
                if (accounts != null) {
                    // search through usernames to see if that one is available
                    for (Account a : accounts) {
                        if (a.getUsername().equals(username)) {
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) { // username is available
                    // check passwords
                    if (!mPassword.getText().toString().equals(mPasswordRepeated.getText().toString())) {
                        return;
                    }

                    // create account
                    Account a = new Account(username, null, mPassword.getText().toString());
                    ref.child("accounts").push().setValue(a);

                    Intent i = new Intent(CreateAccountActivity.this, AddPetActivity.class);
                    startActivity(i);
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
