package com.example.sensor_active_

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
//To Main DataBasePage
//import com.example.sensor_active_.DatabasePages.MainDatabase
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sensor_active_.Raspberry_Pages.AddGateway
import com.example.sensor_active_.Raspberry_Pages.classes.checkAvailable
import com.example.sensor_active_.Raspberry_Pages.connectRaspberry
import com.example.sensor_active_.Raspberry_Pages.sketchData
import com.influx.login_functions.Login
import kotlinx.android.synthetic.main.activity_add_gateway.*
import kotlinx.android.synthetic.main.activity_add_gateway.searchLayout
import kotlinx.android.synthetic.main.activity_connect_raspberry.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Overview : AppCompatActivity() {

    val SHARED_PREFS = "IP_Addresses"
    var buttonText: String? = ""
    val SHARED_PREF_ACTIVE_IP = "IP_Active"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        swipeRefresh()


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

    fun swipeRefresh() {
        swipe_refresh.setOnRefreshListener {
            Toast.makeText(this, "page refreshed", Toast.LENGTH_SHORT).show()
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
           // swipe_refresh.isRefreshing = false
        }

    }

    fun addButtons(key: String?, value: String) {
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
                dynamicButton.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.HFUgreen
                    )
                )
            } else {
                dynamicButton.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.darkred
                    )
                )
            }
        }
        newlinLay.addView(dynamicButton)
        dynamicButton.text = ip
        // add Button to LinearLayout

    }

    fun changeActivityToConnect(_ip_address: String) {

        val sharedPreferences = getSharedPreferences(SHARED_PREF_ACTIVE_IP, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val intent = Intent(this, connectRaspberry::class.java)

        editor.putString("IP_Active", _ip_address)
        editor.apply()
        startActivity(intent)
    }

    fun addGateway(view: View) {
        val intent = Intent(this, AddGateway::class.java)
        startActivity(intent)

    }

    fun pageDatabase(view: View) {

        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    fun toSketch(view: View) {
        val intent = Intent(this, sketchData::class.java)
        startActivity(intent)

    }
}