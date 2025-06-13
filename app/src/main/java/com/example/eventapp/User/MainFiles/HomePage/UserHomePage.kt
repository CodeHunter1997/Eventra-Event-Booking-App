package com.example.eventapp.User.MainFiles.HomePage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.eventapp.R
import com.example.eventapp.User.Fragments.BookingFragment
import com.example.eventapp.User.Fragments.CategoriesFragment
import com.example.eventapp.User.Fragments.EventFragment
import com.example.eventapp.User.Fragments.HomeFragment
import com.example.eventapp.User.Fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserHomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home_page)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Default fragment
        loadFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_events -> loadFragment(EventFragment())
                R.id.nav_categories -> loadFragment(CategoriesFragment())
                R.id.nav_booking -> loadFragment(BookingFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
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