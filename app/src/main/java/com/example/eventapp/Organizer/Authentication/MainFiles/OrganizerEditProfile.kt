package com.example.eventapp.Organizer.Authentication.MainFiles

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.DataModels.OrganizerProfileUpdateRequest
import com.example.eventapp.R
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class OrganizerEditProfile : AppCompatActivity() {

    private lateinit var companyNameEdt: TextView
    private lateinit var phoneEdt: TextView
    private lateinit var emailEdt: TextView
    private lateinit var roleEdt: TextView
    private lateinit var createdAtEdt: TextView
    private lateinit var profileImage: ShapeableImageView
    private lateinit var backArrow: ImageView
    private lateinit var updateBtn: AppCompatButton

    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_organizer_edit_profile)

        companyNameEdt = findViewById(R.id.companyNameEdt)
        phoneEdt = findViewById(R.id.tv_time_label)
        emailEdt = findViewById(R.id.emailEdt)
        roleEdt = findViewById(R.id.roleEdt)
        createdAtEdt = findViewById(R.id.createdAtEdt)
        profileImage = findViewById(R.id.tv_update_event_title)
        backArrow = findViewById(R.id.backArrow)
        updateBtn = findViewById(R.id.updateBtn)


        val imageUrl = intent.getStringExtra("profileImageUrl")
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.sample_details_page)
                .into(profileImage)
        }
        token = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)
            .getString("authToken", "") ?: ""

        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        fetchOrganizerData()

        updateBtn.setOnClickListener {
            updateOrganizerProfile()
            finish()
        }
    }

    private fun fetchOrganizerData() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.organizerDashboard(token)
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!.data
                    if (data != null) {
                        companyNameEdt.text = data.company_name
                        phoneEdt.text = data.phone.toString()
                        emailEdt.text = data.email
                        roleEdt.text = data.role
                        createdAtEdt.text = formatDate(data.createdAt)
                    } else {
                        Toast.makeText(this@OrganizerEditProfile, "No data found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@OrganizerEditProfile, "Failed to fetch profile", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@OrganizerEditProfile, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateOrganizerProfile() {
        val companyName = companyNameEdt.text.toString().trim()
        val email = emailEdt.text.toString().trim()
        val phone = phoneEdt.text.toString().trim().toLongOrNull() ?: 0L

        val request = OrganizerProfileUpdateRequest(
            company_info = "N/A",
            company_name = companyName,
            email = email,
            phone = phone,
            image = ""  
        )

        lifecycleScope.launch {
            try {
                val call = ApiClient.instance.organizerProfileUpdate(token, request)
                val response = withContext(Dispatchers.IO) { call.awaitResponse() }

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@OrganizerEditProfile,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this@OrganizerEditProfile, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("ProfileUpdateError", "Exception: ${e.message}", e)

                Toast.makeText(this@OrganizerEditProfile, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
