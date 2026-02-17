package com.lagamo.miniapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lagamo.miniapp.R
import com.lagamo.miniapp.repository.AuthRepository
import com.lagamo.miniapp.repository.TokenManager
import kotlinx.coroutines.launch

/**
 * Login Screen — Email + Password authentication.
 *
 * Matches the web app's login page design:
 * - Glassmorphism card with back button
 * - Email/Password inputs with white background
 * - Magenta "Sign in" button
 * - Cyan "Register for free" link
 * - Toast notifications (cyan success, magenta error)
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var btnSignIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tokenManager = TokenManager(this)
        authRepository = AuthRepository(tokenManager)

        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)
        btnSignIn = findViewById(R.id.btn_sign_in)

        // Show toast if redirected from registration or logout
        val fromRegister = intent.getBooleanExtra("registered", false)
        val fromLogout = intent.getBooleanExtra("loggedOut", false)
        if (fromRegister) {
            ToastHelper.showSuccess(this, getString(R.string.toast_register_success))
        } else if (fromLogout) {
            ToastHelper.showSuccess(this, getString(R.string.toast_logout_success))
        }

        // Back button
        findViewById<android.widget.ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }

        // Sign in
        btnSignIn.setOnClickListener {
            handleLogin()
        }

        // Register link
        findViewById<TextView>(R.id.link_register).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun handleLogin() {
        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            ToastHelper.showError(this, "Please fill in all fields")
            return
        }

        btnSignIn.isEnabled = false
        btnSignIn.text = "Signing in..."

        lifecycleScope.launch {
            val result = authRepository.login(email, password)
            result.onSuccess {
                // Navigate to Dashboard, clear back stack
                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }.onFailure { error ->
                btnSignIn.isEnabled = true
                btnSignIn.text = getString(R.string.btn_sign_in)
                ToastHelper.showError(this@LoginActivity, error.message ?: getString(R.string.toast_login_error))
            }
        }
    }
}
