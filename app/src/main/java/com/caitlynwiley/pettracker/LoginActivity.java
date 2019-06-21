package com.caitlynwiley.pettracker;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private static final int RC_SIGN_IN = 1;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();

    private Button mEmailSignInButton;
    private Button mCreateAccountButton;
    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mEmailField = findViewById(R.id.email_field);
        mPasswordField = findViewById(R.id.password_field);

        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);

        mCreateAccountButton = findViewById(R.id.create_account_button);
        mCreateAccountButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private void updateUI(FirebaseUser user, String errorMsg) {
        if (user == null) {
            Toast.makeText(getApplicationContext(), "Error signing in. Error message: " + errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }
        // Start main activity
        Intent startMain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(startMain);
    }

    private void signIn() {
        // validate form

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUp() {
        // validate

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        Task<AuthResult> t = mAuth.createUserWithEmailAndPassword(email, password);
        t.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onAuthSuccess(task.getResult().getUser());
                } else {
                    updateUI(null, task.getException().getMessage());
                }
            }
        });
    }

    private void onAuthSuccess(FirebaseUser u) {
        Account user = new Account(u.getUid(), u.getEmail());
        ref.child("users").child(u.getUid()).setValue(user);

        updateUI(u, "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email_sign_in_button:
                signIn();
                break;
            case R.id.create_account_button:
                signUp();
                break;
        }
    }
}