package com.lagamo.miniapp.network

import com.lagamo.miniapp.model.LoginRequest
import com.lagamo.miniapp.model.LoginResponse
import com.lagamo.miniapp.model.RegisterRequest
import com.lagamo.miniapp.model.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Retrofit API interface matching the existing Spring Boot endpoints.
 */
interface ApiService {

    /**
     * POST /api/auth/register
     * Registers a new user. No authentication required.
     */
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Any>

    /**
     * POST /api/auth/login
     * Authenticates user credentials. Returns a JWT token.
     */
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    /**
     * GET /api/auth/me
     * Retrieves the authenticated user's profile.
     * Requires Authorization: Bearer <token>
     */
    @GET("api/auth/me")
    suspend fun getProfile(@Header("Authorization") authHeader: String): Response<UserProfile>
}
