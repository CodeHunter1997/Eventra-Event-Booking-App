package com.example.eventapp.User.Fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.DataModels.EventMinimal
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.HomePage.Adapters.EventListAdapter
import com.example.eventapp.User.MainFiles.HomePage.ViewModel.EventViewModel2
import com.example.eventapp.User.MainFiles.InboxSection
import com.example.eventapp.User.MainFiles.NotificationActivity
import java.util.Locale
import android.Manifest
//import android.content.pm.PackageManager
//import androidx.core.content.ContextCompat
//import androidx.core.app.ActivityCompat

class EventFragment : Fragment() {

    private lateinit var eventViewModel: EventViewModel2
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventListAdapter
    private lateinit var searchEditText: EditText // Declare searchEditText
    private lateinit var notification: ImageView
    private lateinit var profile: ImageView
    private lateinit var inbox: ImageView
    private var REQUEST_CODE_SPEECH_INPUT = 100

    private val REQUEST_CODE_AUDIO_PERMISSION = 101
    private lateinit var micIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        micIcon = view.findViewById(R.id.micIcon)
        inbox = view.findViewById(R.id.inbox)
        profile = view.findViewById(R.id.profile)
        notification = view.findViewById(R.id.notification)
        recyclerView = view.findViewById(R.id.upcoming_events)

        searchEditText = view.findViewById(R.id.searchEditText) // Initialize searchEditText
        setupRecyclerView()

        eventViewModel = ViewModelProvider(this)[EventViewModel2::class.java]
        eventViewModel.eventList.observe(viewLifecycleOwner) { events ->
            eventAdapter.updateEvents(events)

        }

        eventViewModel.fetchEvents()


        // Navigate to Inbox
        inbox.setOnClickListener {
            val intent = Intent(requireContext(), InboxSection::class.java)
            startActivity(intent)
        }

        // Navigate to ProfileFragment (using FragmentTransaction)
        profile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        // Navigate to NotificationActivity
        notification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }


        // Add TextWatcher for search functionality
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Call filterEvents in adapter with the search query
                eventAdapter.filterEvents(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        micIcon.setOnClickListener {
            if (checkAudioPermission()) {
                startVoiceInput()
            } else {
                requestAudioPermission()
            }

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                searchEditText.setText(result[0])
            }
        }
    }
    private fun checkAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestAudioPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_CODE_AUDIO_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_AUDIO_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startVoiceInput()
            } else {
                Toast.makeText(requireContext(), "Permission denied for audio recording", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupRecyclerView() {
        eventAdapter = EventListAdapter(emptyList<EventMinimal>()) // Initialize with correct type
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = eventAdapter
        recyclerView.setHasFixedSize(true)

    }
    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
        }
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Speech input not supported", Toast.LENGTH_SHORT).show()
        }
    }


}

