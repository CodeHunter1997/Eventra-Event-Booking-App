package com.example.eventapp.User.MainFiles.HomePage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.DataModels.TestimonialDataShowResponse
import com.example.eventapp.DataModels.TestimonialDataSuccessfulResponse
import com.example.eventapp.DataModels.UpcomingEvents.TestimonialData
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.HomePage.Adapters.BuyNowPageTestAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

//buy now page for all
class BuyNowActivity : AppCompatActivity() {

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
    private lateinit var customerReviews: RecyclerView
    private lateinit var adapter: BuyNowPageTestAdapter
    private val testimonialList = mutableListOf<TestimonialDataSuccessfulResponse>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

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
        backArrow = findViewById(R.id.backArrow)


        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        Log.d("ReceivedImageURL", "Image URL: $image")

        val fullImageUrl = "https://eventra-server.onrender.com/uploads/images/${image}"
        Log.d("GlideImageURL", fullImageUrl)
        Glide.with(this)
            .load(fullImageUrl)
            .placeholder(R.drawable.sample_details_page)
            .error(R.drawable.sample_details_page)
            .into(bgImage)

        //testimonials
        customerReviews.layoutManager = LinearLayoutManager(this)
        adapter = BuyNowPageTestAdapter(testimonialList)
        customerReviews.adapter = adapter

        val token = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)
            .getString("authToken", "") ?: ""
        if (id.isNotEmpty() && token.isNotEmpty()) {
            fetchTestimonialData(id, token)
        } else {
            Toast.makeText(this, "Missing event ID or token", Toast.LENGTH_SHORT).show()
        }


        // Update views with data
        updateViews(category, eventName, priceTextView, description, date, time, venue, city,)/*artistName, artistRole*/

        buyTicket.setOnClickListener {
            val intent = Intent(this, BookNowActivity::class.java).apply {
                putExtra("_id", id)
                putExtra("category", category)
                putExtra("event_name", eventName)
                putExtra("ticketPrice", priceTextView)
                putExtra("description", description)
                putExtra("date", date)
                putExtra("time", time)
                putExtra("location", location)
                putExtra("venue", venue)
                putExtra("city", city)
                putExtra("image_url", image)

            }
            startActivity(intent)

        }
    }

    private fun fetchTestimonialData(eventId: String, token: String) {
        ApiClient.instance.getTestimonialDataShow(eventId, token)
            .enqueue(object : Callback<TestimonialDataShowResponse> {
                override fun onResponse(
                    call: Call<TestimonialDataShowResponse>,
                    response: Response<TestimonialDataShowResponse>
                ) {
                    if (response.isSuccessful && response.body()?.data != null) {
                        val responseData = response.body()!!.data!!
                        testimonialList.clear()
                        testimonialList.addAll(responseData)
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.e("TESTIMONIAL_API", "Error: ${response.body()?.error}")
                        Toast.makeText(this@BuyNowActivity, "No testimonial found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<TestimonialDataShowResponse>, t: Throwable) {
                    Log.e("TESTIMONIAL_API", "Network failure", t)
                    Toast.makeText(this@BuyNowActivity, "Failed to load testimonial", Toast.LENGTH_SHORT).show()
                }
            })
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
        customerReviews = findViewById(R.id.customerReviews)
    }

    private fun updateViews(
        category: String,
        eventName: String,
        ticketPrice: String,
        description: String,
        date: String,
        time: String,
        venue: String, // Added venue and city
        city: String,
//        artistName: String,
//        artistRole: String
    ) {
        categoryTextView.text = category
        eventNameTextView.text = eventName
        priceTextView.text = "₹ $ticketPrice /"
//        artistNameTextView.text = artistName
//        artistRoleTextView.text = artistRole.uppercase()

        // Format the date and time
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Explicitly set UTC timezone for parsing
            val dateObj = inputFormat.parse(date) ?: throw IllegalArgumentException("Invalid date format: $date") //Handle null

            val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(dateObj)

            dateTextView.text = "$formattedDate,"
            dateFullTextView.text = "$formattedDate, "


            val formattedTime = time.replace("am", "AM").replace("pm", "PM")
            timeTextView.text = " $formattedTime"
            timeFullTextView.text = formattedTime

        } catch (e: Exception) {
            Log.e("Test_Activity", "Error parsing date/time: ${e.message}, date: $date, time: $time") // Log the error
            dateTextView.text = "$date,"
            timeTextView.text = " $time"
            dateFullTextView.text = "$date, "
            timeFullTextView.text = time
        }

        venueTextView.text = venue // Corrected variable name
        cityTextView.text = city
        descriptionTextView.text = description
    }


}