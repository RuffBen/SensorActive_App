package com.example.sensor_active_.DatabasePages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sensor_active_.R

public class MainDatabase : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_database)

    }

    public fun getMyData(v: View) {
        connectDatabase().main()
    }


}