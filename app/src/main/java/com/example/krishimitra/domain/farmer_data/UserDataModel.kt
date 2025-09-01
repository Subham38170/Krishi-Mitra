package com.example.krishimitra.domain.farmer_data

data class UserDataModel(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val village: String = "",
    val district: String = "",
    val state: String = "",
    val mobileNo: String = "",
    val pinCode: String = ""
)