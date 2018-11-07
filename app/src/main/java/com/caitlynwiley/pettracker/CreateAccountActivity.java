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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mPasswordRepeated;
    private Button mCreateBtn;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    private Map<String, Account> accounts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        mUsername = findViewById(R.id.username_field);
        mPassword = findViewById(R.id.password_field_one);
        mPasswordRepeated = findViewById(R.id.password_field_two);
        mCreateBtn = findViewById(R.id.create_btn);
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean found = false;
                String newId = mUsername.getText().toString();
                if (accounts == null) {
                    // no accounts to check against, just create one
                    Account a = new Account(newId, null, mPassword.getText().toString());
                    ref.child("accounts").child(newId).setValue(a);

                    found = false;
                } else {
                    // search through usernames to see if that one is available
                    for (String id : accounts.keySet()) {
                        if (id.equals(newId)) {
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
                    Account a = new Account(newId, null, mPassword.getText().toString());
                    ref.child("accounts").child(newId).setValue(a);

                    Intent i = new Intent(CreateAccountActivity.this, MainActivity.class);
                    i.putExtra("USERNAME", newId);
                    startActivity(i);
                }
            }
        });

        // Attach a listener to read the data
        ref.child("accounts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accounts = (Map) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
