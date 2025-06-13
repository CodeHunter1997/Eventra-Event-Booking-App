package com.example.eventapp.User.Authentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.Auth.DataModels.LoginDataModelRequest
import com.example.eventapp.Auth.DataModels.LoginResponse
import com.example.eventapp.R
import com.example.eventapp.User.Fragments.HomeFragment
import com.example.eventapp.User.MainFiles.HomePage.DeleteActivity
import com.example.eventapp.User.MainFiles.HomePage.UserHomePage
import com.example.eventapp.ExtraFiles.UserTypeRegistration
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.getValue

class UserLogin : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginBtn: Button
    lateinit var signup: TextView
    lateinit var forgotPassword: TextView
    lateinit var rememberMeCheckbox: CheckBox
    lateinit var loginInWithGoogle: Button

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        emailEditText = findViewById(R.id.emailLogin)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginBtn = findViewById(R.id.loginButton)
        signup = findViewById(R.id.signup)
        forgotPassword = findViewById(R.id.forgotPasswordTextView)
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckBox)
        loginInWithGoogle = findViewById(R.id.googleLoginButton)

        setupPasswordToggle(passwordEditText)

        forgotPassword.setOnClickListener {
            val intent = Intent(this, UserForgotPassword::class.java)
            startActivity(intent)
        }
        loginViewModel.email.observe(this) {
            if (emailEditText.text.toString() != it) {
                emailEditText.setText(it)
            }
        }

        loginViewModel.password.observe(this) {
            if (passwordEditText.text.toString() != it) {
                passwordEditText.setText(it)
            }
        }
        signup.setOnClickListener {
            val intent = Intent(this, UserTypeRegistration::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            loginViewModel.setEmail(email)
            loginViewModel.setPassword(password)

            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

            if (!email.matches(emailPattern.toRegex())) {
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val request = LoginDataModelRequest(
                email = email,
                password = password
            )



            ApiClient.instance.loginUser(request).enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    if (response.isSuccessful) {
                        val loginData = response.body()
                        val token = loginData?.token

                        if (!token.isNullOrEmpty()) {
                            val sharedPref = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)

                            with(sharedPref.edit()) {
                                putString("authToken", token)
                                putString("email", loginData.attendeedata.email)
                                putString("first_name", loginData.attendeedata.first_name)
                                putString("last_name", loginData.attendeedata.last_name)
                                putString("userType", "attendee")
                                putString("image", loginData.attendeedata.image)
                                putBoolean("isLoggedIn", rememberMeCheckbox.isChecked)
                                apply()
                            }

                            Log.d("eventAppPrefs", "Saved email: ${loginData.attendeedata.email}")
                        }

                        Toast.makeText(
                            applicationContext,
                            loginData?.message ?: "Login successful",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@UserLogin, UserHomePage::class.java))
                        finish()
                    } else {
                        Log.e(
                            "LoginError",
                            "Login failed with code: ${response.code()}, message: ${response.message()}"
                        )
                        Toast.makeText(
                            applicationContext,
                            "Login failed: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    Log.e("LoginError", "Login failed due to network or unexpected error", t)
                    Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })

            val GOOGLE_OAUTH_URL = "https://eventra-server.onrender.com/user/auth/google"

            loginInWithGoogle.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_OAUTH_URL))
                startActivity(browserIntent)
            }
        }
    }
            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email
            val name = account?.displayName

            Toast.makeText(this, "Signed in as $name", Toast.LENGTH_SHORT).show()

            // Optionally send email/token to backend here

            startActivity(Intent(this, UserHomePage::class.java))
            finish()
        } catch (e: ApiException) {
            Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
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