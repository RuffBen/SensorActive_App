package com.example.sensor_active_

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.sensor_active_.DatabasePages.MainDatabase
import com.example.sensor_active_.DatabasePages.connectDatabase
import com.example.sensor_active_.Raspberry_Pages.MainRaspberry


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    


    fun pageRaspi(view: View) {
        Log.d("World", "Hello World")
        val intent = Intent(this, MainRaspberry::class.java)
        startActivity(intent)

        //setContentView(R.layout.activity_main_raspberry)

    }
    fun pageDatabase(view: View) {
        Log.d("World", "Hello World")

         val intent = Intent(this, MainDatabase::class.java)
        startActivity(intent)

    }

}