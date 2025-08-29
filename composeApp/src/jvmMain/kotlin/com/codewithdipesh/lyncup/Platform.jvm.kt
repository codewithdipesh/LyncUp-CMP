package com.codewithdipesh.lyncup

import java.net.InetAddress

class DesktopPlatform : Platform {
    override val name: String =
        try {
            InetAddress.getLocalHost().hostName
        } catch (e: Exception) {
            "unknown"
        }
    override val os: String = System.getProperty("os.name").lowercase()

}
actual fun getPlatform(): Platform = DesktopPlatform()