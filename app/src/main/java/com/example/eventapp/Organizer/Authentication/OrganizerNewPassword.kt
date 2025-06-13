package com.example.eventapp.Organizer.Authentication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.Auth.DataModels.NewPasswordRequest
import com.example.eventapp.Auth.DataModels.NewPasswordResponse
import com.example.eventapp.Auth.DataModels.OrganizerNewPasswordRequest
import com.example.eventapp.Auth.DataModels.OrganizerNewPasswordResponse
import com.example.eventapp.R
import com.example.eventapp.User.Authentication.UserNewPassword
import com.example.eventapp.ExtraFiles.UserTypeRegistration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrganizerNewPassword : AppCompatActivity() {
    private lateinit var signUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer_new_password)

        val newPasswordEditText = findViewById<EditText>(R.id.newPassword)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPassword)
        val confirmButton = findViewById<AppCompatButton>(R.id.loginButton)
        signUp = findViewById<TextView>(R.id.signup)

        val email = intent.getStringExtra("email") ?: ""
        if (email.isEmpty()) {
            Toast.makeText(this, "Email not received", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        signUp.setOnClickListener {
            val intent = Intent(this, UserTypeRegistration::class.java)
            startActivity(intent)
        }
        confirmButton.setOnClickListener {
            val newPassword = newPasswordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (newPassword.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val token = intent.getStringExtra("token") ?: ""
            if (token.isEmpty()) {
                Toast.makeText(this, "Token not received", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = OrganizerNewPasswordRequest(
                password = newPassword,
                confirm_password = confirmPassword
            )

            ApiClient.instance.organizerNewPassword(token, request)
                .enqueue(object : Callback<OrganizerNewPasswordResponse> {
                    override fun onResponse(
                        call: Call<OrganizerNewPasswordResponse>,
                        response: Response<OrganizerNewPasswordResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@OrganizerNewPassword,
                                "Password changed successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Optionally navigate to login
                        } else {
                            Toast.makeText(
                                this@OrganizerNewPassword,
                                "Error: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<OrganizerNewPasswordResponse>, t: Throwable) {
                        Toast.makeText(
                            this@OrganizerNewPassword,
                            "Failed: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })



        }
}}