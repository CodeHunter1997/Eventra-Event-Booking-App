package com.example.eventapp.Organizer.Authentication.Fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.Organizer.Authentication.MainFiles.Adapters.OrganizerEventAdapter
import com.example.eventapp.Organizer.Authentication.MainFiles.Adapters.OrganizerUpcomingEventAdapter
import com.example.eventapp.Organizer.Authentication.MainFiles.Adapters.OrganizerViewPagerAdapter
import com.example.eventapp.Organizer.Authentication.MainFiles.OrganizerUpcomingActivity
import com.example.eventapp.R
import com.example.eventapp.User.Fragments.ProfileFragment
import com.example.eventapp.User.MainFiles.HomePage.Adapters.EventAdapter
import com.example.eventapp.User.MainFiles.HomePage.Adapters.HomeViewPagerAdapter
import com.example.eventapp.User.MainFiles.HomePage.Adapters.OrganizerAdapter
import com.example.eventapp.User.MainFiles.HomePage.Adapters.UpcomingEventsAdapter
import com.example.eventapp.User.MainFiles.HomePage.ViewModel.EventViewModel
import com.example.eventapp.User.MainFiles.HomePage.ViewModel.OrganizerViewModel
import com.example.eventapp.User.MainFiles.HomePage.ViewModel.UpcomingEventsViewModel
import com.example.eventapp.User.MainFiles.InboxSection
import com.example.eventapp.User.MainFiles.LocationActivity
import com.example.eventapp.User.MainFiles.NotificationActivity
import com.example.eventapp.User.MainFiles.UpcomingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import kotlin.getValue
import android.os.Handler
import android.os.Looper
import com.example.eventapp.User.MainFiles.HomePage.Adapters.TestimonialPagerAdapter

class OrganizerHomeFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var notification: ImageView
    private lateinit var profile: ImageView
    private lateinit var inbox: ImageView
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private var isAutoScrollRunning = false
    private lateinit var viewAll: TextView
    private lateinit var searchBar: EditText
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var upcomingRecyclerView: RecyclerView

    private val eventViewModel: EventViewModel by viewModels()
    private val upcomingEventsViewModel: UpcomingEventsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_organizer_home_page, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        notification = view.findViewById(R.id.notification)
        profile = view.findViewById(R.id.profile)
        inbox = view.findViewById(R.id.inbox)
        viewAll = view.findViewById(R.id.viewAll)
        searchBar = view.findViewById (R.id.searchEditText)
        eventRecyclerView = view.findViewById(R.id.eventRecyclerView)
        upcomingRecyclerView = view.findViewById(R.id.upcoming_events)

        eventRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        eventViewModel.eventList.observe(viewLifecycleOwner) { eventList ->
            eventRecyclerView.adapter = OrganizerEventAdapter(eventList)
        }

        eventViewModel.fetchEvents()
        eventRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(4, 4, 4, 4)
            }
        })

        upcomingRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        upcomingEventsViewModel.eventList.observe(viewLifecycleOwner) { eventList ->
            val limitedEvents = eventList.take(4)
            upcomingRecyclerView.adapter = OrganizerUpcomingEventAdapter(limitedEvents)
        }
        upcomingEventsViewModel.fetchEvents()

//        val sharedPref = requireContext().getSharedPreferences("organzierprefers", AppCompatActivity.MODE_PRIVATE)

        inbox.setOnClickListener {
            val intent = Intent(requireContext(), InboxSection::class.java)
            startActivity(intent)
        }

        profile.setOnClickListener {
            val profileNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            profileNav.selectedItemId = R.id.org_nav_profile // replace with your actual menu item ID

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, OrganizerProfile())
                .addToBackStack(null)
                .commit()
        }

        notification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

//        location.setOnClickListener {
//            val intent = Intent(requireContext(), LocationActivity::class.java)
//            startActivity(intent)
//        }

        viewAll.setOnClickListener {
            val intent = Intent(requireContext(), OrganizerUpcomingActivity::class.java)
            startActivity(intent)
        }

        searchBar.setOnClickListener {
            val intent = Intent(requireContext(), OrganizerUpcomingActivity::class.java)
            intent.putExtra("fromSearch", true)
            startActivity(intent)
        }


        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getEventList()
                if (response.isSuccessful) {
                    val eventList = response.body()?.data ?: emptyList()

                    if (eventList.size > 2) {
                        Toast.makeText(requireContext(), eventList[2].category, Toast.LENGTH_LONG).show()
                    }

                    val adapter = OrganizerViewPagerAdapter(eventList, requireContext())
                    viewPager.adapter = adapter
                    viewPager.offscreenPageLimit = 3

                    viewPager.setPageTransformer { page, position ->
                        page.scaleY = 0.85f + (1 - kotlin.math.abs(position)) * 0.15f
                    }

                } else {
                    Log.e("API_ERROR", "Code: ${response.code()}")
                    Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("EXCEPTION", "Error: ${e.message}")
                Toast.makeText(requireContext(), "Exception: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
        val viewPager: ViewPager2 = view.findViewById(R.id.customerReviews)

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getTestimonials()
                viewPager.adapter = TestimonialPagerAdapter(response.data)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Failed to load testimonials", e)
            }
        }

        return view
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoScrollRunnable)
        isAutoScrollRunning = false
    }

    override fun onResume() {
        super.onResume()
        if (!isAutoScrollRunning) {
            handler.postDelayed(autoScrollRunnable, 3000)
            isAutoScrollRunning = true
        }
    }
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            if (::viewPager.isInitialized && viewPager.adapter != null) {
                val itemCount = viewPager.adapter!!.itemCount
                currentPage = (viewPager.currentItem + 1) % itemCount
                viewPager.setCurrentItem(currentPage, true)
                handler.postDelayed(this, 3000) // Next scroll in 3 sec
            }
        }
    }
}