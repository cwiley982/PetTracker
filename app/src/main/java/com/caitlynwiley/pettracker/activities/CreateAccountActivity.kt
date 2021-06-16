package com.caitlynwiley.pettracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.caitlynwiley.pettracker.models.Account
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateAccountActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val goToNewPetActivity = fun() {
            startActivity(Intent(this@CreateAccountActivity, NewPetActivity::class.java))
        }
        val goToMainActivity = fun() {
            startActivity(Intent(this@CreateAccountActivity, MainActivity::class.java))
        }

        setContent {
            Surface {
                Content(goToNewPetActivity, goToMainActivity)
            }
        }
    }

    @Composable
    fun Content(newPetActivity: () -> Unit, mainActivity: () -> Unit) {
        val auth = FirebaseAuth.getInstance()
        var errorMsg by remember { mutableStateOf("") }
        val submit = fun(email: String, password: String, passwordAgain: String) {
            if (password != passwordAgain) {
                // hide keyboard
                errorMsg = "Passwords do not match. Please try again."
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val dbRef = FirebaseDatabase.getInstance().reference
                        val newAccount = Account(auth.uid!!, email)
                        dbRef.child("users").child(auth.uid!!).setValue(newAccount)
                        dbRef.child("users").child(auth.currentUser!!.uid).child("num_pets")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.value == null || dataSnapshot.value == 0) {
                                        newPetActivity()
                                    } else {
                                        mainActivity()
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                    } else {
                        errorMsg = it.exception!!.message.toString()
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

        Column(modifier = Modifier.padding(8.dp)) {
            Text("Email", modifier = Modifier.padding(4.dp))
            TextField(value = email, onValueChange = { email = it })

            Text("Enter your password", modifier = Modifier.padding(4.dp))
            TextField(value = password, onValueChange = { password = it })

            Text("Enter your password again", modifier = Modifier.padding(4.dp))
            TextField(value = passwordAgain, onValueChange = { passwordAgain = it })

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
                errorMsg, color = Color.Red,
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
}