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

    var url = ""
    var recivedString: String? = ""
    var sensorID: String = ""
    var sensorName = ""
    var bluetoothAddress = ""
    var syncInterval = ""
    var status = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_e_s_p32_sensor)
        sensorID = intent.getStringExtra("sensorID")
        url = intent.getStringExtra("IP")

        recivedString = intent.getStringExtra("Content")
        recivedString =
            JSONObject(JSONObject(recivedString).get("data").toString()).get("sensors").toString()
        recivedString = JSONObject(recivedString).get(sensorID).toString()

        setValues()
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

    fun changes(view: View) {
        Log.i("ESP32", url)

        GlobalScope.launch {

            PreemtiveAuthChange(
                url,
                "/change_sensor",
                "demo",
                "demo",
                sensorIDXML.getText().toString(),
                sensorNameXML.getText().toString(),
                sensorIntervall.getText().toString()
            ).run()
            runOnUiThread {

                Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, Overview::class.java)
                startActivity(intent)
            }


        }



    }

    fun removeSesnor(view: View) {
        GlobalScope.launch {

            PreemtiveAuthChange(
                url,
                "/remove_sensor",
                "demo",
                "demo",
                sensorID,
                "",
                ""
            ).run()
            runOnUiThread {

                Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, Overview::class.java)
                startActivity(intent)
            }
        }

    }

}