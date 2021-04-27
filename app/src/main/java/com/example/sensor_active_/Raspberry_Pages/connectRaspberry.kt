package com.example.sensor_active_.Raspberry_Pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.sensor_active_.R
import com.example.sensor_active_.Raspberry_Pages.classes.PreemtiveAuth
import kotlinx.android.synthetic.main.activity_connect_raspberry.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    var recievedIPAddress = "non"
    var recievedHostName:String = "non"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_raspberry)
        recievedIPAddress = intent.getStringExtra("ip_Address")
         val delimiter = "///"
            recievedHostName = recievedIPAddress.split(delimiter)[0]
            recievedIPAddress = recievedIPAddress.split(delimiter)[1]
            recievedIPAddress = "https://" + recievedIPAddress + PORT
            Log.i("SPLITTED STRING", recievedIPAddress)


        displayIpAddress.text = recievedHostName + "\n" + recievedIPAddress

    }


    fun getInputs() {
        text_view_result.text = watingMessage
        url = recievedIPAddress
        username = ip_username.text.toString()
        password = ip_password.text.toString()
    }



    fun getStatus(view: View) {
        extension = "/status"
        getInputs()
        // Run on other Thread
        GlobalScope.launch {
            textViewContent= PreemtiveAuth(url, extension, username, password).run()
            runOnUiThread{
                text_view_result.text = textViewContent
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
            }
        }

    }

}