package com.amos_tech_code.kmp_memocore.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class AndroidUrlUtils(val context: Context) : UrlUtils {

    override fun openUrlInBrowser(url: String) {

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = url.toUri()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }

}