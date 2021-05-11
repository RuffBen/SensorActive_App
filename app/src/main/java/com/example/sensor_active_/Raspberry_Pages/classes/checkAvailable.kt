package com.example.sensor_active_.Raspberry_Pages.classes

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class checkAvailable{



        fun isReachable(addr: String, openPort: Int, timeOutMillis: Int): Boolean {
        // Any Open port on other machine
        // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.

        return try {
            Socket().use { soc -> soc.connect(InetSocketAddress(addr, openPort), timeOutMillis) }
            true
        } catch (ex: IOException) {
            false
        }
    }

}