package com.example.eventapp.Organizer.Authentication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.Auth.DataModels.ForgetEmailRequest
import com.example.eventapp.Auth.DataModels.ForgetEmailResponse
import com.example.eventapp.Auth.DataModels.OrganizerForgetEmailRequest
import com.example.eventapp.Auth.DataModels.OrganizerForgetEmailResponse
import com.example.eventapp.R
import com.example.eventapp.User.Authentication.UserForgotPassword
import com.example.eventapp.ExtraFiles.UserTypeRegistration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrganizerForgotPassword : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var nextBtn: Button
    lateinit var signUp: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer_forgot_password)

        email = findViewById(R.id.emailEditText)
        nextBtn = findViewById(R.id.nextBtn)
        signUp = findViewById(R.id.signup)

        signUp.setOnClickListener {
            val intent = Intent(this, UserTypeRegistration::class.java)
            startActivity(intent)
        }
        nextBtn.setOnClickListener {
            val emailText = email.text.toString().trim()

            if (!isValidEmail(emailText)) {
                showEmailError("Invalid email format")
                return@setOnClickListener
            }
            val request = OrganizerForgetEmailRequest(emailText)

            ApiClient.instance.organizerForgetPassword(request).enqueue(object : Callback<OrganizerForgetEmailResponse> {
                override fun onResponse(call: Call<OrganizerForgetEmailResponse>, response: Response<OrganizerForgetEmailResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@OrganizerForgotPassword,
                            "Mail sent successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("ForgotPassword", "Error response: $errorBody")
                        showEmailError("Error: ${response.body()?.message ?: "Unknown error"}")
                    }
                }

                override fun onFailure(call: Call<OrganizerForgetEmailResponse?>, t: Throwable) {
                    showEmailError("Failure: ${t.message}")
                }
            })
}
        }
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showEmailError(message: String) {
        email.error = message
        email.setTextColor(Color.RED)
        email.requestFocus()
    }

}