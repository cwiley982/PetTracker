package com.caitlynwiley.pettracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class AppWideViewModel: ViewModel() {
    private val _user = MutableLiveData(Firebase.auth.currentUser)
    val user: LiveData<FirebaseUser?> = _user

    private val _signInFailed = MutableLiveData(false)
    val signInFailed: LiveData<Boolean> = _signInFailed

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    init {
        Firebase.auth.addAuthStateListener { firebaseAuth: FirebaseAuth ->
            Log.d("AppViewModel", "Auth state changed")
            _user.postValue(firebaseAuth.currentUser)
        }
    }

    fun signInWithCredential(credential: AuthCredential) {
        _signInFailed.postValue(false)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(Dispatchers.Default.asExecutor()) { task: Task<AuthResult?> ->
                if (!task.isSuccessful) {
                    Log.w("AppViewModel", "signInWithCredential:failure", task.exception)
                    _user.postValue(null)
                    _signInFailed.postValue(true)
                    _errorMessage.postValue((task.exception as FirebaseException?)?.message ?: "Authentication Failed.")
                }
            }
    }

    fun signInWithEmail(email: String, password: String) {
        _signInFailed.postValue(false)
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(Dispatchers.Default.asExecutor()) { task: Task<AuthResult?> ->
                if (!task.isSuccessful) {
                    Log.w("AppViewModel", "signInWithEmailAndPassword:failure", task.exception)
                    _user.postValue(null)
                    _signInFailed.postValue(true)
                    _errorMessage.postValue((task.exception as FirebaseException?)?.message ?: "Authentication Failed.")
                }
            }
    }
}