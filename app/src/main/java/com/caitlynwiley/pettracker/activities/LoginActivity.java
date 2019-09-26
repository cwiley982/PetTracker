package com.caitlynwiley.pettracker.activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.caitlynwiley.pettracker.FirebaseApi;
import com.caitlynwiley.pettracker.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;

import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

    /* Indicates the email is invalid. */
    private static final int FIRAuthErrorCodeInvalidEmail = 17008;
    /* Indicates the user attempted sign in with a wrong password. */
    private static final int FIRAuthErrorCodeWrongPassword = 17009;
    /* Indicates the user account was not found.  */
    private static final int FIRAuthErrorCodeUserNotFound = 17011;

    private static final int RC_SIGN_IN = 1;
    private static final int RC_GOOGLE_SIGN_IN = 2;
    private static final String TAG = "LoginActivity";
    private int signInAttempt = 0;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    private Button mEmailSignInButton;
    private TextView mCreateAccountButton;
    private Button mGoogleSignInButton;
    private Button mFacebookSignInButton;
    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.caitlynwiley.pettracker",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        */

        // set the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, null));
        setSupportActionBar(toolbar);

        // add menu icon to action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(firebaseAuth -> {
            mAuth = firebaseAuth;
            mUser = firebaseAuth.getCurrentUser();
            updateUI("", "");
        });

        mUser = mAuth.getCurrentUser();

        mEmailField = findViewById(R.id.email_field);
        mPasswordField = findViewById(R.id.password_field);

        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);

        mCreateAccountButton = findViewById(R.id.create_account_button);
        mCreateAccountButton.setOnClickListener(this);

        mGoogleSignInButton = findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setOnClickListener(this);

        mFacebookSignInButton = findViewById(R.id.facebook_login_button);
        mFacebookSignInButton.setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Facebook Login button
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("Success", "Login");
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        mFacebookSignInButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            mUser = mAuth.getCurrentUser();
            updateUI("", "");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        switch (requestCode) {
            case RC_GOOGLE_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("LoginActivity", "Google sign in failed", e);
                    // ...
                }
                break;
            case RC_SIGN_IN:

        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        signInAttempt++;
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("LoginActivity", "signInWithCredential:success");
                        mUser = mAuth.getCurrentUser();
                        updateUI("", "");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("LoginActivity", "signInWithCredential:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        mUser = null;
                        updateUI("", ((FirebaseAuthException)task.getException()).getErrorCode());
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        signInAttempt++;
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        mUser = mAuth.getCurrentUser();
                        updateUI("", "");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        mUser = null;
                        updateUI(task.getException().getMessage(), "");
                    }
                });
    }

    private void updateUI(String errorMsg, String error) {
        if (mUser == null) {
            if (signInAttempt == 0) return;
            String msg;
            switch (error) {
                case "":
                    msg = "Error signing in. Error message: " + errorMsg;
                    break;
                case "ERROR_INVALID_EMAIL":
                    msg = "Invalid email address. Please try again.";
                    break;
                case "ERROR_WRONG_PASSWORD":
                    msg = "Incorrect password. Please try again.";
                    break;
                case "ERROR_USER_NOT_FOUND":
                    msg = "No user found with that email address.";
                    break;
                default:
                    msg = error;
                    break;
            }
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(FirebaseApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        FirebaseApi api = retrofit.create(FirebaseApi.class);
        api.getNumPets(mUser.getUid()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().equals(0)) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("logged_in", true).apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("creating_pet", true).apply();
                    startActivity(new Intent(LoginActivity.this, AddPetActivity.class));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void emailSignIn() {
        // validate form

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        signInAttempt++;

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // show error message "Error: Email and password fields must not be empty
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        mUser = task.getResult().getUser();
                        updateUI("", "");
                    } else {
                        updateUI("", ((FirebaseAuthException)task.getException()).getErrorCode());
                        //FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        //Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signUp() {
        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    private void facebookSignIn() {
        LoginManager.getInstance()
                .logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "user_friends"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email_sign_in_button:
                emailSignIn();
                break;
            case R.id.create_account_button:
                signUp();
                break;
            case R.id.google_sign_in_button:
                googleSignIn();
                break;
            case R.id.facebook_login_button:
                facebookSignIn();
                break;
        }
    }
}
