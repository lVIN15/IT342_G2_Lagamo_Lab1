package com.lagamo.miniapp.model

import com.google.gson.annotations.SerializedName

/**
 * Request body for POST /api/auth/login
 */
data class LoginRequest(
    @SerializedName("email")    val email: String,
    @SerializedName("password") val password: String
)
