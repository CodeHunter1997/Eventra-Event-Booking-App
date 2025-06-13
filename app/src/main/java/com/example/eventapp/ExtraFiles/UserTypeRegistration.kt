package com.example.eventapp.ExtraFiles

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.Organizer.Authentication.OrganizerSignUpEmailID
import com.example.eventapp.R
import com.example.eventapp.User.Authentication.UserSignupEmailID
import com.google.android.material.button.MaterialButton

class UserTypeRegistration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_type_registration)

        // Buttons for each role
//        val adminButton = findViewById<MaterialButton>(R.id.admin)
        val organizerButton = findViewById<MaterialButton>(R.id.organizer)
        val userButton = findViewById<MaterialButton>(R.id.attendee)

        // Navigate to respective signup activities
//        adminButton.setOnClickListener {
//            val intent = Intent(this, AdminSignUp::class.java)
//            startActivity(intent)
//        }

        organizerButton.setOnClickListener {
            val intent = Intent(this, OrganizerSignUpEmailID::class.java)
            startActivity(intent)
        }

        userButton.setOnClickListener {
            val intent = Intent(this, UserSignupEmailID::class.java)
            startActivity(intent)
        }

//        // Optionally handle a "Sign up" text click
//        findViewById<TextView>(R.id.signup).setOnClickListener {
//            val intent = Intent(this, UserSignUp::class.java)
//            startActivity(intent)
//        }
    }
}