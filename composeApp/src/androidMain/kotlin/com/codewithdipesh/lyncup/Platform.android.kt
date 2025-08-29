package com.codewithdipesh.lyncup

import android.os.Build
import java.net.InetAddress

class AndroidPlatform : Platform {
    override val name: String = "${Build.MANUFACTURER}_${Build.MODEL}"
    override val os: String = "Android"

}
actual fun getPlatform(): Platform = AndroidPlatform()