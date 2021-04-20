package com.example.sensor_active_.Raspberry_Pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sensor_active_.DatabasePages.MainDatabase
import com.example.sensor_active_.R
import kotlinx.android.synthetic.main.activity_main_raspberry.*

class MainRaspberry :  AppCompatActivity() {

    var ip_address:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_raspberry)
        var test: String = ""





    }

    fun ConnectPage(view: View) {
        ip_address = "https://" + ip_address_input.text.toString() + ":" + ip_port.text.toString()
        Log.i("MainRaspberry sendip: ", ip_address)

        val intent = Intent(this, connectRaspberry::class.java)
        intent.putExtra("ip_Address", ip_address)
        startActivity(intent)

    }




}