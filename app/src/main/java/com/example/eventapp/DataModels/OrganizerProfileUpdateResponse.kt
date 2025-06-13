package com.example.eventapp.DataModels

data class OrganizerProfileUpdateResponse(
    val status: Int,
    val message: String

)
data class OrganizerProfileUpdateRequest(
    val company_info: String,
    val company_name: String,
    val image: String,
    val email: String,
    val phone: Long
)