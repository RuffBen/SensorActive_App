package com.example.sensor_active_.Raspberry_Pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.sensor_active_.Overview
import com.example.sensor_active_.R
import com.example.sensor_active_.Raspberry_Pages.classes.PreemtiveAuthChange
import com.example.sensor_active_.Raspberry_Pages.classes.PreemtiveAuthUserData
import kotlinx.android.synthetic.main.activity_e_s_p32_sensor.*
import kotlinx.android.synthetic.main.change_user_data.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class changeUserData : AppCompatActivity() {
    var activeIP: String = "No IP Address found"
    val PORT = ":8888"
    var activeIPHTTPS = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_user_data)
        setTitle("Update Userdata");

        loadData()

    }
    fun loadData() {
        val sharedPreferences = getSharedPreferences("IP_Active", MODE_PRIVATE)
        activeIP = sharedPreferences.getString("IP_Active", "No IP Address found").toString()
        val delimiter = "///"
        activeIPHTTPS = activeIP.split(delimiter)[0]
        activeIPHTTPS = "https://" + activeIPHTTPS + PORT
        Log.i("SPLITTED STRING", activeIPHTTPS)


    }
    fun applyChanges(view: View) {

            Log.i("ESP32", activeIPHTTPS)
            GlobalScope.launch {

                var response = PreemtiveAuthUserData(
                    activeIPHTTPS,
                    "/change_ud",
                    oldUN.getText().toString(),
                    oldPW.getText().toString(),
                    newUN.getText().toString(),
                    newPW.getText().toString(),
                    ).run()
                runOnUiThread {

                    if (response.contains("error")) {
                    Toast.makeText(
                        applicationContext,
                        "Server not responding",
                        Toast.LENGTH_SHORT
                    ).show()
                }else {
                        Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(applicationContext, Overview::class.java)
                        startActivity(intent)
                    }
                }
            }

    }


}