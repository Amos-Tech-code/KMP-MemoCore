package com.amos_tech_code.kmp_memocore.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String,
    val email: String,
    val refreshToken: String,
    val userId: String
)