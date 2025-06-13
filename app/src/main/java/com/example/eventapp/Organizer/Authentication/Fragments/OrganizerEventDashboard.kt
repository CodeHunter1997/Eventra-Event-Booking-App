package com.example.eventapp.Organizer.Authentication.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.DataModels.EventData2
import com.example.eventapp.Organizer.Authentication.MainFiles.Adapters.MyEventAdapter
import com.example.eventapp.Organizer.Authentication.MainFiles.ConcertDetailsPage
import com.example.eventapp.Organizer.Authentication.MainFiles.CreateEventPageActivity
import com.example.eventapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrganizerEventDashboard : Fragment() {

    private lateinit var allEventCount: TextView
    private lateinit var activeCount: TextView
    private lateinit var cancelCount: TextView
    private lateinit var createEventBtn: ViewGroup
    private lateinit var allLayout: ViewGroup
    private lateinit var activeLayout: ViewGroup
    private lateinit var cancelLayout: ViewGroup
    private lateinit var recyclerView: RecyclerView
    private var allEvents: List<EventData2> = emptyList()
    private lateinit var myEventAdapter: MyEventAdapter

    private lateinit var detailLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_organizer_event_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allLayout = view.findViewById(R.id.allLayout)
        activeLayout = view.findViewById(R.id.activeLayout)
        cancelLayout = view.findViewById(R.id.cancelLayout)
        allEventCount = view.findViewById(R.id.allEventCount)
        activeCount = view.findViewById(R.id.activeCount)
        cancelCount = view.findViewById(R.id.cancelCount)
        createEventBtn = view.findViewById(R.id.createEvent)
        recyclerView = view.findViewById(R.id.myEventRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Register detailLauncher and reload events on return
        detailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                loadEventList()
            }
        }

        createEventBtn.setOnClickListener {
            val intent = Intent(requireContext(), CreateEventPageActivity::class.java)
            detailLauncher.launch(intent)
        }

        myEventAdapter = MyEventAdapter(mutableListOf()) { selectedEvent ->
            val intent = Intent(requireContext(), ConcertDetailsPage::class.java)
            // put extras...
            detailLauncher.launch(intent)
        }
        recyclerView.adapter = myEventAdapter
        // Initial load
        loadEventList()

        // Filter click listeners
        allLayout.setOnClickListener {
            setRecyclerViewAdapter(allEvents)
        }

        activeLayout.setOnClickListener {
            val filtered = allEvents.filter { !it.isDeleted }
            setRecyclerViewAdapter(filtered)
        }

        cancelLayout.setOnClickListener {
            val filtered = allEvents.filter { it.isDeleted }
            setRecyclerViewAdapter(filtered)
        }
    }
    override fun onResume() {
        super.onResume()
        loadEventList()
    }


    private fun loadEventList() {
        val sharedPref = requireActivity().getSharedPreferences("eventAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPref.getString("authToken", "") ?: ""

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.instance.organizerEventList(token)
                }

                if (response.isSuccessful && response.body() != null) {
                    allEvents = response.body()!!.data

                    val allCount = allEvents.size
                    val cancelledCount = allEvents.count { it.isDeleted }
                    val activeCountValue = allEvents.count { !it.isDeleted }
                    myEventAdapter.updateList(allEvents)

                    allEventCount.text = allCount.toString()
                    activeCount.text = activeCountValue.toString()
                    cancelCount.text = cancelledCount.toString()

                    setRecyclerViewAdapter(allEvents)
                } else {
                    Log.e("API", "Failed: ${response.code()} - ${response.message()}")
                    showEmptyState()
                }

            } catch (e: Exception) {
                Log.e("API", "Exception: ${e.localizedMessage}")
                showEmptyState()
            }
        }


    }

    private fun setRecyclerViewAdapter(events: List<EventData2>) {
        recyclerView.adapter = MyEventAdapter(events) { selectedEvent ->
            val intent = Intent(requireContext(), ConcertDetailsPage::class.java).apply {
                putExtra("_id", selectedEvent._id)
                putExtra("category", selectedEvent.category)
                putExtra("event_name", selectedEvent.event_name)
                putExtra("ticketPrice", selectedEvent.ticketPrice.toString())
                putExtra("description", selectedEvent.description)
                putExtra("date", selectedEvent.date)
                putExtra("time", selectedEvent.time)
                putExtra("venue", selectedEvent.location.venue)
                putExtra("city", selectedEvent.location.city)
                putExtra("image_url", selectedEvent.images.firstOrNull())
                putExtra("status", selectedEvent.status)
                putExtra("isDeleted", selectedEvent.isDeleted)
                putExtra("artistname", selectedEvent.artistname)
                putExtra("artistrole", selectedEvent.artistrole)
                putExtra("total_seats", selectedEvent.total_seats)
                putExtra("type", selectedEvent.type)
            }
            detailLauncher.launch(intent)
        }
    }


    private fun showEmptyState() {
        allEventCount.text = "0"
        activeCount.text = "0"
        cancelCount.text = "0"
        recyclerView.adapter = MyEventAdapter(emptyList()) {}
    }
}
