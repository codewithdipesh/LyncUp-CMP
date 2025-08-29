package com.codewithdipesh.lyncup

interface Platform {
    val os : String
    val name : String
}

expect fun getPlatform(): Platform