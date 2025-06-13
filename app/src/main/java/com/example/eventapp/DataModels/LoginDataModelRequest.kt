package com.example.eventapp.Auth.DataModels

data class LoginDataModelRequest(val email: String, val password: String)

data class LoginResponse(
    val message: String,
    val token: String,
    val attendeedata: AttendeeData
)

data class AttendeeData(
    val _id: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: Long,
    val role: String,
    val gender: String,
    val dob: String,
    val password: String,
    val image: String,
    val isdelete: Boolean,
    val isThirteenPlus: Boolean,
    val isActive: Boolean,
    val isadmindelete: Boolean,
    val admin_msg: String,
    val isverify: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class OrganizerLoginDataModelRequest(val email: String, val password: String)

data class OrganizerLoginResponse(
    val message: String,
    val token: String,
    val userType: String
)