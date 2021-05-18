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
    var textViewContent:String = ""
    var recievedIPAddress = "non"
    var recievedHostName:String = "non"
    var recievedIPAddressHTTPS = ""
    val SHARED_PREFS = "IP_Addresses"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_raspberry)
        loadData()



        displayIpAddress.text = recievedHostName + "\n" + recievedIPAddressHTTPS


    }

    fun loadData() {
        val sharedPreferences = getSharedPreferences("IP_Active", MODE_PRIVATE)
        recievedIPAddress = sharedPreferences.getString("IP_Active", "No IP Address found").toString()
        val delimiter = "///"
        recievedHostName = recievedIPAddress.split(delimiter)[1]
        recievedIPAddress = recievedIPAddress.split(delimiter)[0]
        recievedIPAddressHTTPS = "https://" + recievedIPAddress + PORT
        Log.i("SPLITTED STRING", recievedIPAddressHTTPS)


    }

    fun getInputs() {
     //   text_view_result.text = "watingMessage"
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

               // textViewSensors = textViewContent.split(delimiter)[1]

            runOnUiThread{
                searchLayout.removeAllViews()

                //    text_view_result.text = textViewSensors
            //    if (textViewContent.contains("}"))
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
               // text_view_result.text = textViewContent
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
        val iDS = JSONObject(JSONObject(textViewContent).get("data").toString()).get("sensors").toString()
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
        val sensorID = _sensorName
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
        dynamicButton.text = sensorID
        // add Button to LinearLayout

    }
    fun changeActivityToSensor(_sensorName: String) {

        val intent = Intent(this, ESP32Sensor::class.java)
        intent.putExtra("Content", textViewContent)
        intent.putExtra("sensorID", _sensorName)
        intent.putExtra("IP", url)
        startActivity(intent)
    }



    fun addSensor(view: View) {
        val intent = Intent(this, AddSensor::class.java)
        startActivity(intent)

    }
}