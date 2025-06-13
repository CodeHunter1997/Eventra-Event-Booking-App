package com.example.eventapp.User.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.DataModels.CancelTicketResponse
import com.example.eventapp.DataModels.TestimonialRequest
import com.example.eventapp.DataModels.TestimonialResponse
import com.example.eventapp.DataModels.TicketData
import com.example.eventapp.DataModels.TicketResponse
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.HomePage.Adapters.TicketAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingFragment : Fragment(), TicketAdapter.TicketActionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ticketAdapter: TicketAdapter
    private val allTickets: MutableList<TicketData> = mutableListOf()
    private var userToken: String? = null

    private lateinit var allTab: TextView
    private lateinit var activeTab: TextView
    private lateinit var cancelledTab: TextView

    enum class FilterType { ALL, ACTIVE, CANCELLED }
    private var currentFilter = FilterType.ALL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_booking, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.bookingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        ticketAdapter = TicketAdapter(requireContext(), this, mutableListOf())
        recyclerView.adapter = ticketAdapter

        // Initialize Tabs
        allTab = view.findViewById(R.id.tabAll)
        activeTab = view.findViewById(R.id.tabActive)
        cancelledTab = view.findViewById(R.id.tabCancelled)

        allTab.setOnClickListener { applyFilter(FilterType.ALL) }
        activeTab.setOnClickListener { applyFilter(FilterType.ACTIVE) }
        cancelledTab.setOnClickListener { applyFilter(FilterType.CANCELLED) }

        // Set initial tab selection
        selectFilterTab(FilterType.ALL)

        // Get token from SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("eventAppPrefs", Context.MODE_PRIVATE)
        userToken = sharedPreferences.getString("authToken", null)

        // Load tickets
        loadBookedTickets()

        return view
    }

    private fun loadBookedTickets() {
        if (userToken.isNullOrEmpty()) {
            showToast("User token not found.")
            return
        }

        ApiClient.instance.getUserBookedTickets(userToken!!)
            .enqueue(object : Callback<TicketResponse> {
                override fun onResponse(call: Call<TicketResponse>, response: Response<TicketResponse>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            val tickets = body.data
                            if (!tickets.isNullOrEmpty()) {
                                allTickets.clear()
                                allTickets.addAll(tickets)
                                applyFilter(currentFilter)

                            } else {
                                showToast("No tickets found.")
                                Log.e("BookingFragment", "Ticket data is empty or null.")
                            }
                        } else {
                            showToast("Response body is null.")
                            Log.e("BookingFragment", "Response body is null.")
                        }
                    } else {
                        showToast("Failed to load tickets.")
                        Log.e("BookingFragment", "Load Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<TicketResponse>, t: Throwable) {
                    showToast("Error: ${t.localizedMessage}")
                    Log.e("BookingFragment", "Network failure", t)
                }
            })
    }

    private fun applyFilter(filter: FilterType) {
        currentFilter = filter
        selectFilterTab(filter)

        val filtered = when (filter) {
            FilterType.ALL -> allTickets
            FilterType.ACTIVE -> allTickets.filter { !it.isCancelled }
            FilterType.CANCELLED -> allTickets.filter { it.isCancelled }
        }

        ticketAdapter.updateList(filtered)
    }

    private fun selectFilterTab(filter: FilterType) {
        allTab.isSelected = filter == FilterType.ALL
        activeTab.isSelected = filter == FilterType.ACTIVE
        cancelledTab.isSelected = filter == FilterType.CANCELLED
    }

    override fun onCancelClicked(ticket: TicketData) {
        if (userToken.isNullOrEmpty()) {
            showToast("User token not found.")
            return
        }

        if (ticket.isCancelled) {
            showToast("Ticket already cancelled.")
            return
        }

        ApiClient.instance.cancelTicket(userToken!!, ticket._id)
            .enqueue(object : Callback<CancelTicketResponse> {
                override fun onResponse(call: Call<CancelTicketResponse>, response: Response<CancelTicketResponse>) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        showToast("Ticket cancelled successfully.")
                        loadBookedTickets()
                    } else {
                        showToast("Failed to cancel ticket.")
                        Log.e("BookingFragment", "Cancel Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<CancelTicketResponse>, t: Throwable) {
                    showToast("Error: ${t.localizedMessage}")
                    Log.e("BookingFragment", "Cancel Failure", t)
                }
            })
    }

    override fun onSubmitFeedback(ticket: TicketData, feedback: String, rating: Int) {
        submitFeedbackToServer(ticket, feedback, rating)
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun submitFeedbackToServer(ticket: TicketData, comment: String, rating: Int) {
        if (userToken.isNullOrEmpty()) {
            showToast("User token not found.")
            return
        }

        val eventId = ticket.event_id
        val request = TestimonialRequest(commentdata = comment, rating = rating)

        ApiClient.instance.testimonialDataStore(userToken!!, eventId, request)
            .enqueue(object : Callback<TestimonialResponse> {
                override fun onResponse(
                    call: Call<TestimonialResponse>,
                    response: Response<TestimonialResponse>
                ) {
                    Log.d("BookingFragment", "Response code: ${response.code()}, body: ${response.body()}")

                    if (response.body()?.status == 201) {
                        ticket.feedbackSubmitted = true
                        ticketAdapter.notifyDataSetChanged()
                        showToast("Feedback submitted")
                        Log.d("BookingFragment", "✅ Feedback submitted for event: $eventId")
                    } else {
                        val message = response.body()?.message ?: "Unknown error occurred"
                        showToast(message)
                        Log.e("BookingFragment", " Feedback message: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<TestimonialResponse>, t: Throwable) {
                    showToast("Network error")
                    Log.e("BookingFragment", "❌ Network failure", t)
                }
            })
    }



    }
