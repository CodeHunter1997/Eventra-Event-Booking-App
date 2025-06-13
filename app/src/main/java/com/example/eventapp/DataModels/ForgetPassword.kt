package com.example.eventapp.Auth.DataModels



data class OtpSendRequest(
    val email: String
)
data class OtpSendResponse(
    val message: String
)

data class OtpVerificationRequest(
    val email: String,
    val otp: Int
)
data class OtpVerificationResponse(
    val message: String
)

data class ForgetEmailRequest(
    val email: String
)
data class ForgetEmailResponse(
    val message: String
)

data class NewPasswordRequest(
    val password: String,
    val confirm_password: String

)
data class NewPasswordResponse(
    val success: Boolean,
    val status: Int,
    val message: String
)

data class DeleteAccountRequest(
    val email: String
)
data class DeleteAccountResponse(
    val message: String,
    val status: Int
)

data class ChangePasswordRequest(
    val current_password: String,
    val password: String,
    val confirm_password: String
)
data class ChangePasswordResponse(
    val message: String
)


//Organizer

data class OrganizerOtpSendRequest(
    val email: String
)
data class OrganizerOtpSendResponse(
    val message: String
)

data class OrganizerOtpVerificationRequest(
    val email: String,
    val otp: Int
)
data class OrganizerOtpVerificationResponse(
    val message: String
)

data class OrganizerForgetEmailRequest(
    val email: String
)
data class OrganizerForgetEmailResponse(
    val message: String
)

data class OrganizerNewPasswordRequest(
    val password: String,
    val confirm_password: String
)
data class OrganizerNewPasswordResponse(
    val success: Boolean,
    val status: Int,
    val message: String
)