        package com.example.eventapp.Organizer.Authentication.MainFiles

        import android.content.Intent
        import android.os.Bundle
        import android.widget.Button
        import android.widget.EditText
        import android.widget.ImageView
        import android.widget.Toast
        import androidx.appcompat.app.AppCompatActivity
        import com.example.eventapp.API_With_Object.ApiClient

        import com.example.eventapp.DataModels.EventUpdateResponse
        import com.example.eventapp.DataModels.UpdateEventRequest
        import com.example.eventapp.R
        import retrofit2.Call
        import retrofit2.Callback
        import retrofit2.Response
        import java.time.ZonedDateTime
        import java.time.format.DateTimeFormatter
        import java.util.Locale

        class OrganizerUpdateEventActivity : AppCompatActivity() {
            lateinit var eventNameEdt: EditText
            lateinit var dateEdt: EditText
            lateinit var timeEdt: EditText
            lateinit var cityEdt: EditText
            lateinit var venueEdt: EditText
            lateinit var totalSeatsEdt: EditText
            lateinit var totalPriceEdt: EditText
            lateinit var typeEdt: EditText
            lateinit var artistNameEdt: EditText
            lateinit var artistRoleEdt: EditText
            lateinit var categoryEdt: EditText
            lateinit var descriptionEdt: EditText
            lateinit var updateBtn: Button
            lateinit var backArrow: ImageView

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_update_profile)

                eventNameEdt = findViewById(R.id.et_event_name);
                dateEdt = findViewById(R.id.et_date);
                timeEdt = findViewById(R.id.et_time); // Corrected from tv_time_label to et_time
                cityEdt = findViewById(R.id.et_city); // Corrected from emailEdt to et_city
                venueEdt = findViewById(R.id.et_venue); // Corrected from roleEdt to et_venue
                totalSeatsEdt = findViewById(R.id.et_total_seats); // Corrected from createdAtEdt to et_total_seats
                totalPriceEdt = findViewById(R.id.et_ticket_price); // Corrected from totalPriceedt to et_ticket_price
                typeEdt = findViewById(R.id.et_event_type); // Corrected from typeEdt to et_event_type
                artistNameEdt = findViewById(R.id.et_artist_name); // Corrected from artistNameEdt to et_artist_name
                artistRoleEdt = findViewById(R.id.et_artist_role); // Corrected from artistRoleEdt to et_artist_role
                categoryEdt = findViewById(R.id.et_category); // Corrected from categoryEdt to et_category
                descriptionEdt = findViewById(R.id.et_description); // Corrected from descriptionEdt to et_description
                updateBtn = findViewById(R.id.btn_update_event); // Corrected from create to btn_update_event
                backArrow = findViewById(R.id.iv_back_arrow); // Corrected from backArrow to iv_back_arrow

                backArrow.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }


                typeEdt.setOnClickListener {
                    val types = arrayOf("online", "offline")
                    val builder = android.app.AlertDialog.Builder(this)
                    builder.setTitle("Select Event Type")
                    builder.setItems(types) { _, which ->
                        typeEdt.setText(types[which])
                    }
                    builder.show()
                }

                // Get intent extras
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
                val totalSeats = intent.getStringExtra("total_seats") ?:""
//                Toast.makeText(this, "totalSeats from intent: '$totalSeats'", Toast.LENGTH_LONG).show()

                val type = intent.getStringExtra("type") ?:""

//false error
                val formattedDate = try {
                    val zonedDateTime = ZonedDateTime.parse(dateRaw)
                    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())
                    zonedDateTime.format(formatter)
                } catch (e: Exception) {
                    // Fallback to raw string if parsing fails
                    dateRaw
                }

                eventNameEdt.setText(eventName.toString())
                dateEdt.setText(formattedDate)
                timeEdt.setText(timeRaw.toString())
                cityEdt.setText(city.toString())
                venueEdt.setText(venue.toString())
                totalSeatsEdt.setText(totalSeats.toString())

                totalPriceEdt.setText(ticketPrice.toString())
                typeEdt.setText(type.toString())
                artistNameEdt.setText(artistName.toString())
                artistRoleEdt.setText(artistRole.toString())
                categoryEdt.setText(category.toString())
                descriptionEdt.setText(description.toString())

                updateBtn.setOnClickListener {
                    val token = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)
                        .getString("authToken", "") ?: ""

                    val updatedEvent = UpdateEventRequest(
                        event_name = eventNameEdt.text.toString(),
                        description = descriptionEdt.text.toString(),
                        date = dateEdt.text.toString(),
                        time = timeEdt.text.toString(),
                        location = UpdateEventRequest.Location(
                            city = cityEdt.text.toString(),
                            venue = venueEdt.text.toString()
                        ),
                        total_seats = totalSeatsEdt.text.toString().toIntOrNull() ?: 0,
                        ticketPrice = totalPriceEdt.text.toString().toIntOrNull() ?: 0,
                        type = typeEdt.text.toString(),
                        artistname = artistNameEdt.text.toString(),
                        artistrole = artistRoleEdt.text.toString(),
                        category = categoryEdt.text.toString(),
                        validAge = false
                    )

                    val service = ApiClient.instance

                    service.updateEvent(token, id, updatedEvent).enqueue(object : Callback<EventUpdateResponse> {
                        override fun onResponse(call: Call<EventUpdateResponse>, response: Response<EventUpdateResponse>) {
                            if (response.isSuccessful && response.body() != null) {
                                Toast.makeText(this@OrganizerUpdateEventActivity, "Event updated successfully!", Toast.LENGTH_SHORT).show()

                                val resultIntent = Intent().apply {
                                    putExtra("_id", id)
                                    putExtra("category", updatedEvent.category)
                                    putExtra("event_name", updatedEvent.event_name)
                                    putExtra("ticketPrice", updatedEvent.ticketPrice.toString())
                                    putExtra("description", updatedEvent.description)
                                    putExtra("date", updatedEvent.date)
                                    putExtra("time", updatedEvent.time)
                                    putExtra("venue", updatedEvent.location.venue)
                                    putExtra("city", updatedEvent.location.city)
                                    putExtra("artistname", updatedEvent.artistname)
                                    putExtra("artistrole", updatedEvent.artistrole)
                                    putExtra("type", updatedEvent.type)
                                    putExtra("total_seats", updatedEvent.total_seats.toString())
                                }
                                setResult(RESULT_OK, resultIntent)
                                finish()
                            } else {
                                Toast.makeText(this@OrganizerUpdateEventActivity, "Failed to update event", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<EventUpdateResponse>, t: Throwable) {
                            Toast.makeText(this@OrganizerUpdateEventActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }


            }
        }