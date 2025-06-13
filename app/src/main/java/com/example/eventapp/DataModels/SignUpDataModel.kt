package com.example.eventapp.Auth.DataModels

data class SignUpDataModel (
    val first_name: String,
    val last_name: String,
    val email: String,
    val dob:String,
    val phone: String,
    val gender: String,
    val password: String,
)

data class SignUpResponse(
    val message: String
)

data class OrganizerSignUpDataModel(
    val company_name: String,
    val email: String,
    val phone: String,
    val password: String
)

data class OrganizerSignUpDataModelResponse(
    val message: String
)