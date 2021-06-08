package com.example.sensor_active_

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class sketchRaspberrys : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sketch_raspberrys)
    }


    val SHARED_PREF_RASPI_IPS = "IP_Addresses"
    val SHARED_PREF_RASPI_STATUS = ""
}