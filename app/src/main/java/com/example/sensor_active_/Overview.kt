package com.example.sensor_active_

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.sensor_active_.Raspberry_Pages.AddGateway
import com.example.sensor_active_.Raspberry_Pages.classes.checkAvailable
import com.example.sensor_active_.Raspberry_Pages.connectRaspberry
import kotlinx.android.synthetic.main.activity_add_gateway.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Overview : AppCompatActivity() {

    val SHARED_PREFS = "IP_Addresses"
    var buttonText:String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)



    }

    override fun onResume() {
        super.onResume()
        var lin: LinearLayout = searchLayout
        lin.removeAllViewsInLayout()
        loadData()

    }
    fun loadData() {

        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val allPrefs = sharedPreferences.all
        for ((key, value) in allPrefs) {
                addButtons(key, value.toString())
            Log.i("loadData, Overview: ", buttonText)
        }


    }


    fun addButtons(key: String? , value : String) {
        val ip = key + "///" + value
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
            changeActivityToConnect(dynamicButton.text.toString())

        })
        var trueFalse: Boolean
            GlobalScope.launch {
                trueFalse = checkAvailable().isReachable(key.toString(), 8888, 500)

                if (trueFalse) {

                    dynamicButton.setTextColor(ContextCompat.getColor(applicationContext, R.color.HFUgreen))

                } else {

                    dynamicButton.setTextColor(ContextCompat.getColor(applicationContext, R.color.darkred))

                }


            }




        newlinLay.addView(dynamicButton)
        dynamicButton.text = ip
        // add Button to LinearLayout

    }

    fun changeActivityToConnect(_ip_address: String) {

        val intent = Intent(this, connectRaspberry::class.java)
        intent.putExtra("ip_Address", _ip_address)
        startActivity(intent)
    }

    fun addGateway(view: View) {
        val intent = Intent(this, AddGateway::class.java)
        startActivity(intent)

    }
}