package com.lagamo.miniapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lagamo.miniapp.R
import com.lagamo.miniapp.repository.TokenManager

/**
 * Landing Screen — First screen the user sees.
 *
 * Matches the web app's landing page with:
 * - Logo, title, subtitle
 * - Login button (magenta primary)
 * - Register button (ghost/outlined)
 *
 * If the user already has a valid token, skip to Dashboard.
 */
class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If already logged in, go straight to dashboard
        val tokenManager = TokenManager(this)
        if (tokenManager.isLoggedIn()) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_landing)

        // Login button
        findViewById<android.widget.Button>(R.id.btn_login).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Register button
        findViewById<android.widget.Button>(R.id.btn_register).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
