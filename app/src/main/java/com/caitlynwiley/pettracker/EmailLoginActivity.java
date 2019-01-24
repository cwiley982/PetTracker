package com.caitlynwiley.pettracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EmailLoginActivity extends AppCompatActivity {

    EditText mEmailTextEdit;
    EditText mPasswordEditText;
    Button mSignInButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        mEmailTextEdit = findViewById(R.id.email_field);
        mPasswordEditText = findViewById(R.id.password_field);
        mSignInButton = findViewById(R.id.email_sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailLoginActivity.this, MainActivity.class));
            }
        });
    }
}
