package com.example.eventapp.User.Authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.Auth.DataModels.OtpSendRequest
import com.example.eventapp.Auth.DataModels.OtpSendResponse
import com.example.eventapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSignupEmailID : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_signup_email_id)
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
            val request = OtpSendRequest(email)

            ApiClient.instance.otpSend(request)
                .enqueue(object : Callback<OtpSendResponse> {
                    override fun onResponse(
                        call: Call<OtpSendResponse>,
                        response: Response<OtpSendResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@UserSignupEmailID, "OTP sent", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@UserSignupEmailID, VerifyOTPActivity::class.java)
//                            val intent = Intent(this@UserSignupEmailID, UserSignUp::class.java)
                            intent.putExtra("email_key", email)
                            startActivity(intent)
                            Log.d("OTP_DEBUG", "Response: ${response.code()} - ${response.errorBody()?.string()}")

                        } else {
                            Toast.makeText(this@UserSignupEmailID, "Failed to send OTP: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<OtpSendResponse>, t: Throwable) {
                        Log.e("OTP_DEBUG", "Error", t)
                        Toast.makeText(this@UserSignupEmailID, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
