    package com.example.eventapp.Organizer.Authentication.MainFiles

    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import android.widget.Button
    import android.widget.EditText
    import android.widget.ImageView
    import android.widget.Toast
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import com.example.eventapp.API_With_Object.ApiClient
    import com.example.eventapp.Auth.DataModels.DeleteAccountRequest
    import com.example.eventapp.Auth.DataModels.DeleteAccountResponse
    import com.example.eventapp.Organizer.Authentication.OrganizerLogin
    import com.example.eventapp.R
    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response

    class OrganizerDeleteAccount : AppCompatActivity() {
        private var authToken: String? = null
        private val TAG = "OrganizerDeleteActivity"
        private lateinit var backArrow: ImageView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_organizer_delete_account)  // Your organizer delete layout

            val emailEditText = findViewById<EditText>(R.id.et_date)  // Adjust id accordingly
            val deleteButton = findViewById<Button>(R.id.deleteBtn)
            backArrow = findViewById(R.id.backArrow)

            val sharedPref = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)
            authToken = sharedPref.getString("authToken", null)

            backArrow.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            deleteButton.setOnClickListener {
                val emailInput = emailEditText.text.toString().trim()

                when {
                    emailInput.isEmpty() -> {
                        Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show()
                    }
                    authToken.isNullOrEmpty() -> {
                        Toast.makeText(this, "Authentication token not found. Please log in again.", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@OrganizerDeleteAccount, OrganizerLogin::class.java))
                        finish()
                    }
                    else -> {
                        deleteAccount(emailInput, authToken!!)
                    }
                }
            }
        }


        private fun deleteAccount(email: String, token: String) {
            val requestBody = DeleteAccountRequest(email)
            Log.d(TAG, "Delete Request Body: $requestBody")
            Log.d(TAG, "Delete Request Header Token: $token")

            ApiClient.instance.organizerDeleteAccount(token, requestBody).enqueue(object : Callback<DeleteAccountResponse> {
                override fun onResponse(
                    call: Call<DeleteAccountResponse>,
                    response: Response<DeleteAccountResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@OrganizerDeleteAccount,
                            response.body()?.message ?: "Account deleted successfully!",
                            Toast.LENGTH_LONG
                        ).show()

                        getSharedPreferences("eventAppPrefs", MODE_PRIVATE).edit().clear().apply()
                        Log.d(TAG, "SharedPreferences cleared after account deletion.")

                        startActivity(Intent(this@OrganizerDeleteAccount, OrganizerLogin::class.java))
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e(TAG, "Error: ${response.code()}, Message: $errorBody")
                        Toast.makeText(
                            this@OrganizerDeleteAccount,
                            "Account deletion failed: ${response.code()} - ${errorBody}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DeleteAccountResponse>, t: Throwable) {
                    Log.e(TAG, "Network/API Call Failure: ${t.localizedMessage}", t)
                    Toast.makeText(this@OrganizerDeleteAccount, "Error deleting account: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            })
        }

    }