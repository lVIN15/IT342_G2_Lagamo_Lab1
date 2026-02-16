package com.lagamo.miniapp.repository

import android.content.Context
import android.content.SharedPreferences

/**
 * Secure JWT token storage using SharedPreferences.
 *
 * Stores the JWT token received after successful login.
 * Provides simple save / get / clear operations.
 */
class TokenManager(context: Context) {

    companion object {
        private const val PREF_NAME = "miniapp_auth_prefs"
        private const val KEY_TOKEN = "jwt_token"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * Save JWT token to secure storage.
     */
    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    /**
     * Retrieve the stored JWT token.
     * @return token string, or null if not logged in.
     */
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    /**
     * Clear stored token (logout).
     */
    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    /**
     * Check if user has a valid stored token.
     */
    fun isLoggedIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }
}
