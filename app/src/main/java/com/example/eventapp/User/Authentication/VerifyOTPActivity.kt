package com.example.eventapp.User.Authentication

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
import com.example.eventapp.Auth.DataModels.OtpSendRequest
import com.example.eventapp.Auth.DataModels.OtpSendResponse
import com.example.eventapp.Auth.DataModels.OtpVerificationRequest
import com.example.eventapp.Auth.DataModels.OtpVerificationResponse
import com.example.eventapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyOTPActivity : AppCompatActivity() {

    private lateinit var resendTextView: TextView
    private lateinit var email: String
    private var isResendEnabled = false
    private var timer: CountDownTimer? = null
//    lateinit var signUp: TextView


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

//        signUp.setOnClickListener {
//            val intent = Intent(this, UserTypeRegistration::class.java)
//            startActivity(intent)
//        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Email not provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Restrict each EditText to one digit
        setEditTextInputType(code1)
        setEditTextInputType(code2)
        setEditTextInputType(code3)
        setEditTextInputType(code4)

        // Start the resend timer
        startResendTimer()

        resendTextView.setOnClickListener {
            if (isResendEnabled) {
                resendOTP(email)
            }
        }

        // Handle OTP verification
        nextBtn.setOnClickListener {
            val otp = code1.text.toString() + code2.text.toString() + code3.text.toString() + code4.text.toString()

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
    }

    private fun setEditTextInputType(editText: EditText) {
        editText.inputType = InputType.TYPE_CLASS_NUMBER

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length == 1) {
                    when (editText.id) {
                        R.id.code1 -> findViewById<EditText>(R.id.code2).requestFocus()
                        R.id.code2 -> findViewById<EditText>(R.id.code3).requestFocus()
                        R.id.code3 -> findViewById<EditText>(R.id.code4).requestFocus()
                    }
                }
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
        val request = OtpSendRequest(email)

        ApiClient.instance.otpSend(request)
            .enqueue(object : Callback<OtpSendResponse> {
                override fun onResponse(call: Call<OtpSendResponse>, response: Response<OtpSendResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@VerifyOTPActivity, "OTP resent successfully", Toast.LENGTH_SHORT).show()
                        startResendTimer()
                    } else {
                        val error = response.errorBody()?.string()
                        Log.e("OtpSendError", "Response failed: $error")
                        Toast.makeText(this@VerifyOTPActivity, "Resend failed: $error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OtpSendResponse>, t: Throwable) {
                    Log.e("OtpSendError", "Failure: ${t.message}", t)
                    Toast.makeText(this@VerifyOTPActivity, "Failed to resend OTP: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun verifyOTP(email: String, otp: Int) {
        val request = OtpVerificationRequest(email, otp)

        ApiClient.instance.otpVerify(request)
            .enqueue(object : Callback<OtpVerificationResponse> {
                override fun onResponse(call: Call<OtpVerificationResponse>, response: Response<OtpVerificationResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@VerifyOTPActivity, "OTP Verified", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@VerifyOTPActivity, UserSignUp::class.java)
                        intent.putExtra("email_key", email)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@VerifyOTPActivity, "Invalid OTP: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OtpVerificationResponse>, t: Throwable) {
                    Toast.makeText(this@VerifyOTPActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
