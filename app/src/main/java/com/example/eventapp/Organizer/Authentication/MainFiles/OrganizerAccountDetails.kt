package com.example.eventapp.Organizer.Authentication.MainFiles

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.R
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class OrganizerAccountDetails : AppCompatActivity() {
    private lateinit var idEdt: TextView
    private lateinit var companyNameEdt: TextView
    private lateinit var phoneNumberEdt: TextView
    private lateinit var emailEdt: TextView
    private lateinit var roleEdt: TextView
    private lateinit var createdAtEdt: TextView
    private lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_organizer_account_details)

        // Initialize views
        idEdt = findViewById(R.id.et_event_name)
        companyNameEdt = findViewById(R.id.et_date)
        phoneNumberEdt = findViewById(R.id.tv_time_label)
        emailEdt = findViewById(R.id.emailEdt)
        roleEdt = findViewById(R.id.roleEdt)
        createdAtEdt = findViewById(R.id.createdAtEdt)
        backArrow = findViewById(R.id.backArrow)

        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Fetch and display organizer data
        fetchOrganizerDetails()
    }

    private fun fetchOrganizerDetails() {
        val token = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)
            .getString("authToken", "") ?: ""

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.organizerDashboard(token)
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!.data
                    // Set data to views
                    idEdt.text = data._id
                    companyNameEdt.text = data.company_name
                    phoneNumberEdt.text = data.phone.toString()
                    emailEdt.text = data.email
                    roleEdt.text = data.role
                    createdAtEdt.text = formatDate(data.createdAt)
                } else {
                    Toast.makeText(this@OrganizerAccountDetails, "Failed to load details", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@OrganizerAccountDetails, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun formatDate(isoDate: String): String {
        return try {
            val zonedDateTime = ZonedDateTime.parse(isoDate)
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
            zonedDateTime.format(formatter)
        } catch (e: Exception) {
            "Invalid date"
        }
    }
}
