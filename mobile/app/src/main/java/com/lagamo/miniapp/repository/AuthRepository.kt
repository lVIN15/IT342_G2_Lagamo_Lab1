package com.lagamo.miniapp.repository

import com.lagamo.miniapp.model.LoginRequest
import com.lagamo.miniapp.model.LoginResponse
import com.lagamo.miniapp.model.RegisterRequest
import com.lagamo.miniapp.model.UserProfile
import com.lagamo.miniapp.network.RetrofitClient

/**
 * Repository that bridges the ApiService and TokenManager.
 * Provides clean suspend functions for the UI layer.
 */
class AuthRepository(private val tokenManager: TokenManager) {

    private val api = RetrofitClient.apiService

    /**
     * Register a new user.
     * @return Result.success(Unit) or Result.failure(exception)
     */
    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                val message = parseError(errorBody, "Registration failed")
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Login and store the JWT token.
     * @return Result.success(token) or Result.failure(exception)
     */
    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (token != null) {
                    tokenManager.saveToken(token)
                    Result.success(token)
                } else {
                    Result.failure(Exception("Empty token received"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val message = parseError(errorBody, "Invalid credentials")
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseError(errorBody: String?, defaultMsg: String): String {
        return try {
            if (errorBody != null) {
                val json = org.json.JSONObject(errorBody)
                json.getString("message")
            } else {
                defaultMsg
            }
        } catch (e: Exception) {
            errorBody ?: defaultMsg
        }
    }

    /**
     * Fetch the current user's profile using stored token.
     * @return Result.success(UserProfile) or Result.failure(exception)
     */
    suspend fun getProfile(): Result<UserProfile> {
        return try {
            val token = tokenManager.getToken()
                ?: return Result.failure(Exception("No token stored"))

            val response = api.getProfile("Bearer $token")
            if (response.isSuccessful) {
                val profile = response.body()
                if (profile != null) {
                    Result.success(profile)
                } else {
                    Result.failure(Exception("Empty profile response"))
                }
            } else {
                // Token is invalid/expired — clear it
                if (response.code() == 401) {
                    tokenManager.clearToken()
                }
                Result.failure(Exception("Failed to load profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Logout — clear stored token.
     */
    fun logout() {
        tokenManager.clearToken()
    }

    /**
     * Check if user is currently logged in.
     */
    fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
}
