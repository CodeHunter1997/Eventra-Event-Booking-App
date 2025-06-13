package com.example.eventapp.User.MainFiles

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.R
import java.text.SimpleDateFormat
import java.util.Locale

class TicketBookedActivity : AppCompatActivity() {
    lateinit var orderIdTextView: TextView
    lateinit var eventNameTextView: TextView
    lateinit var venueTextView: TextView
    lateinit var cityTextView: TextView
    lateinit var eventDateTextView: TextView
    lateinit var seatingTypeTextView: TextView
    lateinit var timeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_booked)

        orderIdTextView = findViewById(R.id.orderIdTextView)
        eventNameTextView = findViewById(R.id.eventNameTextView)
        venueTextView = findViewById(R.id.venueTextView)
        cityTextView = findViewById(R.id.cityTextView)
        eventDateTextView = findViewById(R.id.eventDateTextView)
        seatingTypeTextView = findViewById(R.id.seatingTypeTextView)
        timeTextView = findViewById(R.id.timeTextView)

        val orderId = intent.getStringExtra("order_id") ?: ""
        val eventName = intent.getStringExtra("event_name") ?: ""
        val venue = intent.getStringExtra("venue") ?: ""
        val city = intent.getStringExtra("city") ?: ""
        val eventDate = intent.getStringExtra("event_date") ?: ""
        val seatingType = intent.getStringExtra("seating_type") ?: "Not selected"

// Optionally format the date to a readable format
        val formattedDate = try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(eventDate)
            outputFormat.format(date ?: "")
        } catch (e: Exception) {
            eventDate // fallback to raw if formatting fails
        }

// Set values to views
        orderIdTextView.text = "$orderId"
        eventNameTextView.text = "$eventName"
        venueTextView.text = "$venue"
        cityTextView.text = "$city"
        eventDateTextView.text = "$formattedDate"
        seatingTypeTextView.text = "$seatingType"



    }
}