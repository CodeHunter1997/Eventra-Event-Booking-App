package com.example.eventapp.User.Fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.HomePage.Adapters.EventAdapter
import com.example.eventapp.User.MainFiles.HomePage.ViewModel.EventViewModel
import com.example.eventapp.User.MainFiles.HomePage.Adapters.HomeViewPagerAdapter
import com.example.eventapp.User.MainFiles.HomePage.Adapters.UpcomingEventsAdapter
import com.example.eventapp.User.MainFiles.HomePage.ViewModel.OrganizerViewModel
import com.example.eventapp.User.MainFiles.HomePage.ViewModel.UpcomingEventsViewModel
import com.example.eventapp.User.MainFiles.InboxSection
import com.example.eventapp.User.MainFiles.LocationActivity
import com.example.eventapp.User.MainFiles.NotificationActivity
import com.example.eventapp.User.MainFiles.UpcomingActivity
import com.example.eventapp.User.MainFiles.HomePage.Adapters.OrganizerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import kotlin.getValue
import android.os.Handler
import android.os.Looper
import androidx.viewpager.widget.ViewPager
import com.example.eventapp.API_With_Object.ApiInterface
import com.example.eventapp.DataModels.UpcomingEvents.AllTestimonialResponse
import com.example.eventapp.User.MainFiles.HomePage.Adapters.TestimonialPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var notification: ImageView
    private lateinit var profile: ImageView
    private lateinit var inbox: ImageView
    private lateinit var location: ImageView
    private lateinit var firstName: TextView
    private lateinit var lastName: TextView
    private lateinit var viewAll: TextView
    private lateinit var searchBar: EditText
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var upcomingRecyclerView: RecyclerView
    private lateinit var recyclerView: RecyclerView

    private val eventViewModel: EventViewModel by viewModels()
    private val upcomingEventsViewModel: UpcomingEventsViewModel by viewModels()
    private val organizerViewModel: OrganizerViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private var isAutoScrollRunning = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        notification = view.findViewById(R.id.notification)
        profile = view.findViewById(R.id.profile)
        inbox = view.findViewById(R.id.inbox)
        location = view.findViewById(R.id.locationIcon)
        firstName = view.findViewById(R.id.firstName)
        lastName = view.findViewById(R.id.lastName)
        viewAll = view.findViewById(R.id.viewAll)
        searchBar = view.findViewById (R.id.searchEditText)
        eventRecyclerView = view.findViewById(R.id.eventRecyclerView)
        upcomingRecyclerView = view.findViewById(R.id.upcoming_events)
        recyclerView = view.findViewById(R.id.organizerRecyclerView)

        eventRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        eventViewModel.eventList.observe(viewLifecycleOwner) { eventList ->
            val limitedEvents = eventList.take(4)
            eventRecyclerView.adapter = EventAdapter(eventList)
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
            upcomingRecyclerView.adapter = UpcomingEventsAdapter(limitedEvents)
        }
        upcomingEventsViewModel.fetchEvents()

//        organizerRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        organizerViewModel.organizerList.observe(viewLifecycleOwner) { organizerList ->
//            organizerRecyclerView.adapter = OrganizerAdapter(organizerList)
//        }
//
//        organizerViewModel.fetchOrganizers()
//        organizerRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
//            override fun getItemOffsets(
//                outRect: Rect,
//                view: View,
//                parent: RecyclerView,
//                state: RecyclerView.State
//            ) {
//                outRect.right = 16
//            }
//        })

        val sharedPref = requireContext().getSharedPreferences("eventAppPrefs", AppCompatActivity.MODE_PRIVATE)
        val firstNameStr = sharedPref.getString("first_name", "") ?: ""
        val lastNameStr = sharedPref.getString("last_name", "") ?: ""

        firstName.text = firstNameStr
        lastName.text = lastNameStr


        inbox.setOnClickListener {
            val intent = Intent(requireContext(), InboxSection::class.java)
            startActivity(intent)
        }

        profile.setOnClickListener {
            val profileNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            profileNav.selectedItemId = R.id.nav_profile // replace with your actual menu item ID

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        notification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        location.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            startActivity(intent)
        }

        viewAll.setOnClickListener {
            val intent = Intent(requireContext(), UpcomingActivity::class.java)
            startActivity(intent)
        }

        searchBar.setOnClickListener {
            val intent = Intent(requireContext(), UpcomingActivity::class.java)
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

                    val adapter = HomeViewPagerAdapter(eventList, requireContext())
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


        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getOrganizers()
                if (response.isSuccessful) {
                    val organizerList = response.body()?.data ?: emptyList()
                    val adapter = OrganizerAdapter(organizerList)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("API", "Failed to fetch: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Exception: ${e.message}")
            }
        }

        val viewPager: ViewPager2 = view.findViewById(R.id.customerReviews)

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getTestimonials()
                viewPager.adapter = TestimonialPagerAdapter(response.data)
                Log.d("TESTIMONIAL_API", "Response size: ${response.data.size}")
                startAutoScroll()

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
    private fun startAutoScroll() {
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