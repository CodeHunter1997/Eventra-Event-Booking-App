package com.example.eventapp.User.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.ChangePasswordActivity
import com.example.eventapp.User.MainFiles.ContactUsActivity
import com.example.eventapp.User.MainFiles.HomePage.DeleteActivity
import com.example.eventapp.User.MainFiles.FeedbackActivity
import com.example.eventapp.User.MainFiles.InboxSection
import com.example.eventapp.User.MainFiles.LocationActivity
import com.example.eventapp.User.MainFiles.NotificationActivity
import com.example.eventapp.User.MainFiles.ProfileDetails
import com.example.eventapp.ExtraFiles.UserTypeLogin
import com.google.android.material.bottomnavigation.BottomNavigationView


class ProfileFragment : Fragment() {
 lateinit var share: ImageView
 lateinit var locationIcon: ImageView
 lateinit var contactUsIcon: ImageView
 lateinit var delete_ic: ImageView
 lateinit var feedback_ic: ImageView
 lateinit var notification_ic: ImageView
 lateinit var inbox_ic: ImageView
 lateinit var booking_ic: ImageView
 lateinit var location: TextView
 lateinit var bookingHistory: TextView
 lateinit var inbox: TextView
 lateinit var notification: TextView
 lateinit var feedback: TextView
 lateinit var deleteAccount: TextView
 lateinit var changePassword: TextView
 lateinit var logout: TextView
 lateinit var profileImage: ImageView
 lateinit var logout_ic: ImageView
 lateinit var changePassword_ic: ImageView
 lateinit var firstName: TextView
 lateinit var lastName: TextView
 lateinit var profileDetails: TextView
 lateinit var contactUs: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize views from the layout
        share = view.findViewById(R.id.share)
        locationIcon = view.findViewById(R.id.locationIcon)
        delete_ic = view.findViewById(R.id.delete_ic)
        feedback_ic = view.findViewById(R.id.feedback_ic)
        notification_ic = view.findViewById(R.id.notification_ic)
        inbox_ic = view.findViewById(R.id.inbox_ic)
        booking_ic = view.findViewById(R.id.booking_ic)
        changePassword_ic = view.findViewById(R.id.changePassword_ic)
        logout_ic = view.findViewById(R.id.logout_ic)
        contactUsIcon = view.findViewById(R.id.contactUs_ic)

        firstName = view.findViewById(R.id.firstName)
        lastName = view.findViewById(R.id.lastName)
        profileImage = view.findViewById(R.id.tv_update_event_title)
        location = view.findViewById(R.id.createdAtEdt)
        bookingHistory = view.findViewById(R.id.bookingHistory)
        inbox = view.findViewById(R.id.inbox)
        notification = view.findViewById(R.id.notification)
        feedback = view.findViewById(R.id.feedback)
        deleteAccount = view.findViewById(R.id.deleteAccount)
        logout = view.findViewById(R.id.logout)
        changePassword = view.findViewById(R.id.changePassword)
        profileDetails = view.findViewById(R.id.profileDetails)
        contactUs = view.findViewById(R.id.contactUs)

        val sharedPref = requireContext().getSharedPreferences("eventAppPrefs", AppCompatActivity.MODE_PRIVATE)
        val firstNameStr = sharedPref.getString("first_name", "") ?: ""
        val lastNameStr = sharedPref.getString("last_name", "") ?: ""
        val imageview = sharedPref.getString("image","") ?: ""
        firstName.text = firstNameStr
        lastName.text = lastNameStr
        if (imageview.isNotEmpty()) {
            // Convert backslashes to forward slashes and prepend base URL
            val imageUrl = "https://eventra-server.onrender.com/" + imageview.replace("\\", "/")
            Log.d("PROFILE_IMAGE_URL", "Full Image URL: $imageUrl")

            // Set to ImageView using Glide or any other image loader
            Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.profile_image)
                .error(R.drawable.profile_image)
                .into(profileImage)
        }

        // Set click listeners
        share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain" // or "image/*" if you're sharing an image
                putExtra(Intent.EXTRA_SUBJECT, "Check this out!") // optional
                putExtra(Intent.EXTRA_TEXT, "Hey! Check out this awesome app: https://yourapp.link")
            }
            startActivity(Intent.createChooser(shareIntent, "Share using"))
        }

        changePassword_ic.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        changePassword.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        locationIcon.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            startActivity(intent)
        }

        location.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            startActivity(intent)
        }

        booking_ic.setOnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNav.selectedItemId = R.id.nav_booking

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BookingFragment())
                .addToBackStack(null)
                .commit()
        }

        bookingHistory.setOnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNav.selectedItemId = R.id.nav_booking

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BookingFragment())
                .addToBackStack(null)
                .commit()
        }

        inbox.setOnClickListener {
            val intent = Intent(requireContext(), InboxSection::class.java)
            startActivity(intent)
        }

        inbox_ic.setOnClickListener {
            val intent = Intent(requireContext(), InboxSection::class.java)
            startActivity(intent)
        }

        notification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        notification_ic.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        feedback.setOnClickListener {
            val intent = Intent(requireContext(), FeedbackActivity::class.java)
            startActivity(intent)
        }

        feedback_ic.setOnClickListener {
            val intent = Intent(requireContext(), FeedbackActivity::class.java)
            startActivity(intent)
        }

        deleteAccount.setOnClickListener {
            val intent = Intent(requireContext(), DeleteActivity::class.java)
            startActivity(intent)
        }

        delete_ic.setOnClickListener {
            val intent = Intent(requireContext(), DeleteActivity::class.java)
            startActivity(intent)
        }

            contactUs.setOnClickListener {
            val intent = Intent(requireContext(), ContactUsActivity::class.java)
            startActivity(intent)
        }

        contactUsIcon.setOnClickListener {
            val intent = Intent(requireContext(), ContactUsActivity::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener {
            showLogoutConfirmation()
        }

        logout_ic.setOnClickListener {
            showLogoutConfirmation()
        }
        profileDetails.setOnClickListener {
            val intent = Intent(requireContext(), ProfileDetails::class.java)
            startActivity(intent)
        }

        return view
    }
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, _ ->
                logoutUser()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun logoutUser() {
        val sharedPref = requireContext().getSharedPreferences("eventAppPrefs", AppCompatActivity.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireContext(), UserTypeLogin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }




}