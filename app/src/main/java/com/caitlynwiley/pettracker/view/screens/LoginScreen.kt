package com.caitlynwiley.pettracker.view.screens

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.theme.PetTrackerTheme
import com.caitlynwiley.pettracker.viewmodel.AppWideViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider

/**
 * A login screen that offers login via email/password.
 */
@Composable
fun LoginActivity() {
    PetTrackerTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text("Pet Tracker")
                })
            }
        ) {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "LoginScreen") {
                composable(route = "LoginScreen") {
                    LoginScreen {
                        navController.navigate("CreateAccount")
                    }
                }
                composable(route = "CreateAccount") {
                    CreateAccountScreen()
                }
            }
        }
    }
}

//    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        // Pass the activity result back to the Facebook SDK
//        mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
//    }

@Composable
fun LoginScreen(createAccount: () -> Unit) {
    val context = LocalContext.current
    val appViewModel = viewModel<AppWideViewModel>()

    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            try {
                Log.d("LoginActivity", "Google sign in success, continue to auth with Firebase")
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                val account = task.getResult(ApiException::class.java)
                // Google Sign In was successful, authenticate with Firebase
                val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
                appViewModel.signInWithCredential(credential = credential)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        } else {
            Log.e("LoginActivity", "Bad activity result, code=" + it.resultCode)
        }
    }

    // Configure Google Sign In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    // Configure Facebook Sign In
    val fbCallbackMgr = CallbackManager.Factory.create()
    LoginManager.getInstance().registerCallback(fbCallbackMgr,
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("LoginActivity", "handleFacebookAccessToken:$loginResult.accessToken.token")
                val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                appViewModel.signInWithCredential(credential)
            }

            override fun onCancel() {
                Toast.makeText(context, "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
            }
        }
    )

    val signInFailed = appViewModel.signInFailed.observeAsState()
    if (signInFailed.value == true) {
        Toast.makeText(context, appViewModel.errorMessage.value, Toast.LENGTH_LONG)
            .show()
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
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

                Button(
                    onClick = {
                        if (formValidated(email, password)) {
                            appViewModel.signInWithEmail(email, password)
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
                    Text("SIGN IN")
                }

                Text("Forgot password?", modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold)
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
                .background(color = MaterialTheme.colors.onBackground))

            Text("OR", textAlign = TextAlign.Center, fontSize = 24.sp,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.align(Alignment.Center))

            Box(modifier = Modifier
                .height(1.dp)
                .align(Alignment.CenterEnd)
                .fillMaxWidth(.45f)
                .padding(end = 8.dp)
                .background(color = MaterialTheme.colors.onBackground))
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(10f),
            verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // google sign in btn
                SignInOption(
                    icon = painterResource(R.drawable.googleg_standard_color_18),
                    btnText = "Sign in with Google",
                    textColor = Color.Black,
                    btnColor = Color.White
                ) {
                    activityLauncher.launch(googleSignInClient.signInIntent)
                }

                Divider(modifier = Modifier.height(16.dp), color = Color.Transparent)

                // facebook sign in btn
                SignInOption(
                    icon = painterResource(R.drawable.com_facebook_button_icon),
                    btnText = "Sign in with Facebook",
                    textColor = Color.White,
                    btnColor = Color.Blue
                ) {
//                    LoginManager.getInstance().logInWithReadPermissions(context,
//                        listOf("public_profile", "user_friends"))
                }

                Divider(modifier = Modifier.height(16.dp), color = Color.Transparent)

                // create account btn
                SignInOption(
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary),
                    vector = Icons.Filled.Email,
                    btnText = "Create account",
                    textColor = MaterialTheme.colors.onSecondary,
                    btnColor = MaterialTheme.colors.secondary
                ) {
                    createAccount()
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

@Composable
fun SignInOption(colorFilter: ColorFilter? = null, vector: ImageVector, btnText: String,
                 textColor: Color, btnColor: Color, signIn: () -> Unit) {
    Button(onClick = signIn, colors = ButtonDefaults.buttonColors(backgroundColor = btnColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 32.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                imageVector = vector,
                contentDescription = "",
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

private fun formValidated(email: String, password: String): Boolean {
    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
        // show error message "Error: Email and password fields must not be empty"
        return false
    }
    return true
}

@Preview
@Composable
fun PreviewLoginActivity() {
    LoginScreen {}
}