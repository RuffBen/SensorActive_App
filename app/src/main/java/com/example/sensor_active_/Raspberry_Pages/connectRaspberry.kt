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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
    var textViewContent: String = ""
    var recievedIPAddress = "non"
    var recievedHostName: String = "non"
    var recievedIPAddressHTTPS = ""
    val SHARED_PREFS = "IP_Addresses"
    var shared_prefs_ip = ""
    val SHARED_PREFS_IP_LIST = "IP_STATUS_LIST"
    val SHARED_PREFS_IP_ACTIVE = "IP_Active"
    val SHARED_PREFS_PW_LIST = "PW_LIST"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_raspberry)
        setTitle("Gateway configuration")
        loadData()
        //getSensorStatus()
        displayIpAddress.text = recievedHostName + "\n" + recievedIPAddressHTTPS

        swipeRefresh()
    }

    fun loadData() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS_IP_ACTIVE, MODE_PRIVATE)
        recievedIPAddress =
            sharedPreferences.getString(SHARED_PREFS_IP_ACTIVE, "No IP Address found").toString()
        val delimiter = "///"
        recievedHostName = recievedIPAddress.split(delimiter)[1]
        recievedIPAddress = recievedIPAddress.split(delimiter)[0]
        shared_prefs_ip = recievedIPAddress
        recievedIPAddressHTTPS = "https://" + recievedIPAddress + PORT
        Log.i("SPLITTED STRING", recievedIPAddressHTTPS)
        val sharedPreferencesPW = getSharedPreferences(SHARED_PREFS_PW_LIST, MODE_PRIVATE)
        var userValue = sharedPreferencesPW.getString(shared_prefs_ip, "no Userdata found")
        if(userValue == "no Userdata found"){
            Toast.makeText(this, "Could not find default Logindata", Toast.LENGTH_SHORT).show()
        }else {
            ip_username.setText(JSONObject(userValue).get("username").toString())
           ip_password.setText(JSONObject(userValue).get("password").toString())

        }



    }

    fun swipeRefresh() {
        swipe_refresh.setOnRefreshListener {

            Toast.makeText(this, "page refreshed", Toast.LENGTH_SHORT).show()
            finish()
            overridePendingTransition(0, 0)
            startActivity(getIntent())
            overridePendingTransition(0, 0)
            swipe_refresh.isRefreshing = false
        }

    }

    fun getInputs() {
        //   text_view_result.text = "watingMessage"
        url = recievedIPAddressHTTPS
        username = ip_username.text.toString()
        password = ip_password.text.toString()
    }


    fun getStatus(view: View) {
        getSensorStatus()


    }

    fun getSensorStatus() {
        extension = "/status"

        getInputs()
        // Run on other Thread
        GlobalScope.launch {
            textViewContent = PreemtiveAuth(url, extension, username, password).run()

            // textViewSensors = textViewContent.split(delimiter)[1]

            runOnUiThread {
                searchLayout.removeAllViews()
                if(textViewContent.contains("Invalid credentials")){
                    Toast.makeText(applicationContext, "Wrong Username or Password", Toast.LENGTH_SHORT).show()

                } else if(textViewContent.contains("error")){
                    Toast.makeText(applicationContext, "Server not responding", Toast.LENGTH_SHORT).show()
                } else {
                    //    text_view_result.text = textViewSensors
                    //    if (textViewContent.contains("}"))
                    callForButtons()
                    val sharedPreferences = getSharedPreferences(SHARED_PREFS_IP_LIST, MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    Log.i("added to IP_LIST: ", textViewContent)
                    editor.putString(recievedIPAddress, textViewContent)
                    editor.apply()
                    Log.i("SHARED PREFS IP_LIST", sharedPreferences.getString(recievedIPAddress, "no content for this status" + recievedIPAddress))
                }


            }
        }
    }
    fun changeUD(view: View) {
        val intent = Intent(this, changeUserData::class.java)
        startActivity(intent)
    }




    fun removeIP(view: View) {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        Log.i("shardALL1: ", sharedPreferences.all.toString())

        editor.remove(recievedIPAddress).commit()
        Toast.makeText(this, "IP removed", Toast.LENGTH_SHORT).show()
        Log.i("shardALL2: ", sharedPreferences.all.toString())


    }

    fun callForButtons() {

        val iDS =
            JSONObject(JSONObject(textViewContent).get("data").toString()).get("sensors").toString()
        val jsonObject = JSONObject(iDS)
        val keys: Iterator<String> = jsonObject.keys()
        //loop to get all sensor names
        while (keys.hasNext()) {
            val key = keys.next()
            if (jsonObject.get(key) is JSONObject) {
                Log.i("iterator", key.toString())
                var thisSensName = JSONObject(
                    JSONObject(
                        JSONObject(
                            JSONObject(textViewContent).get("data").toString()
                        ).get("sensors").toString()
                    ).get(key).toString()
                ).get("sensor_name").toString()
                // var thisSensName = JSONObject(JSONObject(textViewContent).get(key).toString()).get("sensor_name").toString()
                // ADD Buttons to scrollview
                addButtons(key, thisSensName)
            }
        }
    }

    fun addButtons(_sensorID: String?, _sensorName: String) {
        val sensorID = _sensorID
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
            changeActivityToSensor(sensorID)
        })

        newlinLay.addView(dynamicButton)
        dynamicButton.text = sensorID + " : " + sensorName
        // add Button to LinearLayout

    }

    fun changeActivityToSensor(_sensorID: String?) {

        val intent = Intent(this, ESP32Sensor::class.java)
        intent.putExtra("Content", textViewContent)
        intent.putExtra("sensorID", _sensorID)
        intent.putExtra("IP", url)
        startActivity(intent)
    }


    fun addSensor(view: View) {
        val intent = Intent(this, AddSensor::class.java)
        startActivity(intent)

    }

    fun setUserdata(view: View) {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS_PW_LIST, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        Log.i("shardALL: ", sharedPreferences.all.toString())
        var ip_username_ = ip_username.text
        var ip_password_ = ip_password.text
        Log.i("USERDATA SAVE", "{\"username\":\"$ip_username_\",\"password\":\"$ip_password_\"}")
        editor.putString(shared_prefs_ip, "{\"username\":\"$ip_username_\",\"password\":\"$ip_password_\"}")
        editor.apply()
        Toast.makeText(this, "New Logindata set", Toast.LENGTH_SHORT).show()




    }

}