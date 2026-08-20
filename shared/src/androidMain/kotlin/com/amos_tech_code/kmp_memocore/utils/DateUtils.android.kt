package com.amos_tech_code.kmp_memocore.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Instant

actual object DateUtils {

    actual fun formatDate(instant: Instant, format: String): String {
        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(instant.toEpochMilliseconds())
        return dateFormatter.format(date)

    }
}