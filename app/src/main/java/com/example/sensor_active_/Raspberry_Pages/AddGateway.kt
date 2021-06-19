package com.example.sensor_active_.Raspberry_Pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sensor_active_.R
import com.example.sensor_active_.Raspberry_Pages.classes.checkAvailable
import kotlinx.android.synthetic.main.activity_add_gateway.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.util.*


class AddGateway : AppCompatActivity() {

    var ip_HostName_and_Address: String = ""

    var allRaspberrysName: List<String> = ArrayList()
    var allRaspberrysIP: List<String> = ArrayList()

    val SHARED_PREFS = "IP_Addresses"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gateway)
        setTitle("Add Gateway");

    }

    //function which calls the Connected Page -> also adds https to the string and sends the ip over to next activity
    fun ConnectPage(view: View) {
        GlobalScope.launch {
            ip_HostName_and_Address = ip_address_input.text.toString()
            val ip_HostName: String = InetAddress.getByName(ip_HostName_and_Address).hostName
            ip_HostName_and_Address = ip_HostName + "///" + ip_HostName_and_Address
            Log.i("MainRaspberry sendip: ", ip_HostName_and_Address)

            changeActivityToConnection(ip_HostName_and_Address)
        }
    }

    fun changeActivityToConnection(_ip_address: String) {

        val intent = Intent(this, connectRaspberry::class.java)
        intent.putExtra("ip_Address", _ip_address)
        startActivity(intent)
    }


    //this all is for the search tough local network for raspberrys
    fun searchGatewaysButton(view: View) {
        searchGateway.isEnabled = false
        searchGateway.text = "Searching..."
        GlobalScope.launch {

            searchGateways()
            runOnUiThread {
                searchGateway.text = "Search for new gateways"
                searchGateway.isEnabled = true
            }
        }

    }

    fun searchGateways() {
        var inetAddress: InetAddress

        allRaspberrysName = emptyList<String>()
        allRaspberrysIP = emptyList<String>()
        var currentHostname: String
        runOnUiThread {
            searchLayout.removeAllViews()
        }
        try {
            //go through all 255 Ports and check for answer with ping
            for (i in 0 until 255 step 1) {
                progressBar.progress = i * 10 / 26
                inetAddress = InetAddress.getByName("192.168.0." + i.toString())
                currentHostname = inetAddress.hostName
                Log.i("getByHostName", currentHostname)
                //calls if "isLetters()" which sorts out all IP's without an hostname and adds them to the arrays
                // if (isLetters(currentHostname) && checkAvailable().isReachable(currentHostname, 8888, 500)){
                if (isLetters(currentHostname)) {
                    allRaspberrysName += inetAddress.hostName
                    allRaspberrysIP += inetAddress.hostAddress


                }
            }
            progressBar.progress = 100

            //goes through arrays and adds a Button for every Gateway
            for (i in allRaspberrysIP.indices) {
                Log.i(
                    "List of Raspies",
                    "All Raspis are: " + allRaspberrysName[i] + ": " + allRaspberrysIP[i]
                )
                runOnUiThread {

                    addButton(allRaspberrysName[i], allRaspberrysIP[i])

                    //Your code
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun isLetters(string: String): Boolean {
        return string.any { it.isLetter() }
        //return string.contains("raspberry")
    }


    //adds Buttons function -> defines structure of the buttons
    fun addButton(name: String, ip: String) {

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
        //set Onclick Listener for buttons
        dynamicButton.setOnClickListener(View.OnClickListener { view ->
            Log.i("ButtonClick:", dynamicButton.text.toString())
            saveClickedIP(dynamicButton.text.toString())
        })
        newlinLay.addView(dynamicButton)

        dynamicButton.text = name + "///" + ip
        // add Button to LinearLayout

    }

    //saves the IP addresses in Shared Preferences
    fun saveClickedIP(saveText: String) {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val delimiter = "///"
        var hostName = saveText.split(delimiter)[0]
        var ipAddress = saveText.split(delimiter)[1]
        val editor = sharedPreferences.edit()
        Log.i("shardALL: ", sharedPreferences.all.toString())


        editor.putString(ipAddress, hostName)
        editor.apply()
        Toast.makeText(this, "IP saved", Toast.LENGTH_SHORT).show()
        Log.i("shardALL2: ", sharedPreferences.all.toString())

    }
}