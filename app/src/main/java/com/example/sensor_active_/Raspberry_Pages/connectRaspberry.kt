package com.example.sensor_active_.Raspberry_Pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.sensor_active_.R
import com.example.sensor_active_.Raspberry_Pages.classes.PreemtiveAuth
import com.example.sensor_active_.Raspberry_Pages.classes.checkAvailable
import kotlinx.android.synthetic.main.activity_add_gateway.*
import kotlinx.android.synthetic.main.activity_connect_raspberry.*
import kotlinx.android.synthetic.main.activity_connect_raspberry.searchLayout
import kotlinx.android.synthetic.main.activity_connect_raspberry.text_view_result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.InetAddress

class connectRaspberry : AppCompatActivity() {
    //HTTPS links only
    var url = ""
    var extension = ""
    var username = ""
    var password = ""
    val PORT = ":8888"
    val watingMessage = "waiting for results..."
    var textViewContent:String = ""
    var textViewSensors = ""
    var recievedIPAddress = "non"
    var recievedHostName:String = "non"
    var recievedIPAddressHTTPS = ""
    val SHARED_PREFS = "IP_Addresses"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_raspberry)
        recievedIPAddress = intent.getStringExtra("ip_Address")
         val delimiter = "///"
            recievedHostName = recievedIPAddress.split(delimiter)[1]
            recievedIPAddress = recievedIPAddress.split(delimiter)[0]
            recievedIPAddressHTTPS = "https://" + recievedIPAddress + PORT
            Log.i("SPLITTED STRING", recievedIPAddressHTTPS)


        displayIpAddress.text = recievedHostName + "\n" + recievedIPAddressHTTPS

    }


    fun getInputs() {
        text_view_result.text = watingMessage
        url = recievedIPAddressHTTPS
        username = ip_username.text.toString()
        password = ip_password.text.toString()
    }



    fun getStatus(view: View) {
        extension = "/status"

        getInputs()
        // Run on other Thread
        GlobalScope.launch {
            textViewContent= PreemtiveAuth(url, extension, username, password).run()
            val delimiter = "\"sensors\": {"

            textViewSensors = textViewContent.split(delimiter)[1]


            runOnUiThread{
            //    text_view_result.text = textViewSensors
                callForButtons()
            }
        }


    }


    fun sendPost(view: View) {
        extension = "/change" //Option -> KEY hier
        getInputs()
        // Run on other Thread
        GlobalScope.launch {
            textViewContent= PreemtiveAuth(url, extension, username, password).run()
            runOnUiThread{
                text_view_result.text = textViewContent
                callForButtons()

            }
        }

    }

    fun removeIP(view: View) {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        Log.i("shardALL1: ", sharedPreferences.all.toString())

            editor.remove(recievedIPAddress).commit()
            Toast.makeText(this, "IP removed", Toast.LENGTH_SHORT).show()
            Log.i("shardALL2: ", sharedPreferences.all.toString())



    }

    fun callForButtons(){
        var iDS = JSONObject(JSONObject(textViewContent).get("data").toString()).get("sensors").toString()
        val jsonObject = JSONObject(iDS)
        val keys: Iterator<String> = jsonObject.keys()
        //loop to get all sensor names
        while (keys.hasNext()) {
            val key = keys.next()
            if (jsonObject.get(key) is JSONObject) {
                Log.i("iterator", key.toString())
                // ADD Buttons to scrollview
                addButtons(key)
            }
        }
    }

    fun addButtons(_sensorName: String?) {
        val sensorName = _sensorName
        //define the Parent of the Buttons
        var linLay: LinearLayout = searchLayout

        //adds new layout for button
        var newlinLay: LinearLayout = LinearLayout(this)
        // defines button
        val dynamicButton = Button(this)
        linLay.addView(newlinLay)
        // setting layout_width and layout_height using layout parameters
        dynamicButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dynamicButton.setOnClickListener(View.OnClickListener { view ->
            // Log.i("ButtonClick:", dynamicButton.text.toString())
            changeActivityToSensor(dynamicButton.text.toString())
        })

        newlinLay.addView(dynamicButton)
        dynamicButton.text = sensorName
        // add Button to LinearLayout

    }
    fun changeActivityToSensor(_sensorName: String) {

        val intent = Intent(this, ESP32Sensor::class.java)
        intent.putExtra("ip_Address", _sensorName)
        startActivity(intent)
    }
}