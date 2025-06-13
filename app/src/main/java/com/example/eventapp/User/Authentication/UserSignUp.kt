package com.example.eventapp.User.Authentication

import android.app.DatePickerDialog
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.Auth.DataModels.SignUpDataModel
import com.example.eventapp.Auth.DataModels.SignUpResponse
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.HomePage.UserHomePage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class UserSignUp : AppCompatActivity() {

    lateinit var firstName: EditText
    lateinit var lastName: EditText
        lateinit var dob: EditText
    lateinit var gender: EditText
    lateinit var emailId: EditText
    lateinit var phoneNumber: EditText
    lateinit var enterYourPassword: EditText
    lateinit var confirmPassword: EditText
    lateinit var signUpBtn: Button
//    lateinit var signUpWithGoogleBtn: Button
    lateinit var login: TextView
    lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)

        // Initialize UI elements
        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        dob = findViewById(R.id.emailEdt)
        gender = findViewById(R.id.editTextGender)
        emailId = findViewById(R.id.emailID)
        phoneNumber = findViewById(R.id.number)
        enterYourPassword = findViewById(R.id.passwordEditText)
        confirmPassword = findViewById(R.id.confirmPassword)
        signUpBtn = findViewById(R.id.signupBtn)
//        signUpWithGoogleBtn = findViewById(R.id.signUpWithGoogle)
        login = findViewById(R.id.loginTxt)

        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        val receivedEmail = intent.getStringExtra("email_key")
        emailId.setText(receivedEmail)
        emailId.isEnabled = false


        // Setup listeners for input fields
        setupCapitalization(firstName) { signUpViewModel.firstName.value = it }
        setupCapitalization(lastName) { signUpViewModel.lastName.value = it }
        emailId.addTextChangedListener(textWatcher { signUpViewModel.email.value = receivedEmail })
        phoneNumber.addTextChangedListener(textWatcher { signUpViewModel.phoneNumber.value = it })
        enterYourPassword.addTextChangedListener(textWatcher { signUpViewModel.password.value = it })
        confirmPassword.addTextChangedListener(textWatcher { signUpViewModel.confirmPassword.value = it })

        // Setup DatePicker for DOB
        dob.setOnClickListener { showDatePickerDialog() }

        // Gender Picker Dialog
        gender.setOnClickListener {
            val options = arrayOf("male", "female", "other")
            AlertDialog.Builder(this)
                .setTitle("Select Gender")
                .setItems(options) { _, which ->
                    val selected = options[which]
                    gender.setText(selected)
                    signUpViewModel.gender.value = selected
                }.show()
        }

        setupPasswordToggle(enterYourPassword)
        setupPasswordToggle(confirmPassword)

        // Register button click listener
        signUpBtn.setOnClickListener {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            val phonePattern = "^[0-9]{10}$"
            val passwordPattern =
                "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}$"

            val fname = firstName.text.toString().trim()
            val lname = lastName.text.toString().trim()
            val dobText = dob.text.toString().trim()
            val genderText = gender.text.toString().trim()
            val email = emailId.text.toString().trim()
            val phone = phoneNumber.text.toString().trim()
            val password = enterYourPassword.text.toString().trim()
            val confirmPass = confirmPassword.text.toString().trim()

            if (fname.isEmpty() || lname.isEmpty() || dobText.isEmpty() || genderText.isEmpty()
                || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPass.isEmpty()
            ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.matches(emailPattern.toRegex())) {
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!phone.matches(phonePattern.toRegex())) {
                Toast.makeText(this, "Enter a valid 10-digit phone number", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (!password.matches(passwordPattern.toRegex())) {
                Toast.makeText(
                    this,
                    "Password must be at least 8 characters and include a number & special character",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (password != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = SignUpDataModel(
                first_name = fname,
                last_name = lname,
                email = email,
                dob = dobText,
                phone = phone,
                gender = genderText,
                password = password,
            )
            ApiClient.instance.registerUser(request).enqueue(object : Callback<SignUpResponse> {
                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {
                    val responseBody = response.body()
                    Log.d("SignUpDebug", "Raw response: $responseBody")

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@UserSignUp,
                            "Registration successful! Please Login",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@UserSignUp, UserLogin::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@UserSignUp,
                            "Failed: ${responseBody?.message ?: "Unknown error"}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e(
                            "SignUpDebug",
                            "Failed response: Code ${response.code()} - ${
                                response.errorBody()?.string()
                            }"
                        )
                    }
                }

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    Toast.makeText(
                        this@UserSignUp,
                        "Network error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("SignUpDebug", "onFailure: ${t.localizedMessage}", t)
                }
            })
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(this, { _, y, m, d ->
            val date = String.format("%04d-%02d-%02d", y, m + 1, d)
            dob.setText(date)
        }, year, month, day)

        dialog.datePicker.maxDate = System.currentTimeMillis()
        dialog.show()
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
