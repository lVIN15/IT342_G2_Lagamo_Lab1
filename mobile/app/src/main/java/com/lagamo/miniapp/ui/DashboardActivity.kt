package com.lagamo.miniapp.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lagamo.miniapp.R
import com.lagamo.miniapp.model.UserProfile
import com.lagamo.miniapp.repository.AuthRepository
import com.lagamo.miniapp.repository.TokenManager
import kotlinx.coroutines.launch

/**
 * Dashboard / Profile Screen — Shows the authenticated user's profile.
 *
 * Matches the web app's dashboard:
 * - Avatar circle with initials (magenta gradient)
 * - Full name + email subtitle
 * - "Active" status badge (cyan)
 * - Info rows: Email, Contact, Address
 * - Logout button with confirmation dialog
 *
 * Automatically redirects to Login if token is invalid/expired.
 */
class DashboardActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository

    private lateinit var txtLoading: TextView
    private lateinit var profileContent: LinearLayout
    private lateinit var txtAvatar: TextView
    private lateinit var txtFullname: TextView
    private lateinit var txtEmailSubtitle: TextView
    private lateinit var txtInfoEmail: TextView
    private lateinit var txtInfoContact: TextView
    private lateinit var txtInfoAddress: TextView
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tokenManager = TokenManager(this)
        authRepository = AuthRepository(tokenManager)

        initViews()
        loadProfile()
    }

    private fun initViews() {
        txtLoading = findViewById(R.id.txt_loading)
        profileContent = findViewById(R.id.profile_content)
        txtAvatar = findViewById(R.id.txt_avatar)
        txtFullname = findViewById(R.id.txt_fullname)
        txtEmailSubtitle = findViewById(R.id.txt_email_subtitle)
        txtInfoEmail = findViewById(R.id.txt_info_email)
        txtInfoContact = findViewById(R.id.txt_info_contact)
        txtInfoAddress = findViewById(R.id.txt_info_address)
        btnLogout = findViewById(R.id.btn_logout)

        btnLogout.setOnClickListener { showLogoutDialog() }
    }

    private fun loadProfile() {
        txtLoading.visibility = View.VISIBLE
        profileContent.visibility = View.GONE

        lifecycleScope.launch {
            val result = authRepository.getProfile()
            result.onSuccess { profile ->
                displayProfile(profile)
            }.onFailure {
                // Token invalid → redirect to login
                redirectToLogin()
            }
        }
    }

    private fun displayProfile(profile: UserProfile) {
        txtLoading.visibility = View.GONE
        profileContent.visibility = View.VISIBLE

        // Avatar initials
        val firstInitial = (profile.firstname ?: "").take(1).uppercase()
        val lastInitial = (profile.lastname ?: "").take(1).uppercase()
        txtAvatar.text = "$firstInitial$lastInitial"

        // Full name
        val fullName = listOfNotNull(
            profile.firstname,
            profile.middlename,
            profile.lastname
        ).filter { it.isNotBlank() }.joinToString(" ")
        txtFullname.text = fullName

        // Email subtitle
        txtEmailSubtitle.text = profile.email ?: ""

        // Info rows
        txtInfoEmail.text = profile.email ?: getString(R.string.dash_placeholder)
        txtInfoContact.text = profile.contactNumber?.ifBlank { null }
            ?: getString(R.string.dash_placeholder)

        // Full address
        val fullAddress = listOfNotNull(
            profile.street,
            profile.barangay,
            profile.municipality,
            profile.province,
            profile.country
        ).filter { it.isNotBlank() }.joinToString(", ")
        txtInfoAddress.text = fullAddress.ifBlank { getString(R.string.dash_placeholder) }
    }

    private fun showLogoutDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)

        val dialog = AlertDialog.Builder(this, R.style.Theme_MiniApp)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Make dialog background transparent to show our custom card
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.findViewById<Button>(R.id.btn_dialog_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_dialog_confirm).setOnClickListener {
            dialog.dismiss()
            performLogout()
        }

        dialog.show()
    }

    private fun performLogout() {
        authRepository.logout()
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("loggedOut", true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun redirectToLogin() {
        authRepository.logout()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
