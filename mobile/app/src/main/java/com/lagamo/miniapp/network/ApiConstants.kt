package com.lagamo.miniapp.network

/**
 * ============================================================
 *  API Constants — Dynamic Base URL Configuration
 * ============================================================
 *
 *  Since this app may be compiled in a Cloud Environment (e.g., Project IDX),
 *  the Base URL is NOT hardcoded.
 *
 *  ➜ PASTE YOUR BACKEND'S PUBLIC URL BELOW.
 *     Make sure it ends with a trailing slash "/".
 *
 *  Examples:
 *     "http://10.0.2.2:8080/"          ← Android emulator → localhost
 *     "https://your-app.herokuapp.com/" ← Cloud-hosted backend
 *     "https://abc123.idx.dev/"         ← Project IDX backend
 */
object ApiConstants {

    // ✏️  Change this to your backend's public URL
    const val BASE_URL = "http://localhost:8080/"
}
