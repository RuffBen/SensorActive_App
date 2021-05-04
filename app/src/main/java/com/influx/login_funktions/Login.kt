package com.influx.login_funktions

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import com.influx.R
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn_Login = findViewById(R.id.bt_Login) as Button

        btn_Login.setOnClickListener{
            getUserInfos()
        }

    }

    fun getUserInfos():ArrayList<String>{
        var outStringList = ArrayList<String>()

        var portIDAlternativ = findViewById(R.id.text_portAlternativ) as EditText
        var portID = portIDAlternativ.toString()

        var websiteLink = findViewById(R.id.text_WebsiteLogin) as EditText
        var loginName = findViewById(R.id.editTextPersonName) as EditText
        var loginPassword = findViewById(R.id.editTextPassword) as EditText
        println(websiteLink.text.toString())

        println(websiteLink.text.toString()+":"+portID+loginName.text.toString()+loginPassword.text.toString().toCharArray())

        //val influxDBClient = InfluxDBClientKotlinFactory.create(websiteLink.toString()+":"+portID,loginName.toString(),loginPassword.toString().toCharArray())

        return outStringList
    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            var portIDAlternativ = findViewById(R.id.text_portAlternativ) as EditText
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.RadioB_StandartPort->
                    if (checked) {
                        portIDAlternativ.setVisibility(View.GONE)

                    }
                R.id.RadioB_differentPort ->
                    if (checked) {
                        portIDAlternativ.setVisibility(View.VISIBLE)
                    }
            }
        }
    }
}