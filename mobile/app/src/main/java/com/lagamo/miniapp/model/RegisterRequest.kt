package com.lagamo.miniapp.model

import com.google.gson.annotations.SerializedName

/**
 * Request body for POST /api/auth/register
 * Matches the backend User entity fields.
 */
data class RegisterRequest(
    @SerializedName("firstname")     val firstname: String,
    @SerializedName("middlename")    val middlename: String,
    @SerializedName("lastname")      val lastname: String,
    @SerializedName("street")        val street: String,
    @SerializedName("barangay")      val barangay: String,
    @SerializedName("municipality")  val municipality: String,
    @SerializedName("province")      val province: String,
    @SerializedName("country")       val country: String,
    @SerializedName("contactNumber") val contactNumber: String,
    @SerializedName("email")         val email: String,
    @SerializedName("password")      val password: String
)
