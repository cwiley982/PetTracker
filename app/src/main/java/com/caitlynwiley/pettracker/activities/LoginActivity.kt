package com.caitlynwiley.pettracker.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.caitlynwiley.pettracker.FirebaseApi
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.activities.LoginActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(), View.OnClickListener {
    private var signInAttempt = 0
    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.reference
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mCallbackManager: CallbackManager? = null
    private var mEmailSignInButton: Button? = null
    private var mCreateAccountButton: TextView? = null
    private var mGoogleSignInButton: Button? = null
    private var mFacebookSignInButton: Button? = null
    private var mEmailField: EditText? = null
    private var mPasswordField: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white, null))
        setSupportActionBar(toolbar)

        // add menu icon to action bar
        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        mAuth!!.addAuthStateListener { firebaseAuth: FirebaseAuth ->
            mAuth = firebaseAuth
            mUser = firebaseAuth.currentUser
            updateUI("", "")
        }
        mUser = mAuth!!.currentUser
        mEmailField = findViewById(R.id.email_field)
        mPasswordField = findViewById(R.id.password_field)
        mEmailSignInButton = findViewById(R.id.email_sign_in_button)
        mEmailSignInButton?.setOnClickListener(this)
        mCreateAccountButton = findViewById(R.id.create_account_button)
        mCreateAccountButton?.setOnClickListener(this)
        mGoogleSignInButton = findViewById(R.id.google_sign_in_button)
        mGoogleSignInButton?.setOnClickListener(this)
        mFacebookSignInButton = findViewById(R.id.facebook_login_button)
        mFacebookSignInButton?.setOnClickListener(this)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Facebook Login button
        FacebookSdk.setApplicationId(resources.getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(this.applicationContext)
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        Log.d("Success", "Login")
                        handleFacebookAccessToken(loginResult.accessToken)
                    }

                    override fun onCancel() {
                        Toast.makeText(this@LoginActivity, "Login Cancel", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(exception: FacebookException) {
                        Toast.makeText(this@LoginActivity, exception.message, Toast.LENGTH_LONG).show()
                    }
                })
        mFacebookSignInButton?.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        if (mAuth!!.currentUser != null) {
            mUser = mAuth!!.currentUser
            updateUI("", "")
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_GOOGLE_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("LoginActivity", "Google sign in failed", e)
                    // ...
                }
            }
            RC_SIGN_IN -> {
            }
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct!!.idToken, null)
        signInAttempt++
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("LoginActivity", "signInWithCredential:success")
                        mUser = mAuth!!.currentUser
                        updateUI("", "")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("LoginActivity", "signInWithCredential:failure", task.exception)
                        Toast.makeText(applicationContext, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                        mUser = null
                        updateUI("", (task.exception as FirebaseAuthException?)!!.errorCode)
                    }
                }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        signInAttempt++
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        mUser = mAuth!!.currentUser
                        updateUI("", "")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        mUser = null
                        updateUI(task.exception!!.message, "")
                    }
                }
    }

    private fun updateUI(errorMsg: String?, error: String) {
        if (mUser == null) {
            if (signInAttempt == 0) return
            val msg: String = when (error) {
                "" -> "Error signing in. Error message: $errorMsg"
                "ERROR_INVALID_EMAIL" -> "Invalid email address. Please try again."
                "ERROR_WRONG_PASSWORD" -> "Incorrect password. Please try again."
                "ERROR_USER_NOT_FOUND" -> "No user found with that email address."
                else -> error
            }
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
            return
        }
        val gson = GsonBuilder()
                .setLenient()
                .create()
        val retrofit = Retrofit.Builder().baseUrl(FirebaseApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val api = retrofit.create(FirebaseApi::class.java)
        api.getNumPets(mUser!!.uid).enqueue(object : Callback<Int?> {
            override fun onResponse(call: Call<Int?>, response: Response<Int?>) {
                if (response.isSuccessful && response.body() != null && response.body() != 0) {
                    PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putBoolean("logged_in", true).apply()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putBoolean("creating_pet", true).apply()
                    startActivity(Intent(this@LoginActivity, AddPetActivity::class.java))
                }
            }

            override fun onFailure(call: Call<Int?>, t: Throwable) {}
        })
    }

    private fun emailSignIn() {
        // validate form
        val email = mEmailField!!.text.toString()
        val password = mPasswordField!!.text.toString()
        signInAttempt++
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // show error message "Error: Email and password fields must not be empty
            return
        }
        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        mUser = task.result!!.user
                        updateUI("", "")
                    } else {
                        updateUI("", (task.exception as FirebaseAuthException?)!!.errorCode)
                        //FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        //Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
    }

    private fun signUp() {
        startActivity(Intent(this@LoginActivity, CreateAccountActivity::class.java))
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    private fun facebookSignIn() {
        LoginManager.getInstance()
                .logInWithReadPermissions(this@LoginActivity,
                        listOf("public_profile", "user_friends"))
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.email_sign_in_button -> emailSignIn()
            R.id.create_account_button -> signUp()
            R.id.google_sign_in_button -> googleSignIn()
            R.id.facebook_login_button -> facebookSignIn()
        }
    }

    companion object {
        /* Indicates the email is invalid. */
        private const val FIRAuthErrorCodeInvalidEmail = 17008

        /* Indicates the user attempted sign in with a wrong password. */
        private const val FIRAuthErrorCodeWrongPassword = 17009

        /* Indicates the user account was not found.  */
        private const val FIRAuthErrorCodeUserNotFound = 17011
        private const val RC_SIGN_IN = 1
        private const val RC_GOOGLE_SIGN_IN = 2
        private const val TAG = "LoginActivity"
    }
}