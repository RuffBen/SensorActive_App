package com.example.sensor_active_

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.sensor_active_.DatabasePages.MainDatabase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    


    fun pageRaspi(view: View) {
        Log.d("World", "Hello World")
        val intent = Intent(this, Overview::class.java)
        startActivity(intent)

        //setContentView(R.layout.activity_main_raspberry)

    }
    fun pageDatabase(view: View) {
        Log.d("World", "Hello Database")

         val intent = Intent(this, MainDatabase::class.java)
        startActivity(intent)

    }

}