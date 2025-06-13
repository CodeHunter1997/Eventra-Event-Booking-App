package com.example.eventapp.Organizer.Authentication.MainFiles

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.API_With_Object.ApiClient
import com.example.eventapp.DataModels.UpcomingEvent
import com.example.eventapp.Organizer.Authentication.MainFiles.Adapters.OrganizerUpcomingEventPageAdapter
import com.example.eventapp.R
import com.example.eventapp.User.MainFiles.HomePage.Adapters.UpcomingEventsPageAdapter
import com.example.eventapp.User.MainFiles.UpcomingActivity
import kotlinx.coroutines.launch
import java.util.Locale

class OrganizerUpcomingActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var micIcon: ImageView
    private lateinit var upcomingRecyclerView: RecyclerView
    private lateinit var adapter: OrganizerUpcomingEventPageAdapter
    private var allEvents: List<UpcomingEvent> = emptyList()

    private val REQUEST_CODE_SPEECH_INPUT = 100
    private val REQUEST_CODE_AUDIO_PERMISSION = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizer_upcoming)

        searchEditText = findViewById(R.id.searchEditText)
        micIcon = findViewById(R.id.micIcon)  // Init mic icon
        upcomingRecyclerView = findViewById(R.id.upcoming_events)

        upcomingRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = OrganizerUpcomingEventPageAdapter(emptyList())
        upcomingRecyclerView.adapter = adapter

        fetchUpcomingEvents()
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterEvents(s.toString())
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
    private fun fetchUpcomingEvents() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getUpcomingEvents()
                if (response.isSuccessful && response.body() != null) {
                    allEvents = response.body()!!.data
                    adapter.updateData(allEvents)
                } else {
                    Log.e("UpcomingActivity", "Error code: ${response.code()}")
                    Toast.makeText(this@OrganizerUpcomingActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("UpcomingActivity", "Exception: ${e.localizedMessage}")
                Toast.makeText(this@OrganizerUpcomingActivity, "Exception: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun filterEvents(query: String) {
        val filteredList = if (query.isNotBlank()) {
            allEvents.filter { event ->
                event.event_name.contains(query, ignoreCase = true) ||
                        (event.category?.contains(query, ignoreCase = true) ?: false) ||
                        event.location.city.contains(query, ignoreCase = true)
            }
        } else {
            allEvents
        }
        adapter.updateData(filteredList)
    }
    private fun checkAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestAudioPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_CODE_AUDIO_PERMISSION)
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
            Toast.makeText(this, "Speech input not supported", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_AUDIO_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceInput()
            } else {
                Toast.makeText(this, "Permission denied for audio recording", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                searchEditText.setText(result[0])
            }
        }
        }
}