package com.amos_tech_code.kmp_memocore

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform