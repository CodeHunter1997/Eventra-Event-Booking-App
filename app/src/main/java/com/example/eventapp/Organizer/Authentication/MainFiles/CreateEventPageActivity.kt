package com.example.eventapp.Organizer.Authentication.MainFiles

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.DataModels.CreateEventResponse
import com.example.eventapp.DataModels.EventCreateRequest
import com.example.eventapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateEventPageActivity : AppCompatActivity() {

    private lateinit var eventName: EditText
    private lateinit var date: EditText
    private lateinit var time: EditText
    private lateinit var city: EditText
    private lateinit var venue: EditText
    private lateinit var totalSeats: EditText
    private lateinit var totalPrice: EditText
    private lateinit var type: EditText
    private lateinit var artistName: EditText
    private lateinit var artistRole: EditText
    private lateinit var category: EditText
    private lateinit var description: EditText
    private lateinit var createBtn: Button
    private lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_page)

        eventName = findViewById(R.id.et_event_name)
        date = findViewById(R.id.et_date)
        time = findViewById(R.id.tv_time_label)
        city = findViewById(R.id.emailEdt)
        venue = findViewById(R.id.roleEdt)
        totalSeats = findViewById(R.id.createdAtEdt)
        totalPrice = findViewById(R.id.ticketPriceEdt)
        type = findViewById(R.id.typeEdt)
        artistName = findViewById(R.id.artistNameEdt)
        artistRole = findViewById(R.id.artistRoleEdt)
        category = findViewById(R.id.categoryEdt)
        description = findViewById(R.id.descriptionEdt)
        createBtn = findViewById(R.id.createEvent)
        backArrow = findViewById(R.id.backArrow)

        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        type.setOnClickListener {
            val types = arrayOf("online", "offline")
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Select Event Type")
            builder.setItems(types) { _, which ->
                type.setText(types[which])
            }
            builder.show()
        }

        createBtn.setOnClickListener {
            val token = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)
                .getString("authToken", "") ?: ""

            if (token.isBlank()) {
                Toast.makeText(this, "Authentication token missing!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (eventName.text.isBlank() || date.text.isBlank() || time.text.isBlank() || city.text.isBlank()
                || venue.text.isBlank() || totalSeats.text.isBlank() || totalPrice.text.isBlank()
                || type.text.isBlank() || artistName.text.isBlank() || artistRole.text.isBlank()
                || category.text.isBlank() || description.text.isBlank()
            ) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = EventCreateRequest(
                event_name = eventName.text.toString(),
                description = description.text.toString(),
                date = date.text.toString(),
                time = time.text.toString(),
                city = city.text.toString(),
                venue = venue.text.toString(),
                total_seats = totalSeats.text.toString().toInt(),
                ticketPrice = totalPrice.text.toString().toInt(),
                type = type.text.toString().trim().lowercase(),
                artistname = artistName.text.toString(),
                artistrole = artistRole.text.toString(),
                category = category.text.toString()
            )

            Log.d("CreateEventData", "Token: $token")
            Log.d("CreateEventData", "Event Name: ${eventName.text}")
            Log.d("CreateEventData", "Date: ${date.text}")
            Log.d("CreateEventData", "Time: ${time.text}")
            Log.d("CreateEventData", "City: ${city.text}")
            Log.d("CreateEventData", "Venue: ${venue.text}")
            Log.d("CreateEventData", "Total Seats: ${totalSeats.text}")
            Log.d("CreateEventData", "Total Price: ${totalPrice.text}")
            Log.d("CreateEventData", "Type: ${type.text}")
            Log.d("CreateEventData", "Artist Name: ${artistName.text}")
            Log.d("CreateEventData", "Artist Role: ${artistRole.text}")
            Log.d("CreateEventData", "Category: ${category.text}")
            Log.d("CreateEventData", "Description: ${description.text}")

            ApiClient.instance.createEvent(
                token = token,
                eventName = request.event_name,
                description = request.description,
                date = request.date,
                time = request.time,
                city = request.city,
                venue = request.venue,
                totalSeats = request.total_seats,
                ticketPrice = request.ticketPrice,
                type = request.type,
                artistName = request.artistname,
                artistRole = request.artistrole,
                category = request.category
            ).enqueue(object : Callback<CreateEventResponse> {

                override fun onResponse(call: Call<CreateEventResponse>, response: Response<CreateEventResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CreateEventPageActivity, "Event created successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "No error body"
                        Log.e("CreateEvent", "Failed with code: ${response.code()} \nError body: $errorBody")
                        Toast.makeText(this@CreateEventPageActivity, "Failed: ${response.code()} - $errorBody", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<CreateEventResponse>, t: Throwable) {
                    Toast.makeText(this@CreateEventPageActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("CreateEvent", "Network failure", t)
                }
            })
        }
    }
}
