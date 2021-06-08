package com.example.sensor_active_.Raspberry_Pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sensor_active_.R
import kotlinx.android.synthetic.main.activity_sketch_data.*

class sketchData : AppCompatActivity() {
    val SHARED_PREFS_IP_LIST = "IP_STATUS_LIST"
    var currentIP = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sketch_data)



        val sharedPreferences = getSharedPreferences(SHARED_PREFS_IP_LIST, MODE_PRIVATE)
        Log.i("SHARED PREFS IP_LIST", sharedPreferences.getString(currentIP, "no content for this status" + currentIP))
        var Data = sharedPreferences.getString(currentIP, "no content for this status" + currentIP)
        visData.text = Data

    }


}