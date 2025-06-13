package com.example.eventapp.User.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.DataModels.EventMinimal
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.CategoryEventListActivity
import com.example.eventapp.User.MainFiles.HomePage.Adapters.CategoryAdapter
import com.example.eventapp.User.MainFiles.HomePage.ViewModel.EventViewModel2

class CategoriesFragment : Fragment() {

    private lateinit var eventViewModel: EventViewModel2
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    private var allEvents = listOf<EventMinimal>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        eventViewModel = ViewModelProvider(this)[EventViewModel2::class.java]

        // Observe events from ViewModel
        eventViewModel.eventList.observe(viewLifecycleOwner) { events ->
            allEvents = events

            // Extract unique category names from events
            val uniqueCategories = allEvents.map { it.category }.distinct().sorted()

            // Create or update adapter with category list
            categoryAdapter = CategoryAdapter(uniqueCategories) { categoryName ->
                val intent = Intent(requireContext(), CategoryEventListActivity::class.java)
                intent.putExtra("category", categoryName)
                startActivity(intent)
            }

            recyclerView.adapter = categoryAdapter
        }

        // Fetch events
        eventViewModel.fetchEvents()
    }
}
