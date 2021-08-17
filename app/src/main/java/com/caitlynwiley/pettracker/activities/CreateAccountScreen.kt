package com.caitlynwiley.pettracker.activities

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
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
                    startBaseActivity(context = context)
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

private fun startBaseActivity(context: Context) {
    startActivity(context, Intent(context, BaseActivity::class.java), null)
}

@Composable
fun CreateAccount(onSubmit: (String, String, String) -> Unit, errorMsg: String = "") {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordAgain by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text("Email", modifier = Modifier.padding(4.dp))
        TextField(value = email, modifier = Modifier.fillMaxWidth(),
            onValueChange = { email = it })

        Text("Enter your password", modifier = Modifier.padding(4.dp))
        TextField(
            value = password,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { password = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Text("Enter your password again", modifier = Modifier.padding(4.dp))
        TextField(
            value = passwordAgain,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { passwordAgain = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Button(
            onClick = { onSubmit(email, password, passwordAgain) }, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
        )
        {
            Text("CREATE")
        }

        Text(
            errorMsg, color = MaterialTheme.colors.error,
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun PreviewCreateAccount() {
    CreateAccount(fun(_, _, _) {})
}