package com.example.eventapp.Organizer.Authentication.MainFiles

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eventapp.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class OrganizerDetailsActivity : AppCompatActivity() {


    private lateinit var categoryTextView: TextView
    private lateinit var eventNameTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var artistNameTextView: TextView
    private lateinit var artistRoleTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var venueTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var dateFullTextView: TextView
    private lateinit var timeFullTextView: TextView
    private lateinit var buyTicket: Button
    private lateinit var bgImage: ImageView
    private lateinit var backArrow: ImageView
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer_details)
        // Initialize views
        initializeViews()

        // Get data from intent
        val id = intent.getStringExtra("_id") ?:""
        val category = intent.getStringExtra("category") ?: ""
        val eventName = intent.getStringExtra("event_name") ?: ""
        val priceTextView = intent.getStringExtra("ticketPrice") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val time = intent.getStringExtra("time") ?: ""
        val location = intent.getStringExtra("location") ?: ""
        val venue = intent.getStringExtra("venue") ?: ""
        val city = intent.getStringExtra("city") ?: ""
        val sharedPref = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)
        val authToken = sharedPref.getString("authToken", null)
        val image = intent.getStringExtra("image_url") ?: ""
        val statusSP = intent.getStringExtra("status") ?:""
        backArrow = findViewById(R.id.backArrow)


        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        if (image.isNotEmpty()) {
            val fixedFilename = image.replace(".jpg-", "-").replace(".webp-", "-")
            val fullImageUrl = "https://eventra-server.onrender.com/uploads/images/$fixedFilename"

            Log.d("ConcertImageURL", "Loading image from: $fullImageUrl")

            Glide.with(this)
                .load(fullImageUrl)
                .placeholder(R.drawable.sample_details_page)
                .error(R.drawable.sample_details_page)
                .into(bgImage)
        } else {
            bgImage.setImageResource(R.drawable.sample_details_page)
            Log.w("ConcertImageURL", "No image URL provided for this event.")
        }


//        val artistName = intent.getStringExtra("artist_name") ?: ""
//        val artistRole = intent.getStringExtra("artist_role") ?: ""

        // Update views with data
        updateViews(category, eventName, priceTextView, description, date, time, venue, city, statusSP)/*artistName, artistRole*/

//        buyTicket.setOnClickListener {
//            val intent = Intent(this, BookNowActivity::class.java).apply {
//                putExtra("_id", id)
//                putExtra("category", category)
//                putExtra("event_name", eventName)
//                putExtra("ticketPrice", priceTextView)
//                putExtra("description", description)
//                putExtra("date", date)
//                putExtra("time", time)
//                putExtra("location", location)
//                putExtra("venue", venue)
//                putExtra("city", city)
//                putExtra("image_url", image)
//
//            }
//            startActivity(intent)
//
//        }
    }

    private fun initializeViews() {
        categoryTextView = findViewById(R.id.category)
        eventNameTextView = findViewById(R.id.tv_event_name_label)
        priceTextView = findViewById(R.id.price)
//        artistNameTextView = findViewById(R.id.artistname)
//        artistRoleTextView = findViewById(R.id.artistrole)
        dateTextView = findViewById(R.id.date2)
        timeTextView = findViewById(R.id.date3)
        venueTextView = findViewById(R.id.role) // Corrected ID
        cityTextView = findViewById(R.id.email)
        descriptionTextView = findViewById(R.id.description)
        dateFullTextView = findViewById(R.id.tv_date_label)
        timeFullTextView = findViewById(R.id.tv_time_label)
        buyTicket = findViewById(R.id.buyTickets)
        bgImage = findViewById(R.id.bgImage) // or whatever the correct ID is
        status = findViewById(R.id.status)
    }

    private fun updateViews(
        category: String,
        eventName: String,
        ticketPrice: String,
        description: String,
        date: String,
        time: String,
        venue: String,
        city: String,
        statusSP: String
    ) {
        categoryTextView.text = category
        eventNameTextView.text = eventName
        priceTextView.text = "₹ $ticketPrice /"

        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val dateObj = inputFormat.parse(date) ?: throw IllegalArgumentException("Invalid date format: $date")

            val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(dateObj)

            dateTextView.text = "$formattedDate,"
            dateFullTextView.text = "$formattedDate, "

            val formattedTime = time.replace("am", "AM").replace("pm", "PM")
            timeTextView.text = " $formattedTime"
            timeFullTextView.text = formattedTime

        } catch (e: Exception) {
            Log.e("OrganizerDetailsActivity", "Error parsing date/time: ${e.message}")
            dateTextView.text = "$date,"
            timeTextView.text = " $time"
            dateFullTextView.text = "$date, "
            timeFullTextView.text = time
        }

        venueTextView.text = venue
        cityTextView.text = city
        descriptionTextView.text = description
        status.text = statusSP //
    }




}