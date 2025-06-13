package com.example.eventapp.Organizer.Authentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.Auth.DataModels.OrganizerOtpSendRequest
import com.example.eventapp.Auth.DataModels.OrganizerOtpSendResponse
import com.example.eventapp.Auth.DataModels.OtpSendRequest
import com.example.eventapp.Auth.DataModels.OtpSendResponse
import com.example.eventapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrganizerSignUpEmailID : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer_sign_up_email_id)

        val nextBtn = findViewById<Button>(R.id.nextBtn)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)

        nextBtn.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            // Validate email
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Send OTP
            val request = OrganizerOtpSendRequest(email)

            // Send OTP using organizer endpoint
            ApiClient.instance.organizerOtpSend(request)
                .enqueue(object : Callback<OrganizerOtpSendResponse> {
                    override fun onResponse(
                        call: Call<OrganizerOtpSendResponse>,
                        response: Response<OrganizerOtpSendResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@OrganizerSignUpEmailID,
                                "OTP sent",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(
                                this@OrganizerSignUpEmailID,
                                OrganizerVerifyOTPActivity::class.java
//                                OrganizerSignUp::class.java
                            )
                            intent.putExtra("email_key", email)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@OrganizerSignUpEmailID,
                                "Failed to send OTP: ${response.errorBody()?.string()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<OrganizerOtpSendResponse>, t: Throwable) {
                        Toast.makeText(
                            this@OrganizerSignUpEmailID,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

        }
    }
}
