    package com.example.eventapp.User.MainFiles.HomePage

    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import android.widget.Button
    import android.widget.ImageView
    import android.widget.RadioButton
    import android.widget.TextView
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import com.bumptech.glide.Glide
    import com.example.eventapp.API_With_Object.ApiClient
    import com.example.eventapp.DataModels.BuyTicketRequest
    import com.example.eventapp.DataModels.BuyTicketResponse
    import com.example.eventapp.R
    import com.example.eventapp.User.MainFiles.TicketBookedActivity
    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response
    import java.text.SimpleDateFormat
    import java.util.Locale

    class BookNowActivity : AppCompatActivity() {

        lateinit var eventImg: ImageView
        lateinit var user_name: TextView
        lateinit var venue: TextView
        lateinit var city: TextView
        lateinit var fulltime: TextView
        lateinit var btn_decrease: Button
        lateinit var ticket_count: TextView
        lateinit var btn_increase: Button
        lateinit var frontStage: RadioButton
        lateinit var middleStage: RadioButton
        lateinit var balconyStage: RadioButton
        lateinit var totalAmount: TextView
        lateinit var payNowBtn: Button
        private lateinit var backArrow: ImageView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_buy_now_details_page)

            user_name = findViewById(R.id.user_name)
            eventImg = findViewById(R.id.eventImg)
            venue = findViewById(R.id.role)
            city = findViewById(R.id.email)
            fulltime = findViewById(R.id.fulltime)
            btn_decrease = findViewById(R.id.btn_decrease)
            ticket_count = findViewById(R.id.ticket_count)
            btn_increase = findViewById(R.id.btn_increase)
            frontStage = findViewById(R.id.frontStage)
            middleStage = findViewById(R.id.middleStage)
            balconyStage = findViewById(R.id.balconyStage)
            totalAmount = findViewById(R.id.totalAmount)
            payNowBtn = findViewById(R.id.payNowBtn)
            backArrow = findViewById(R.id.backArrow)


            val id = intent.getStringExtra("_id") ?: ""
            val eventName = intent.getStringExtra("event_name") ?: ""
            val ticketPrice = intent.getStringExtra("ticketPrice") ?: "0"
            val dateStr = intent.getStringExtra("date") ?: ""
            val time = intent.getStringExtra("time") ?: ""
            val venueLocation = intent.getStringExtra("venue") ?: ""
            val cityLocation = intent.getStringExtra("city") ?: ""
            val imageUrl = intent.getStringExtra("image_url")


            //back arrow

            backArrow.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
    //date formatting
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

            val formattedDate = try {
                val parsedDate = inputFormat.parse(dateStr)
                if (parsedDate != null) outputFormat.format(parsedDate) else dateStr
            } catch (e: Exception) {
                dateStr
            }

            val fullTimeText = "$formattedDate $time"


            if (!imageUrl.isNullOrEmpty()) {
                val fullImageUrl = "https://eventra-server.onrender.com/uploads/images/${imageUrl}"
                Log.d("GlideImageURL", fullImageUrl)

                Glide.with(this) // Use Activity as the context
                    .load(fullImageUrl)
                    .error(R.drawable.sample_details_page)
                    .into(eventImg)
            }

            val sharedPref = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)
            val firstName = sharedPref.getString("first_name", "") ?: ""
            val lastName = sharedPref.getString("last_name", "") ?: ""
            val fullName = "${firstName} ${lastName}"

    // Now set it in the name field:
            user_name.setText(fullName)
            fulltime.setText(fullTimeText)
            venue.setText(venueLocation)
            city.setText(cityLocation)
            fulltime.text = fullTimeText


    //ticket counter
            val ticketPriceStr = ticketPrice.toIntOrNull() ?: 0  // Safely convert String to Int

            var count = 1
            ticket_count.text = count.toString()
            totalAmount.text = "₹${ticketPriceStr * count}"  // This now works as both are Ints

            btn_increase.setOnClickListener {
                count++
                ticket_count.text = count.toString()
                totalAmount.text = "₹${ticketPriceStr * count}"
            }

            btn_decrease.setOnClickListener {
                if (count > 1) {
                    count--
                    ticket_count.text = count.toString()
                    totalAmount.text = "₹${ticketPriceStr * count}"
                }
            }

            //radio button
            val selectedSeatingType = when {
                frontStage.isChecked -> "Front Stage"
                middleStage.isChecked -> "Middle Stage"
                balconyStage.isChecked -> "Balcony"
                else -> "No selection"
            }


            payNowBtn.setOnClickListener {
                val sharedPref = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)
                val token = sharedPref.getString("authToken", "") ?: ""

                val eventId = intent.getStringExtra("_id") ?: ""
                val ticketCount = ticket_count.text.toString().toIntOrNull() ?: 1

                val selectedSeatingType = when {
                    frontStage.isChecked -> "Front Stage"
                    middleStage.isChecked -> "Middle Stage"
                    balconyStage.isChecked -> "Balcony"
                    else -> ""
                }

                if (selectedSeatingType.isEmpty()) {
                    Toast.makeText(this, "Please select a seating type", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (token.isNullOrEmpty()) {
                    Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val buyTicketRequest = BuyTicketRequest(
                    event_id = eventId,
                    noOfTicketsBought = ticketCount
                )

                ApiClient.instance.buyTicket(token, buyTicketRequest)
                    .enqueue(object : Callback<BuyTicketResponse> {
                        override fun onResponse(call: Call<BuyTicketResponse>, response: Response<BuyTicketResponse>) {
                            if (response.isSuccessful) {
                                val data = response.body()?.data
                                if (data != null) {
                                    val intent = Intent(this@BookNowActivity, TicketBookedActivity::class.java).apply {
                                        putExtra("order_id", data._id)
                                        putExtra("event_name", data.event_info.event_name)
                                        putExtra("noOfTicketsBought", data.noOfTicketsBought)
                                        putExtra("venue", data.event_info.event_location.venue)
                                        putExtra("city", data.event_info.event_location.city)
                                        putExtra("event_date", data.event_info.event_date)
                                        putExtra("seating_type", selectedSeatingType)
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                // Log the error details
                                val errorBody = response.errorBody()?.string()
                                Log.e("API_ERROR", "Error response: $errorBody")
                                Toast.makeText(this@BookNowActivity, "Failed to book ticket", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<BuyTicketResponse>, t: Throwable) {
                            Toast.makeText(this@BookNowActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            }

        }


    }