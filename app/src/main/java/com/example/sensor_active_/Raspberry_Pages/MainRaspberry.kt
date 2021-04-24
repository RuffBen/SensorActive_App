package com.example.sensor_active_.Raspberry_Pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.sensor_active_.R
import kotlinx.android.synthetic.main.activity_main_raspberry.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*


class MainRaspberry : AppCompatActivity() {

    var ip_address: String = ""

    var allRaspberrysName: List<String> = ArrayList()
    var allRaspberrysIP: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_raspberry)


    }

    //function which calls the Connected Page -> also adds https to the string and sends the ip over to next activity
    fun ConnectPage(view: View) {
        ip_address = "https://" + ip_address_input.text.toString() + ":" + ip_port.text.toString()
        Log.i("MainRaspberry sendip: ", ip_address)

        changeActivityToConnection(ip_address)

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
            runOnUiThread{
                searchGateway.text = "Search for new gateways"
                searchGateway.isEnabled = true
            }
        }

    }

    fun searchGateways() {
        var inetAddress: InetAddress

        allRaspberrysName = emptyList<String>()
        allRaspberrysIP = emptyList<String>()
        var currentHostname:String
        runOnUiThread {
            searchLayout.removeAllViews()
        }
        try {
            //go through all 255 Ports and check for answer with ping
            for (i in 0 until 255 step 1) {
                inetAddress = InetAddress.getByName("192.168.0." + i.toString())
                currentHostname =inetAddress.hostName
                Log.i("getByHostName", currentHostname )
                //calls if "isLetters()" which sorts out all IP's without an hostname and adds them to the arrays
                if (isLetters(currentHostname) && isReachable(currentHostname,8888, 500 )) {
                    allRaspberrysName += inetAddress.hostName
                    allRaspberrysIP += inetAddress.hostAddress


                }
            }

            //goes through arrays and adds a Button for every Gateway
            for (i in allRaspberrysIP.indices) {
                Log.i("List of Raspies", "All Raspis are: " + allRaspberrysName[i] + ": " + allRaspberrysIP[i])
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
        dynamicButton.setOnClickListener(View.OnClickListener { view ->
            Log.i("ButtonClick:", dynamicButton.text.toString())
            changeActivityToConnection(dynamicButton.text.toString())
        })
        newlinLay.addView(dynamicButton)
        dynamicButton.text = name +"///" +  ip
        // add Button to LinearLayout

    }

    fun isReachable(addr: String, openPort: Int, timeOutMillis: Int): Boolean {
        // Any Open port on other machine
        // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.
        return try {
            Socket().use { soc -> soc.connect(InetSocketAddress(addr, openPort), timeOutMillis) }
            true
        } catch (ex: IOException) {
            false
        }
    }


}