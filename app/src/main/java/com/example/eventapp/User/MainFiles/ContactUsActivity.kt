package com.example.eventapp.User.MainFiles

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.DataModels.UpcomingEvents.ContactUsRequest
import com.example.eventapp.DataModels.UpcomingEvents.ContactUsResponse
import com.example.eventapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var serviceTypeEditText: EditText
    private lateinit var messageEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        // Initialize all views
        val backArrow = findViewById<ImageView>(R.id.backArrow)
        firstNameEditText = findViewById(R.id.firstName)
        lastNameEditText = findViewById(R.id.lastName)
        emailEditText = findViewById(R.id.et_date)
        phoneEditText = findViewById(R.id.tv_time_label)
        serviceTypeEditText = findViewById(R.id.serviceType)
        messageEditText = findViewById(R.id.message)
        submitButton = findViewById(R.id.submit)

        // Handle back arrow
        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        submitButton.setOnClickListener {
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val phoneText = phoneEditText.text.toString().trim()
        val serviceType = serviceTypeEditText.text.toString().trim()
        val message = messageEditText.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneText.isEmpty() ||
            serviceType.isEmpty() || message.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        if (!isValidPhoneNumber(phoneText)) {
            Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        val phone = phoneText.toLong()

        val contactUsRequest = ContactUsRequest(
            first_name = firstName,
            last_name = lastName,
            email = email,
            phone = phone,
            servicetype = serviceType,
            message = message
        )

            ApiClient.instance.submitContactUs(contactUsRequest)
                .enqueue(object : Callback<ContactUsResponse> {
                    override fun onResponse(
                        call: Call<ContactUsResponse>,
                        response: Response<ContactUsResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(
                                this@ContactUsActivity,
                                "Thank you for contacting us! Our team will get in touch with you shortly",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()

                        } else {
                            Toast.makeText(
                                this@ContactUsActivity,
                                "Submission failed: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ContactUsResponse>, t: Throwable) {
                        Toast.makeText(
                            this@ContactUsActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }


    // Validates that phone number has exactly 10 digits
    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.matches(Regex("^\\d{10}$"))
    }

    // Validates email with a standard pattern
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }



}
