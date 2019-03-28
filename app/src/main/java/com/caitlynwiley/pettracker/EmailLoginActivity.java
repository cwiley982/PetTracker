package com.caitlynwiley.pettracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class EmailLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = EmailLoginActivity.class.getSimpleName();

    EditText mEmailTextEdit;
    EditText mPasswordEditText;
    Button mSignInButton;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        mAuth = FirebaseAuth.getInstance();
        mEmailTextEdit = findViewById(R.id.email_field);
        mPasswordEditText = findViewById(R.id.password_field);
        mSignInButton = findViewById(R.id.email_sign_in_button);
        mSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String email = mEmailTextEdit.getText().toString();
        String password = mPasswordEditText.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(EmailLoginActivity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
