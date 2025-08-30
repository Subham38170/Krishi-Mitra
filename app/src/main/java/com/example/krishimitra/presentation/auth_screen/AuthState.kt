package com.example.krishimitra.presentation.auth_screen

data class AuthState(
    val signIn: String = "Login",
    val signUp: String = "SignUp",
    val email: String = "Email",
    val password: String = "Password",
    val isError: String? = null,
    val isSuccess: Boolean = false,
    val isSignLoading: Boolean = true
)
