package com.amos_tech_code.kmp_memocore.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import kotlin.time.Instant

actual object DateUtils {

    actual fun formatDate(instant: Instant, format: String): String {

        val nsDate = NSDate(instant.toEpochMilliseconds() / 1000.0)
        val dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = format
        return dateFormatter.stringFromDate(nsDate)

    }
}