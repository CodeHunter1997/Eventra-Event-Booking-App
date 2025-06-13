package com.example.eventapp.Organizer.Authentication.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.Organizer.Authentication.MainFiles.*
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrganizerProfile : Fragment() {

    private lateinit var share: ImageView
    private lateinit var contactUsIcon: ImageView
    private lateinit var delete_ic: ImageView
    private lateinit var feedback_ic: ImageView
    private lateinit var notification_ic: ImageView
    private lateinit var inbox_ic: ImageView
    private lateinit var location: TextView
    private lateinit var inbox: TextView
    private lateinit var notification: TextView
    private lateinit var feedback: TextView
    private lateinit var deleteAccount: TextView
    private lateinit var details: TextView
    private lateinit var profileImage: ImageView
    private lateinit var details_ic: ImageView
    private lateinit var firstName: TextView
    private lateinit var lastName: TextView
    private lateinit var profileDetails: TextView
    private lateinit var contactUs: TextView
    private lateinit var name: TextView
    private var currentImagePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_organizer_profile, container, false)

        // Initialize Views
        share = view.findViewById(R.id.share)
        delete_ic = view.findViewById(R.id.delete_ic)
        feedback_ic = view.findViewById(R.id.feedback_ic)
        notification_ic = view.findViewById(R.id.notification_ic)
        inbox_ic = view.findViewById(R.id.inbox_ic)
        contactUsIcon = view.findViewById(R.id.contactUs_ic)
        details_ic = view.findViewById(R.id.details_ic)
        profileImage = view.findViewById(R.id.image)
        inbox = view.findViewById(R.id.inbox)
        notification = view.findViewById(R.id.notification)
        feedback = view.findViewById(R.id.feedback)
        deleteAccount = view.findViewById(R.id.deleteAccount)
        profileDetails = view.findViewById(R.id.editDetails)
        contactUs = view.findViewById(R.id.contactUs)
        details = view.findViewById(R.id.details)
        name = view.findViewById(R.id.name)

        // Setup Click Listeners
        share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Check this out!")
                putExtra(Intent.EXTRA_TEXT, "Hey! Check out this awesome app: https://yourapp.link")
            }
            startActivity(Intent.createChooser(shareIntent, "Share using"))
        }

        notification.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }

        notification_ic.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }

        feedback.setOnClickListener {
            startActivity(Intent(requireContext(), FeedbackActivity::class.java))
        }

        feedback_ic.setOnClickListener {
            startActivity(Intent(requireContext(), FeedbackActivity::class.java))
        }

        deleteAccount.setOnClickListener {
            startActivity(Intent(requireContext(), OrganizerDeleteAccount::class.java))
        }

        delete_ic.setOnClickListener {
            startActivity(Intent(requireContext(), OrganizerDeleteAccount::class.java))
        }

        contactUs.setOnClickListener {
            startActivity(Intent(requireContext(), ContactUsActivity::class.java))
        }

        contactUsIcon.setOnClickListener {
            startActivity(Intent(requireContext(), ContactUsActivity::class.java))
        }

        profileDetails.setOnClickListener {
            val intent = Intent(requireContext(), OrganizerEditProfile::class.java)
            val imageUrl = "https://eventra-server.onrender.com/${currentImagePath}" // Use the correct path here
            intent.putExtra("profileImageUrl", imageUrl)
            startActivity(intent)
        }


        inbox.setOnClickListener {
            startActivity(Intent(requireContext(), InboxSection::class.java))
        }

        inbox_ic.setOnClickListener {
            startActivity(Intent(requireContext(), InboxSection::class.java))
        }

        details.setOnClickListener {
            startActivity(Intent(requireContext(), OrganizerAccountDetails::class.java))
        }

        details_ic.setOnClickListener {
            startActivity(Intent(requireContext(), OrganizerAccountDetails::class.java))
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        fetchOrganizerData()
    }

    private fun fetchOrganizerData() {
        val token = getAccessTokenFromPrefs()

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.instance.organizerDashboard(token)
                }

                if (response.isSuccessful) {
                    val data = response.body()?.data
                    data?.let {
                        name.text = it.company_name
                        currentImagePath = it.image
                        val imageUrl = "https://eventra-server.onrender.com/${currentImagePath}"
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.sample_details_page)
                            .into(profileImage)
                    }
                } else {
                    Log.e("ProfileFragment", "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ProfileFragment", "Exception: ${e.message}")
            }
        }
    }

    private fun getAccessTokenFromPrefs(): String {
        val sharedPref = requireContext().getSharedPreferences("eventAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("authToken", "") ?: ""
    }
}
