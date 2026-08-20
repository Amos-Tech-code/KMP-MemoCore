package com.amos_tech_code.kmp_memocore.utils

import kotlin.time.Instant

expect object DateUtils {

    fun formatDate(instant: Instant, format: String = "yyyy-MM-dd'T'HH:mm:ss"): String

}