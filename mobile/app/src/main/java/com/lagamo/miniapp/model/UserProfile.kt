package com.lagamo.miniapp.model

import com.google.gson.annotations.SerializedName

/**
 * Response body from GET /api/auth/me
 * Matches the profile map returned by AuthController.getCurrentUser()
 */
data class UserProfile(
    @SerializedName("id")            val id: Long,
    @SerializedName("firstname")     val firstname: String?,
    @SerializedName("middlename")    val middlename: String?,
    @SerializedName("lastname")      val lastname: String?,
    @SerializedName("email")         val email: String?,
    @SerializedName("contactNumber") val contactNumber: String?,
    @SerializedName("street")        val street: String?,
    @SerializedName("barangay")      val barangay: String?,
    @SerializedName("municipality")  val municipality: String?,
    @SerializedName("province")      val province: String?,
    @SerializedName("country")       val country: String?
)
