package com.example.sensor_active_.Raspberry_Pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.example.sensor_active_.R
import com.example.sensor_active_.Raspberry_Pages.classes.PreemtiveAuthSensors
import kotlinx.android.synthetic.main.activity_add_gateway.*
import kotlinx.android.synthetic.main.activity_connect_raspberry.*
import kotlinx.android.synthetic.main.activity_connect_raspberry.searchLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddSensor : AppCompatActivity() {

    var activeIP: String = "No IP Address found"
    val PORT = ":8888"
    var activeIPHTTPS = ""
    var response = ""
    val SHARED_PREFS_PW_LIST = "PW_LIST"
    private var username = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sensor)
        setTitle("Add Sensor");
        loadData()

    }


    fun loadData() {
        val sharedPreferences = getSharedPreferences("IP_Active", MODE_PRIVATE)
        activeIP = sharedPreferences.getString("IP_Active", "No IP Address found").toString()
        val delimiter = "///"
        activeIP = activeIP.split(delimiter)[0]
        activeIPHTTPS = "https://" + activeIP + PORT
        val sharedPreferencesPW = getSharedPreferences(SHARED_PREFS_PW_LIST, MODE_PRIVATE)
        var userValue = sharedPreferencesPW.getString(activeIP, "no Userdata found")
        if(userValue == "no Userdata found"){
            Toast.makeText(this, "Please set Logindata on Gateways page!", Toast.LENGTH_SHORT).show()
        }else {
            username = JSONObject(userValue).get("username").toString()
            password = JSONObject(userValue).get("password").toString()

        }


    }
    fun searchSensorSerial(view: View) {
        GlobalScope.launch {
            Log.i("Userdata", "un:" + username + ", " + password)
            response = PreemtiveAuthSensors(
                activeIPHTTPS,
                "/read_serial_address",
                username,
                password,
                "",
                "",
                ""
            ).run()
            runOnUiThread{
                searchLayout.removeAllViews()
                if(response.contains("Invalid credentials")){
                    Toast.makeText(applicationContext, "Wrong Username or Password", Toast.LENGTH_SHORT).show()

                }else {
                    //    text_view_result.text = textViewSensors
                    //    if (textViewContent.contains("}"))
                    if (JSONObject(response).get("success") == true)
                        Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()

                    //     callForButtons(response)
                    else {
                        Toast.makeText(
                            applicationContext,
                            "no Sensor connected",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

            }
        }

    }

    fun searchSensorButton(view: View) {
        progressBar.progress = 25
        GlobalScope.launch {

            response = PreemtiveAuthSensors(
                activeIPHTTPS,
                "/search_bluetooth-devices",
                username,
                password,
                "",
                "",
                ""
            ).run()
            runOnUiThread{
                if(response.contains("Invalid credentials")){
                    Toast.makeText(applicationContext, "Wrong Username or Password", Toast.LENGTH_SHORT).show()
                    progressBar.progress = 0
                }else {
                    progressBar.progress = 50
                    searchLayout.removeAllViews()
                    //    text_view_result.text = textViewSensors
                    //    if (textViewContent.contains("}"))
                    if (JSONObject(response).get("success") == true)
                        callForButtons(response)
                    else {
                        Toast.makeText(applicationContext, "no Sensor found", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
            }
        }
    }
    fun callForButtons(_response:String){
        progressBar.progress = 75
        val responseData = JSONObject(_response).get("data").toString()
        val delimiter = ","
        var responseDataList = responseData.split(delimiter)

        Log.i("respone AddSensor",responseDataList.size.toString())
        for (item in responseDataList){
            addButton(item)
        }
        progressBar.progress = 100
        //loop to get all sensor names



    }


    fun addButton(bluetoothID: String) {

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
            GlobalScope.launch{
            val addResponse = PreemtiveAuthSensors(
                activeIPHTTPS,
                "/add_sensor",
                "demo",
                "demo",
                "",
                dynamicButton.text.toString(),
                "neuerSensor").run()
            runOnUiThread{
                Toast.makeText(applicationContext, addResponse, Toast.LENGTH_SHORT).show()
            }
            }


        })
        newlinLay.addView(dynamicButton)

        dynamicButton.text = bluetoothID
        // add Button to LinearLayout

    }




}