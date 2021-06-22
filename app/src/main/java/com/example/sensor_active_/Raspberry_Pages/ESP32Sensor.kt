package com.example.sensor_active_.Raspberry_Pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.sensor_active_.Overview
import com.example.sensor_active_.R
import com.example.sensor_active_.Raspberry_Pages.classes.Assert
import com.example.sensor_active_.Raspberry_Pages.classes.PreemtiveAuth
import com.example.sensor_active_.Raspberry_Pages.classes.PreemtiveAuthChange
import com.example.sensor_active_.Raspberry_Pages.classes.PreemtiveAuthSensors
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_e_s_p32_sensor.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class ESP32Sensor : AppCompatActivity() {

    var activeIP: String = "No IP Address found"
    val PORT = ":8888"
    var activeIPHTTPS = ""
    var recivedString: String? = ""
    var sensorID: String = ""
    var sensorName = ""
    var bluetoothAddress = ""
    var syncInterval = ""
    var status = ""
    val SHARED_PREFS_PW_LIST = "PW_LIST"
    private var username = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_e_s_p32_sensor)
        setTitle("Sensor configuration");
        sensorID = intent.getStringExtra("sensorID")

        recivedString = intent.getStringExtra("Content")
        recivedString =
            JSONObject(JSONObject(recivedString).get("data").toString()).get("sensors").toString()
        recivedString = JSONObject(recivedString).get(sensorID).toString()

        setValues()
        loadData()
    }

    fun setValues() {
        val gSon = Gson().fromJson<Assert>(recivedString, Assert::class.java)
        sensorIDXML.setText(sensorID)
        sensorName = gSon.sensor_name.toString()
        sensorNameXML.setText(sensorName)
        headline.text = sensorName
        bluetoothAddress = gSon.sensor_bluetooth_adress.toString()
        sensorBluetooth.setText(bluetoothAddress)
        syncInterval = gSon.sync_interval.toString()
        sensorIntervall.setText(syncInterval)
        status = gSon.status.toString()
        sensorStatus.setText(status)

    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("IP_Active", MODE_PRIVATE)
        activeIP = sharedPreferences.getString("IP_Active", "No IP Address found").toString()
        val delimiter = "///"
        activeIP = activeIP.split(delimiter)[0]
        activeIPHTTPS = "https://" + activeIP + PORT

        val sharedPreferencesPW = getSharedPreferences(SHARED_PREFS_PW_LIST, MODE_PRIVATE)

        var userValue = sharedPreferencesPW.getString(activeIP, "no Userdata found")
        if (userValue == "no Userdata found") {
            Toast.makeText(this, "Please set Logindata on Gateways page!", Toast.LENGTH_SHORT)
                .show()
        } else {
            username = JSONObject(userValue).get("username").toString()
            password = JSONObject(userValue).get("password").toString()

        }
    }

    fun changes(view: View) {
        Log.i("ESP32", activeIPHTTPS)
        GlobalScope.launch {

            var response = PreemtiveAuthChange(
                activeIPHTTPS,
                "/change_sensor",
                username,
                password,
                sensorIDXML.getText().toString(),
                sensorNameXML.getText().toString(),
                sensorIntervall.getText().toString()
            ).run()
            runOnUiThread {
                if (response.contains("Invalid credentials")) {
                    Toast.makeText(
                        applicationContext,
                        "Wrong Username or Password",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, Overview::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    fun removeSesnor(view: View) {
        Log.i("remove Sensor", sensorID)
        GlobalScope.launch {
            val response = PreemtiveAuthChange(
                activeIPHTTPS,
                "/remove_sensor",
                username,
                password,
                sensorID,
                "",
                ""
            ).run()
            runOnUiThread {
                if(response.contains("Invalid credentials")){
                    Toast.makeText(applicationContext, "Wrong Username or Password", Toast.LENGTH_SHORT).show()

                }else {
                    Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, Overview::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}