package com.amos_tech_code.kmp_memocore.data.remote

import com.amos_tech_code.kmp_memocore.model.AuthRequest
import com.amos_tech_code.kmp_memocore.model.AuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

expect  val BASE_URL_EMULATOR : String

const val BASE_URL = "https://quicknotes-server-18yo.onrender.com"

class ApiService(
    val client: HttpClient
) {

    private val ACTIVE_URL = BASE_URL
    private val LOGIN_ENDPOINT = "$ACTIVE_URL/auth/login"
    private val SIGNUP_ENDPOINT = "$ACTIVE_URL/auth/signup"

    suspend fun login(request: AuthRequest) : Result<AuthResponse> {

        return try {

            val response = client.post(LOGIN_ENDPOINT) {
                setBody(request)
            }

            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body() as AuthResponse)
            } else {
                Result.failure(Exception("Something went wrong"))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }

    }

    suspend fun signup(request: AuthRequest) : Result<AuthResponse> {

        return try {

            val response = client.post(SIGNUP_ENDPOINT) {
                setBody(request)
            }

            if (response.status == HttpStatusCode.Created) {
                Result.success(response.body() as AuthResponse)
            } else {
                Result.failure(Exception("Something went wrong"))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }

    }

}