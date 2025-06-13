package com.example.eventapp.User.MainFiles

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eventapp.R

    class InboxSection : AppCompatActivity() {
        private lateinit var backArrow: ImageView


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox_section)
            backArrow = findViewById(R.id.backArrow)

            backArrow.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
    }
}