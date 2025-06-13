package com.example.eventapp.Organizer.Authentication.MainFiles

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.DataModels.DeleteEventResponse
import com.example.eventapp.R
import com.google.android.gms.maps.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ConcertDetailsPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conert_details_page)

        // Initialize views
        val bgImage = findViewById<ImageView>(R.id.bgImage)
        val backArrow = findViewById<ImageView>(R.id.backArrow)
        val categoryView = findViewById<TextView>(R.id.category)
        val eventNameView = findViewById<TextView>(R.id.tv_event_name_label)
        val priceView = findViewById<TextView>(R.id.price)
        val liveStatus = findViewById<TextView>(R.id.status)
        val dateView = findViewById<TextView>(R.id.date2)
        val timeView = findViewById<TextView>(R.id.date3)
        val venueView = findViewById<TextView>(R.id.role)
//        val cityView = findViewById<TextView>(R.id.city)
//        val mapView = findViewById<MapView>(R.id.mapView)
        val descriptionView = findViewById<TextView>(R.id.description)
        val editBtn = findViewById<AppCompatButton>(R.id.editBtn)
        val deleteBtn = findViewById<AppCompatButton>(R.id.deleteBtn)
        val dateTopBar = findViewById<TextView>(R.id.tv_date_label)
        val timeTopBar = findViewById<TextView>(R.id.tv_time_label)

        // Get intent data
        val id = intent.getStringExtra("_id") ?: ""
        val category = intent.getStringExtra("category") ?: ""
        val eventName = intent.getStringExtra("event_name") ?: ""
        val ticketPrice = intent.getStringExtra("ticketPrice") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val dateRaw = intent.getStringExtra("date") ?: ""
        val timeRaw = intent.getStringExtra("time") ?: ""
        val venue = intent.getStringExtra("venue") ?: ""
        val city = intent.getStringExtra("city") ?: ""
        val imageUrl = intent.getStringExtra("image_url") ?: ""
        val statusStr = intent.getStringExtra("status") ?: ""
        val isDeleted = intent.getBooleanExtra("isDeleted", false)
        val artistName = intent.getStringExtra("artistname") ?: ""
        val artistRole = intent.getStringExtra("artistrole") ?: ""
        val totalSeats = intent.getStringExtra("total_seats")?:""

        editBtn.setOnClickListener {
            val intent = Intent(this, OrganizerUpdateEventActivity::class.java).apply {
                putExtra("_id", id)
                putExtra("category", category)
                putExtra("event_name", eventName)
                putExtra("ticketPrice", ticketPrice)
                putExtra("total_seats", totalSeats)
                putExtra("description", description)
                putExtra("date", dateRaw)
                putExtra("time", timeRaw)
                putExtra("venue", venue)
                putExtra("city", city)
                putExtra("image_url", imageUrl)
                putExtra("status", statusStr)
                putExtra("isDeleted", isDeleted)
                putExtra("artistname", artistName)
                putExtra("artistrole", artistRole)

            }
            startActivity(intent)
            setResult(Activity.RESULT_OK)
            finish()
        }

        val fullImageUrl = "https://eventra-server.onrender.com/uploads/images/${imageUrl}"
        Log.d("GlideImageURL", fullImageUrl)
        Glide.with(this)
            .load(fullImageUrl)
            .placeholder(R.drawable.sample_details_page)
            .error(R.drawable.sample_details_page)
            .into(bgImage)

        // Set text data
        categoryView.text = category
        eventNameView.text = eventName
        priceView.text = "₹$ticketPrice"
        liveStatus.text = statusStr
        venueView.text = "$venue, $city"
        descriptionView.text = description

        if (isDeleted) {
            editBtn.visibility = View.GONE
            deleteBtn.visibility = View.GONE
            liveStatus.visibility = View.GONE
        }
        // Format date and time
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val dateObj = inputFormat.parse(dateRaw)

            val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
            val formattedDate = dateObj?.let { dateFormat.format(it) } ?: dateRaw

            dateView.text = formattedDate
            dateTopBar.text = formattedDate
        } catch (e: Exception) {
            Log.e("ConertDetailsPage", "Date parsing error: ${e.message}")
            dateView.text = dateRaw
            dateTopBar.text = dateRaw
        }

        // Format time
        val formattedTime = timeRaw.replace("am", "AM").replace("pm", "PM")
        timeView.text = formattedTime
        timeTopBar.text = formattedTime

        // Back arrow click
        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Load shared preferences (if needed later)
        val sharedPref = getSharedPreferences("eventAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPref.getString("authToken", "") ?: ""

        deleteBtn.setOnClickListener {
            if (id.isNotEmpty()) {
                deleteEvent(token, id)
            } else {
                Toast.makeText(this, "Invalid Event ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun deleteEvent(token: String, eventId: String) {
        val call = ApiClient.instance.deleteEvent(token, eventId)
        call.enqueue(object : Callback<DeleteEventResponse> {
            override fun onResponse(
                call: Call<DeleteEventResponse>,
                response: Response<DeleteEventResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@ConcertDetailsPage, "Event marked as cancelled", Toast.LENGTH_SHORT).show()
                    finish() // Optionally close the details page
                } else {
                    Toast.makeText(this@ConcertDetailsPage, "Failed to delete event", Toast.LENGTH_SHORT).show()
                    Log.e("DELETE", "Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DeleteEventResponse>, t: Throwable) {
                Toast.makeText(this@ConcertDetailsPage, "Network error", Toast.LENGTH_SHORT).show()
                Log.e("DELETE", "Failure: ${t.localizedMessage}")
            }
        })
    }

}
