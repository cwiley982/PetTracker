package com.caitlynwiley.pettracker.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.preference.PreferenceManager
import com.caitlynwiley.pettracker.PetTrackerTheme
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking
import java.util.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : ComponentActivity() {
    private var signInAttempt = 0
    private lateinit var mAuth: FirebaseAuth
    private var mUser: FirebaseUser? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mCallbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface {
                PetTrackerTheme {
                    Scaffold(
                        topBar = {
                        TopAppBar(title = {
                            Text("Pet Tracker")
                        })
                    }) {
                        LoginActivity()
                    }
                }
            }
        }

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

        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance()
        ref = database.reference

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        mAuth.addAuthStateListener { firebaseAuth: FirebaseAuth ->
            mUser = firebaseAuth.currentUser
            updateUI("", "")
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Configure Facebook Sign In
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
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            mUser = mAuth.currentUser
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
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("LoginActivity", "signInWithCredential:success")
                        mUser = mAuth.currentUser
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
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        mUser = mAuth.currentUser
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

        runBlocking {
            val numPets = PetTrackerRepository().getNumPets(mUser?.uid)
            if (numPets == 0) {
                PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
                    .putBoolean("logged_in", true).apply()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } else {
                PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
                    .putBoolean("creating_pet", true).apply()
                startActivity(Intent(this@LoginActivity, NewPetActivity::class.java))
            }
        }
    }

    private fun emailSignIn(email: String, password: String) {
        // validate form
        signInAttempt++
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // show error message "Error: Email and password fields must not be empty"
            return
        }
        mAuth.signInWithEmailAndPassword(email, password)
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

    @Composable
    fun LoginActivity() {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(modifier = Modifier.fillMaxSize()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(value = email, onValueChange = {email = it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        label = { Text("Email") })

                    TextField(value = password, onValueChange = {password = it},
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        label = { Text("Password") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Button(onClick = {emailSignIn(email, password)}, modifier = Modifier.padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
                        Text("SIGN IN")
                    }

                    Text("Forgot password?", modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colors.onPrimary, fontWeight = FontWeight.Bold)
                }
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            ) {
                // midline?
                Box(modifier = Modifier
                    .height(1.dp)
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(.45f)
                    .padding(start = 8.dp)
                    .background(color = MaterialTheme.colors.onPrimary))

                Text("OR", textAlign = TextAlign.Center, fontSize = 24.sp,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.align(Alignment.Center))

                Box(modifier = Modifier
                    .height(1.dp)
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth(.45f)
                    .padding(end = 8.dp)
                    .background(color = MaterialTheme.colors.onPrimary))
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(10f),
                verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // google sign in btn
                    SignInOption(
                        icon = painterResource(R.drawable.google_logo),
                        btnText = "Sign in with Google",
                        textColor = Color.Black,
                        btnColor = Color.White
                    ) {
                        googleSignIn()
                    }

                    Divider(modifier = Modifier.height(16.dp), color = Color.Transparent)

                    // facebook sign in btn
                    SignInOption(
                        icon = painterResource(R.drawable.com_facebook_button_icon),
                        btnText = "Sign in with Facebook",
                        textColor = Color.White,
                        btnColor = Color.Blue
                    ) {
                        facebookSignIn()
                    }

                    Divider(modifier = Modifier.height(16.dp), color = Color.Transparent)

                    // create account btn
                    SignInOption(
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary),
                        icon = (painterResource(R.drawable.ic_email_black_24dp)),
                        btnText = "Create account",
                        textColor = MaterialTheme.colors.onSecondary,
                        btnColor = MaterialTheme.colors.secondary
                    ) {
                        signUp()
                    }
                }
            }
        }
    }

    @Composable
    fun SignInOption(colorFilter: ColorFilter? = null, icon: Painter, btnText: String,
                     textColor: Color, btnColor: Color, signIn: () -> Unit) {
        Button(onClick = signIn, colors = ButtonDefaults.buttonColors(backgroundColor = btnColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 32.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = icon, contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .width(32.dp)
                        .height(32.dp),
                    colorFilter = colorFilter?.let { colorFilter }
                )
                Text(text = btnText, fontSize = 14.sp, color = textColor,
                    modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    @Preview
    @Composable
    fun PreviewLoginActivity() {
        LoginActivity()
    }
}