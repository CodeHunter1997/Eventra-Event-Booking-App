package com.example.eventapp.User.MainFiles

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.R

class FeedbackActivity : AppCompatActivity() {
    lateinit var subject: EditText
    lateinit var yourMessage: EditText
    lateinit var sendBtn: Button
    private lateinit var backArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        backArrow = findViewById(R.id.backArrow)
        subject = findViewById(R.id.subject)
        yourMessage = findViewById(R.id.yourMessage)
        sendBtn = findViewById(R.id.sendBtn)

        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        sendBtn.setOnClickListener {
            val subjectValue = subject.text.toString().trim()
            val messageValue = yourMessage.text.toString().trim()

            if (subject.text.isEmpty()){
                Toast.makeText(this, "Please fill the Subject", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (yourMessage.text.isEmpty()){
                Toast.makeText(this, "Please fill the Feedback", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                Toast.makeText(this, "Thanks for your feedback!", Toast.LENGTH_LONG).show()
//                subject.text.clear()
//                yourMessage.text.clear()
                finish()
            }

        }

    }
}