package com.example.eventapp.User.MainFiles

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.DataModels.DashboardResponse
import com.example.eventapp.R
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details)

        val fullName: TextView = findViewById(R.id.tv_full_name_value) // Corrected from et_event_name to tv_full_name_value
        val email: TextView = findViewById(R.id.tv_email_value) // Corrected from et_date to tv_email_value
        val phone: TextView = findViewById(R.id.tv_phone_value) // Corrected from tv_time_label to tv_phone_value
        val dob: TextView = findViewById(R.id.tv_dob_value) // Corrected from emailEdt to tv_dob_value
        val accountId: TextView = findViewById(R.id.tv_account_id_value) // Corrected from roleEdt to tv_account_id_value
        val gender: TextView = findViewById(R.id.tv_gender_value) // Corrected from createdAtEdt to tv_gender_value
        val profileImage: ImageView = findViewById(R.id.tv_update_event_title) // Kept as tv_update_event_title, assuming it's meant to be the profile image
        val backArrow: ImageView = findViewById(R.id.backArrow) // Kept as backArrow

        // Get user token from SharedPreferences
        val userToken = getSharedPreferences("eventAppPrefs", Context.MODE_PRIVATE)
            .getString("authToken", null)

        val sharedPref = getSharedPreferences("eventAppPrefs", AppCompatActivity.MODE_PRIVATE)
        val imageview = sharedPref.getString("image","") ?: ""

        if (imageview.isNotEmpty()) {
            // Convert backslashes to forward slashes and prepend base URL
            val imageUrl = "https://eventra-server.onrender.com/" + imageview.replace("\\", "/")
            Log.d("PROFILE_IMAGE_URL", "Full Image URL: $imageUrl")

            // Set to ImageView using Glide or any other image loader
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.profile_image)
                .error(R.drawable.profile_image)
                .into(profileImage)
        }

        if (userToken != null) {
            ApiClient.instance.getUserDashboard(userToken)
                .enqueue(object : Callback<DashboardResponse> {
                    override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            val data = response.body()!!.data

                            fullName.text = "${data.first_name} ${data.last_name}"
                            email.text = data.email
                            phone.text = data.phone.toString()
                            dob.text = data.dob.substringBefore("T") // Optional: clean ISO date
                            accountId.text = data._id
                            gender.text = data.gender
                        } else {
                            Toast.makeText(this@ProfileDetails, "Failed to load dashboard", Toast.LENGTH_SHORT).show()
                            Log.e("Dashboard", "Code: ${response.code()}, Message: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                        Toast.makeText(this@ProfileDetails, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("Dashboard", "API call failed", t)
                    }
                })
        } else {
            Toast.makeText(this, "User token not found", Toast.LENGTH_SHORT).show()
        }

        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
