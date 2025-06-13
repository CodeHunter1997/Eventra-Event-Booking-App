package com.example.eventapp.OnboardingScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.eventapp.Organizer.Authentication.Fragments.OrganizerHomeFragment
import com.example.eventapp.Organizer.Authentication.OrganizerHomePage
import com.example.eventapp.R
import com.example.eventapp.User.Fragments.HomeFragment
import com.example.eventapp.User.MainFiles.HomePage.UserHomePage
import com.example.eventapp.databinding.ActivityOnboardingScreenBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.jvm.java

class OnboardingScreen : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPref = getSharedPreferences("eventAppPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        val userType = sharedPref.getString("userType", null)
        Log.d("OnboardingPrefs", "isLoggedIn=$isLoggedIn, userType=$userType")


        if (isLoggedIn) {
            val userType = sharedPref.getString("userType", "attendee")
            if (userType == "attendee") {
                startActivity(Intent(this, UserHomePage::class.java))
            } else {
                startActivity(Intent(this, OrganizerHomePage::class.java))
            }
            finish()
        }


        // Show onboarding screen only if not logged in
        binding = ActivityOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val onboardingItems = listOf(
            OnboardingItem(R.drawable.onboarding2, "Feel the Beat, Live the Night"),
            OnboardingItem(R.drawable.onboarding1, "Seamless Planning, Impactful Results"),
            OnboardingItem(R.drawable.onboarding3, "Where Strategy Takes the Spotlight")
        )

        val adapter = OnboardingAdapter(onboardingItems, binding.viewPager)
        binding.viewPager.adapter = adapter
        binding.indicator.setViewPager(binding.viewPager)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()
    }
}
