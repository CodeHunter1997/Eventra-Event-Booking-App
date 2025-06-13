package com.example.eventapp.Organizer.Authentication

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.Auth.DataModels.OrganizerOtpSendRequest
import com.example.eventapp.Auth.DataModels.OrganizerOtpSendResponse
import com.example.eventapp.Auth.DataModels.OrganizerOtpVerificationRequest
import com.example.eventapp.Auth.DataModels.OrganizerOtpVerificationResponse
import com.example.eventapp.Auth.DataModels.OtpSendRequest
import com.example.eventapp.Auth.DataModels.OtpSendResponse
import com.example.eventapp.Auth.DataModels.OtpVerificationRequest
import com.example.eventapp.Auth.DataModels.OtpVerificationResponse
import com.example.eventapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrganizerVerifyOTPActivity : AppCompatActivity() {

    private lateinit var resendTextView: TextView
    //    private lateinit var signUp: TextView
    private var isResendEnabled = false
    private var timer: CountDownTimer? = null
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otpactivity)

        // Get email from intent
        email = intent.getStringExtra("email_key") ?: ""

//        signUp = findViewById(R.id.signup)
        resendTextView = findViewById(R.id.resendTextView)

        val code1 = findViewById<EditText>(R.id.code1)
        val code2 = findViewById<EditText>(R.id.code2)
        val code3 = findViewById<EditText>(R.id.code3)
        val code4 = findViewById<EditText>(R.id.code4)
        val nextBtn = findViewById<AppCompatButton>(R.id.nextBtn)

        if (email.isBlank()) {
            Toast.makeText(this, "Email not provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set numeric input type and auto-move focus
        setupOtpEditText(code1, code2)
        setupOtpEditText(code2, code3)
        setupOtpEditText(code3, code4)

        startResendTimer()

        resendTextView.setOnClickListener {
            if (isResendEnabled) {
                resendOTP(email)
            }
        }

        nextBtn.setOnClickListener {
            val otp = code1.text.toString() + code2.text.toString() +
                    code3.text.toString() + code4.text.toString()

            if (otp.length != 4) {
                Toast.makeText(this, "Please enter the full 4-digit OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val otpInt = otp.toInt()
                verifyOTP(email, otpInt)
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Invalid OTP format", Toast.LENGTH_SHORT).show()
            }
        }


//        signUp.setOnClickListener {
//            val intent = Intent(this, OrganizerSignUp::class.java)
//            intent.putExtra("email_key", email)
//            startActivity(intent)
//        }
    }

    private fun setupOtpEditText(current: EditText, next: EditText) {
        current.inputType = InputType.TYPE_CLASS_NUMBER
        current.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) next.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun startResendTimer() {
        isResendEnabled = false
        resendTextView.isClickable = false

        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                resendTextView.text = "Resend in ${seconds}s"
            }

            override fun onFinish() {
                resendTextView.text = "Resend OTP"
                resendTextView.isClickable = true
                isResendEnabled = true
            }
        }.start()
    }

    private fun resendOTP(email: String) {
        val request = OrganizerOtpSendRequest(email)

        ApiClient.instance.organizerOtpSend(request).enqueue(object : Callback<OrganizerOtpSendResponse> {
            override fun onResponse(call: Call<OrganizerOtpSendResponse>, response: Response<OrganizerOtpSendResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@OrganizerVerifyOTPActivity, "OTP resent successfully", Toast.LENGTH_SHORT).show()
                    startResendTimer()
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("OtpSendError", "Response failed: $error")
                    Toast.makeText(this@OrganizerVerifyOTPActivity, "Resend failed: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OrganizerOtpSendResponse>, t: Throwable) {
                Log.e("OtpSendError", "Failure: ${t.message}", t)
                Toast.makeText(this@OrganizerVerifyOTPActivity, "Failed to resend OTP: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun verifyOTP(email: String, otp: Int) {
        val request = OrganizerOtpVerificationRequest(email, otp)

        ApiClient.instance.organizerOtpVerify(request).enqueue(object : Callback<OrganizerOtpVerificationResponse> {
            override fun onResponse(call: Call<OrganizerOtpVerificationResponse>, response: Response<OrganizerOtpVerificationResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@OrganizerVerifyOTPActivity, "OTP Verified", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@OrganizerVerifyOTPActivity, OrganizerSignUp::class.java)
                    intent.putExtra("email_key", email)
                    startActivity(intent)
                    finish()
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("VerifyOTPError", "Response failed: $error")
                    Toast.makeText(this@OrganizerVerifyOTPActivity, "Invalid OTP: $error", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<OrganizerOtpVerificationResponse>, t: Throwable) {
                Toast.makeText(this@OrganizerVerifyOTPActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
