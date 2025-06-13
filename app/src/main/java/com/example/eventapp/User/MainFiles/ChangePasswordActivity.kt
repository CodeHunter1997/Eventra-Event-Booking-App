package com.example.eventapp.User.MainFiles

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.API_With_Object.ApiInterface
import com.example.eventapp.Auth.DataModels.ChangePasswordRequest
import com.example.eventapp.Auth.DataModels.ChangePasswordResponse
import com.example.eventapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var saveChanges: Button
    private lateinit var apiService: ApiInterface
    private lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        backArrow = findViewById(R.id.backArrow)

        currentPasswordEditText = findViewById(R.id.currentPassword)
        newPasswordEditText = findViewById(R.id.newPassword)
        confirmPasswordEditText = findViewById(R.id.confirmPassword)
        saveChanges = findViewById(R.id.saveChanges)
        apiService = ApiClient.instance


        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        saveChanges.setOnClickListener {
            handleChangePassword()
        }
    }
    private fun handleChangePassword() {
        val current = currentPasswordEditText.text.toString()
        val new = newPasswordEditText.text.toString()
        val confirm = confirmPasswordEditText.text.toString()

        if (current.isBlank() || new.isBlank() || confirm.isBlank()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (new != confirm) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPref = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("authToken", null)

        if (token != null) {
            val call = ApiClient.instance.changePassword(
                token = token,
                request = ChangePasswordRequest(
                    current_password = current,
                    password = new,
                    confirm_password = confirm
                )
            )

            call.enqueue(object : Callback<ChangePasswordResponse> {
                override fun onResponse(
                    call: Call<ChangePasswordResponse>,
                    response: Response<ChangePasswordResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ChangePasswordActivity,
                            response.body()?.message ?: "Password changed successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@ChangePasswordActivity,
                            "Failed: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        "Error: ${t.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(this, "Token not found. Please login again.", Toast.LENGTH_SHORT).show()
        }
    }
}