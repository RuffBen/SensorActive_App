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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sensor)
        loadData()

    }


    fun loadData() {
        val sharedPreferences = getSharedPreferences("IP_Active", MODE_PRIVATE)
        activeIP = sharedPreferences.getString("IP_Active", "No IP Address found").toString()
        val delimiter = "///"
        activeIPHTTPS = activeIP.split(delimiter)[0]
        activeIPHTTPS = "https://" + activeIPHTTPS + PORT
        Log.i("SPLITTED STRING", activeIPHTTPS)


    }
    fun searchSensorSerial(view: View) {
        GlobalScope.launch {

            response = PreemtiveAuthSensors(
                activeIPHTTPS,
                "/read_serial_address",
                "demo",
                "demo",
                "",
                "",
                ""
            ).run()
            runOnUiThread{
                searchLayout.removeAllViews()
                //    text_view_result.text = textViewSensors
                //    if (textViewContent.contains("}"))
                if(JSONObject(response).get("success") == true)
                    Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()

                //     callForButtons(response)
                else{
                    Toast.makeText(applicationContext, "no Sensor connected", Toast.LENGTH_SHORT).show()

                }

            }
        }

    }

    fun searchSensorButton(view: View) {
        GlobalScope.launch {

            response = PreemtiveAuthSensors(
                activeIPHTTPS,
                "/search_bluetooth-devices",
                "demo",
                "demo",
                "",
                "",
                ""
            ).run()
            runOnUiThread{
                searchLayout.removeAllViews()
                //    text_view_result.text = textViewSensors
                //    if (textViewContent.contains("}"))
                if(JSONObject(response).get("success") == true)
                    callForButtons(response)
                else{
                    Toast.makeText(applicationContext, "no Sensor found", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
    fun callForButtons(_response:String){
        val responseData = JSONObject(_response).get("data").toString()
        val delimiter = ","
        var responseDataList = responseData.split(delimiter)

        Log.i("respone AddSensor",responseDataList.size.toString())
        for (item in responseDataList){
            addButton(item)
        }

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