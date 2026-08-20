package com.amos_tech_code.kmp_memocore.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class AppleUrlUtils : UrlUtils {

    override fun openUrlInBrowser(url: String) {

        val nsUrl = NSURL.URLWithString(url)
        UIApplication.sharedApplication.openURL(nsUrl!!)
    }

}