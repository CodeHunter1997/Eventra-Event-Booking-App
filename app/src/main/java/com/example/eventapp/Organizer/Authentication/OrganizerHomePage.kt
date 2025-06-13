package com.example.eventapp.Organizer.Authentication

import OrganizerLogout
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.eventapp.Organizer.Authentication.Fragments.OrganizerEventDashboard
import com.example.eventapp.Organizer.Authentication.Fragments.OrganizerHomeFragment
import com.example.eventapp.Organizer.Authentication.Fragments.OrganizerProfile
import com.example.eventapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class OrganizerHomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer_home_page)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Set default selected item
        bottomNav.selectedItemId = R.id.org_nav_home

        // Load the default fragment
        loadFragment(OrganizerHomeFragment())

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.org_nav_home -> loadFragment(OrganizerHomeFragment())
                R.id.org_nav_events -> loadFragment(OrganizerEventDashboard())
                R.id.org_nav_profile -> loadFragment(OrganizerProfile())
                R.id.org_nav_logout -> loadFragment(OrganizerLogout())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
