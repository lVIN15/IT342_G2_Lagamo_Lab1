package com.lagamo.miniapp.model

import com.google.gson.annotations.SerializedName

/**
 * Response body from POST /api/auth/login
 * Backend returns: { "token": "..." }
 */
data class LoginResponse(
    @SerializedName("token") val token: String
)
