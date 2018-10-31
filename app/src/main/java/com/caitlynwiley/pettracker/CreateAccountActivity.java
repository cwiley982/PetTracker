package com.caitlynwiley.pettracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mPasswordRepeated;
    private Button mCreateBtn;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("pettracker");
    private String[] userIds;

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
                for (String id : userIds) {
                    if (id.equalsIgnoreCase(newId)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // check passwords
                    if (!mPassword.getText().toString().equals(mPasswordRepeated.getText().toString())) {
                        return;
                    }
                    // create account
                    ref.child("accounts/" + newId).setValue(new Account(newId, null, mPassword.getText().toString()));
                    Intent i = new Intent(CreateAccountActivity.this, MainActivity.class);
                    i.putExtra("USERNAME", newId);
                    startActivity(i);
                }
            }
        });

        // Attach a listener to read the data at our posts reference
        ref.child("userIds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userIds = dataSnapshot.getValue(String[].class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

}
