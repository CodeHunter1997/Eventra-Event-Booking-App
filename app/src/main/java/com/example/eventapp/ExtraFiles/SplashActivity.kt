package com.example.eventapp.ExtraFiles

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.eventapp.OnboardingScreen.OnboardingScreen
import com.example.eventapp.R

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { false }


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logoImage: ImageView = findViewById(R.id.logo_image)
        val backgroundCircle: View = findViewById(R.id.background_circle)

        // Load animations
        logoImage.animate()
            .scaleX(2f)
            .scaleY(2f)
            .setDuration(3000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                // 2. Once logo is shown, animate background wave
                backgroundCircle.animate()
                    .scaleX(4f)
                    .scaleY(4f)
                    .alpha(0.3f)
                    .setDuration(3000)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
            }
            .start()

        // 3. Proceed to MainActivity after delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, OnboardingScreen::class.java))
            finish()
        }, 2500)
    }
}
