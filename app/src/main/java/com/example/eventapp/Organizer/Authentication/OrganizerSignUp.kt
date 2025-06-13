package com.example.eventapp.Organizer.Authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.Auth.DataModels.OrganizerSignUpDataModel
import com.example.eventapp.Auth.DataModels.OrganizerSignUpDataModelResponse
import com.example.eventapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrganizerSignUp : AppCompatActivity() {

    lateinit var companyName: EditText
    lateinit var emailId: EditText
    lateinit var phoneNumber: EditText
    lateinit var enterYourPassword: EditText
    lateinit var confirmPassword: EditText
    lateinit var signUpBtn: Button
    lateinit var login: TextView
    lateinit var signUpViewModel: OrganizerSignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer_sign_up)

        companyName = findViewById(R.id.et_date)
        emailId = findViewById(R.id.emailID)
        phoneNumber = findViewById(R.id.number)
        enterYourPassword = findViewById(R.id.passwordEditText)
        confirmPassword = findViewById(R.id.confirmPassword)
        signUpBtn = findViewById(R.id.signupBtn)
        login = findViewById(R.id.loginTxt)

        val verifiedEmail = intent.getStringExtra("email_key") ?: ""
        emailId.setText(verifiedEmail)
        emailId.isEnabled = false

        signUpViewModel = ViewModelProvider(this)[OrganizerSignUpViewModel::class.java]

        setupCapitalization(companyName) { signUpViewModel.companyName.value = it }
        emailId.addTextChangedListener(textWatcher { signUpViewModel.email.value = verifiedEmail })
        phoneNumber.addTextChangedListener(textWatcher { signUpViewModel.phoneNumber.value = it })
        enterYourPassword.addTextChangedListener(textWatcher {
            signUpViewModel.password.value = it
        })
        confirmPassword.addTextChangedListener(textWatcher {
            signUpViewModel.confirmPassword.value = it
        })
        setupPasswordToggle(enterYourPassword)
        setupPasswordToggle(confirmPassword)


        signUpBtn.setOnClickListener {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            val phonePattern = "^[0-9]{10}$"
            val passwordPattern =
                "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}$"
            val email = emailId.text.toString().trim()
            val phone = phoneNumber.text.toString().trim()
            val password = enterYourPassword.text.toString().trim()
            val confirmPass = confirmPassword.text.toString().trim()
            val company_name = companyName.text.toString().trim()

            if (company_name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPass.isEmpty()
            ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.matches(emailPattern.toRegex())) {
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!phone.matches(phonePattern.toRegex())) {
                Toast.makeText(this, "Enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!password.matches(passwordPattern.toRegex())) {
                Toast.makeText(
                    this, "Password must be at least 8 characters and include a number & special character", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = OrganizerSignUpDataModel(
                company_name = company_name,
                email = email,
                phone = phone,
                password = password
            )
            ApiClient.instance.registerOrganizer(request).enqueue(object : Callback<OrganizerSignUpDataModelResponse>{
                override fun onResponse(call: Call<OrganizerSignUpDataModelResponse>, response: Response<OrganizerSignUpDataModelResponse>){
                    val responseBody = response.body()
//                    Log.d("SignUpDebug", "Raw response: $responseBody")
                    if (response.isSuccessful && responseBody?.message?.contains("Organizer registered successfully", ignoreCase = true) == true){
                        Toast.makeText(this@OrganizerSignUp, "Registration successful! Please login", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@OrganizerSignUp, OrganizerLogin::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        val errorMsg = responseBody?.message ?: "Unknown error"
                        Log.e("OrganizerSignUp", "Signup failed: $errorMsg") // Log the error
                        Toast.makeText(this@OrganizerSignUp,"Failed: ${responseBody?.message ?: "Unknown error"}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<OrganizerSignUpDataModelResponse?>, t: Throwable) {
                    Toast.makeText(this@OrganizerSignUp, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}
    private fun textWatcher(onChange: (String) -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                onChange(s.toString())
            }
        }
    }

    private fun setupCapitalization(editText: EditText, onTextChanged: (String) -> Unit) {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.isNotEmpty()) {
                    val capitalized = input.replaceFirstChar { it.uppercaseChar() }
                    if (capitalized != input) {
                        editText.removeTextChangedListener(this)
                        editText.setText(capitalized)
                        editText.setSelection(capitalized.length)
                        editText.addTextChangedListener(this)
                    }
                    onTextChanged(capitalized)
                }
            }
        }
        editText.addTextChangedListener(watcher)
    }

    private fun setupPasswordToggle(editText: EditText) {
        var isVisible = false
        val originalTypeface = editText.typeface

        editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                val drawable = editText.compoundDrawables[drawableEnd]
                if (drawable != null) {
                    val drawableWidth = drawable.bounds.width()
                    val extraClickArea = 40
                    if (event.rawX >= (editText.right - drawableWidth - extraClickArea)) {
                        isVisible = !isVisible
                        editText.inputType = if (isVisible) {
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        } else {
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        }
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, if (isVisible) R.drawable.eye_on else R.drawable.eye, 0
                        )
                        editText.typeface = originalTypeface
                        editText.setSelection(editText.text.length)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }

}