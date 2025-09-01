package com.example.krishimitra.presentation.auth_screen

import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krishimitra.Constants
import com.example.krishimitra.domain.farmer_data.UserDataModel
import com.example.krishimitra.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val repo: Repo
) : ViewModel() {


    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()


    init {
        getLanguage()
    }

    fun getLocation() {
        _state.update {
            it.copy(
                isLocationLoading = true
            )
        }
        viewModelScope.launch(Dispatchers.IO){

            try {
                val location = repo.getLocation()
                if(location!=null){
                    _state.update {
                        it.copy(
                            isLocationLoading = false,
                            location = location
                        )
                    }
                    Log.d("CHECK",location.toString())

                }
                else{
                    _state.update {
                        it.copy(
                            location = location,
                            isLocationLoading = false,
                            isError = "Unable to access location"
                        )
                    }
                    Log.d("CHECK","Error")

                }
            }catch (e: Exception){
                _state.update {
                    it.copy(
                        isLocationLoading = false,
                        isError = e.message
                    )
                }
                Log.d("CHECK",e.message.toString())

            }
        }
    }

    private fun getLanguage() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getLanguage().collectLatest { langcode ->
                _state.update {
                    it.copy(
                        currentLanguage = Constants.SUPPORTED_LANGUAGES.find { it.code == langcode }?.nativeName
                            ?: "English"
                    )
                }
            }

        }
    }


    fun changeLanguage(
        lang_code: String
    ) {
        viewModelScope.launch {
            repo.changeLanguage(lang_code)
            getLanguage()

        }
    }

    fun signIn(
        userData: UserDataModel
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isSignLoading = true) }
            firebaseAuth.signInWithEmailAndPassword(userData.email, userData.password)
                .addOnSuccessListener {
                    _state.update {
                        it.copy(
                            isSuccess = true
                        )
                    }
                }
                .addOnFailureListener { error ->
                    _state.update {
                        it.copy(
                            isError = error.message,
                            isSignLoading = false
                        )
                    }
                }

        }
    }


    fun signUp(
        userData: UserDataModel
    ) {
        _state.update { it.copy(isSignLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnSuccessListener {
                    fireStore.collection("users")
                        .document(firebaseAuth.uid ?: "Unknown")
                        .set(userData)
                        .addOnSuccessListener {
                            _state.update {
                                it.copy(
                                    isError = null,
                                    isSuccess = true
                                )
                            }
                        }
                        .addOnFailureListener { error ->
                            _state.update {
                                it.copy(
                                    isError = error.message,
                                    isSignLoading = false
                                )
                            }
                        }

                }
                .addOnFailureListener { error ->
                    _state.update {
                        it.copy(
                            isError = error.message,
                            isSignLoading = false
                        )
                    }
                }
        }
    }


}