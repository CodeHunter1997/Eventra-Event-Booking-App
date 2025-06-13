package com.example.eventapp.ExtraFiles

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.Organizer.Authentication.OrganizerLogin
import com.example.eventapp.R
import com.example.eventapp.User.Authentication.UserLogin
import com.example.eventapp.ExtraFiles.UserTypeRegistration
import com.google.android.material.button.MaterialButton

class UserTypeLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_type_login)

        val organizerButton = findViewById<MaterialButton>(R.id.organizer)
        val userButton = findViewById<MaterialButton>(R.id.attendee)


        organizerButton.setOnClickListener {
            startActivity(Intent(this, OrganizerLogin::class.java))
        }

        userButton.setOnClickListener {
            startActivity(Intent(this, UserLogin::class.java))
        }

        findViewById<TextView>(R.id.signup).setOnClickListener {
            val intent = Intent(this, UserTypeRegistration::class.java)
            startActivity(intent)
        }
    }
}