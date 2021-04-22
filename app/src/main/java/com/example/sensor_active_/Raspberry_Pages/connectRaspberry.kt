package com.example.sensor_active_.Raspberry_Pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.sensor_active_.R
import kotlinx.android.synthetic.main.activity_connect_raspberry.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class connectRaspberry : AppCompatActivity() {
    //HTTPS links only
    var url = ""
    var extension = ""
    var username = ""
    var password = ""
    var POST_message = ""
    val watingMessage = "waiting for results..."
    var textViewContent:String = ""
    var recievedIPAddress:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_raspberry)
        recievedIPAddress = intent.getStringExtra("ip_Address")
        displayIpAddress.text = recievedIPAddress

        if(recievedIPAddress.contains("///")){
            var delimiter = "///"
            recievedIPAddress = recievedIPAddress.split(delimiter)[1]
            recievedIPAddress = "https://" + recievedIPAddress + ":8888"
            Log.i("SPLITTED STRING", recievedIPAddress)

        }
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
        //start Process
        GlobalScope.launch {
            textViewContent= PreemtiveAuth(url, extension, username, password).run()
            runOnUiThread{
                text_view_result.text = textViewContent
            }
        }

    }
    fun getEmpty(view: View) {
        extension = ""
        getInputs()

        GlobalScope.launch {
            textViewContent= PreemtiveAuth(url, extension, username, password).run()
            runOnUiThread{
                text_view_result.text = textViewContent
            }
        }

    }
    fun sendPost(view: View) {
        extension = postMessage.text.toString()
        Log.i("POSTMessage", extension)

        getInputs()
        GlobalScope.launch {
            textViewContent= PreemtiveAuth(url, extension, username, password).run()
            runOnUiThread{
                text_view_result.text = textViewContent
            }
        }
    }
}