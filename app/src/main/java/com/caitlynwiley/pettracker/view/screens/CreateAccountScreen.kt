package com.caitlynwiley.pettracker.view.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.caitlynwiley.pettracker.view.BaseActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun CreateAccountScreen() {
    val context = LocalContext.current
    var errorMsg by remember { mutableStateOf("") }
    val submit = fun (email: String, password: String, passwordAgain: String) {
        if (password != passwordAgain) {
            // hide keyboard
            errorMsg = "Passwords do not match. Please try again."
        } else {
            Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("NewAccount", "starting base activity")
                    startActivity(context, Intent(context, BaseActivity::class.java), null)
                } else {
                    it.exception?.message?.let { msg ->
                        errorMsg = msg
                    }
                }
            }
        }
    }

    CreateAccount(onSubmit = submit, errorMsg)
}

@Composable
fun CreateAccount(onSubmit: (String, String, String) -> Unit, errorMsg: String = "") {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordAgain by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().padding(all = 16.dp)) {
        Column(modifier = Modifier
            .fillMaxWidth()) {
            Text("Email", modifier = Modifier.padding(4.dp))
            TextField(
                value = email,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { email = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Text("Enter your password", modifier = Modifier.padding(4.dp))
            TextField(
                value = password,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { password = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Text("Confirm your password", modifier = Modifier.padding(4.dp))
            TextField(
                value = passwordAgain,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { passwordAgain = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Text(
                text = errorMsg,
                color = MaterialTheme.colors.error,
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
                    .alpha(if (errorMsg == "") 0f else 1f)
            )
        }

        Button(
            onClick = { onSubmit(email, password, passwordAgain) },
            modifier = Modifier
                .align(Alignment.BottomEnd),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
        ) {
            Text("Create")
        }
    }
}

@Preview
@Composable
fun PreviewCreateAccount() {
    CreateAccount(fun(_, _, _) {}, "error")
}