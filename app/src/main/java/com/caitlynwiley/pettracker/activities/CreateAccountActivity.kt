package com.caitlynwiley.pettracker.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.models.Account
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateAccountActivity : BaseActivity() {
    private var mEmail: EditText? = null
    private var mPassword: EditText? = null
    private var mPasswordRepeated: EditText? = null
    private var mErrorText: TextView? = null
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.reference
    private var mAuth: FirebaseAuth? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)
        mAuth = FirebaseAuth.getInstance()
        mEmail = findViewById(R.id.email_field)
        mPassword = findViewById(R.id.password_field_one)
        mPasswordRepeated = findViewById(R.id.password_field_two)
        mErrorText = findViewById(R.id.error_msg)
        val createBtn = findViewById<Button>(R.id.create_btn)
        createBtn.setOnClickListener {
            val email = mEmail?.text.toString()
            // check passwords
            if (mPassword!!.text.toString() != mPasswordRepeated!!.text.toString()) {
                hideKeyboard()
                showError("Passwords do not match. Please try again.")
                return@setOnClickListener
            }
            val t = mAuth!!.createUserWithEmailAndPassword(email, mPassword!!.text.toString())
            t.addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    // create account
                    val a = Account(mAuth!!.uid!!, email)
                    ref.child("users").child(mAuth!!.uid!!).setValue(a)
                    ref.child("users").child(mAuth!!.currentUser!!.uid).child("num_pets").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.value == null || dataSnapshot.value == 0) {
                                startActivity(Intent(this@CreateAccountActivity, AddPetActivity::class.java))
                            } else {
                                startActivity(Intent(this@CreateAccountActivity, MainActivity::class.java))
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                } else {
                    val msg = ""
                    /*if (errorCode == FIRAuthErrorCodeEmailAlreadyInUse) {
                        msg = "Email address already in use. Please use a different email.";
                    } else {
                        msg = "Account creation failed with message: " + task.getException().getMessage();
                    }*/showError(msg)
                    hideKeyboard()
                }
            }
        }
    }

    private fun showError(errorMessage: String) {
        mErrorText!!.text = errorMessage
    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = this.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        /* Indicates the email used to attempt a sign up is already in use. */
        private const val FIRAuthErrorCodeEmailAlreadyInUse = 17007
    }
}