package com.example.eventapp.User.MainFiles

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.HomePage.Adapters.EventListAdapter
import com.example.eventapp.User.MainFiles.HomePage.ViewModel.EventViewModel2

class CategoryEventListActivity : AppCompatActivity() {

    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var eventAdapter: EventListAdapter
    private lateinit var eventViewModel: EventViewModel2
    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_event_list)

        selectedCategory = intent.getStringExtra("category")

        supportActionBar?.title = selectedCategory ?: "Events"

        eventRecyclerView = findViewById(R.id.eventRecyclerView)
        eventAdapter = EventListAdapter(emptyList())
        eventRecyclerView.layoutManager = LinearLayoutManager(this)
        eventRecyclerView.adapter = eventAdapter

        eventViewModel = ViewModelProvider(this)[EventViewModel2::class.java]
        eventViewModel.eventList.observe(this) { events ->
            if (selectedCategory.isNullOrEmpty()) {
                // Show all events if no category is selected
                eventAdapter.updateEvents(events)
            } else {
                val filtered = events.filter { it.category == selectedCategory }
                if (filtered.isEmpty()) {
                    Toast.makeText(this, "No events found in $selectedCategory", Toast.LENGTH_SHORT).show()
                }
                eventAdapter.updateEvents(filtered)
            }
        }

        eventViewModel.fetchEvents()
    }
}
