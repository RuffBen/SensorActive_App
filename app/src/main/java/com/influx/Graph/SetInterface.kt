package com.influx.Graph

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sensor_active_.R
import com.influx.login_functions.Login
import kotlinx.android.synthetic.main.activity_add_gateway.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SetInterface : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        // get reference to button
        val btn_BarChart = findViewById(R.id.buttonBarChart) as Button

        btn_BarChart.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        GlobalScope.launch {
            var listOfBuckets = mutableListOf<String>()
            var listOfOrgs=influxCommunication().getBuckets()
            for (bucket in listOfOrgs) {
                listOfBuckets.add(bucket.name)
            }
            runOnUiThread {
            for (bucket in listOfBuckets) {
                addBucketButtons(bucket)
            }
            }
        }

    }


    fun addBucketButtons(orgName: String) {

            //define the Parent of the Buttons
            var linLay = findViewById(R.id.searchLayoutOrgs) as LinearLayout

            //adds new layout for button
            var newlinLay= LinearLayout(this)
            // defines button
            val dynamicButton = Button(this)
            linLay.addView(newlinLay)
            // setting layout_width and layout_height using layout parameters
            dynamicButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dynamicButton.setOnClickListener(View.OnClickListener { view ->

                getMeasurementList(dynamicButton.text.toString())

            })

            newlinLay.addView(dynamicButton)
            dynamicButton.text = orgName
            // add Button to LinearLayout

    }


    fun addMesurmentButton(mesurment: String, bucket:String){
        //define the Parent of the Buttons
        var linLay = findViewById(R.id.searchLayoutBuckets) as LinearLayout

        //adds new layout for button
        var newlinLay= LinearLayout(this)
        // defines button
        val dynamicButton = Button(this)
        linLay.addView(newlinLay)
        // setting layout_width and layout_height using layout parameters
        dynamicButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dynamicButton.setOnClickListener(View.OnClickListener { view ->

            GlobalScope.launch {
                getDivicesList(dynamicButton.text.toString(), bucket)

            }
        })

        newlinLay.addView(dynamicButton)
        dynamicButton.text = mesurment
        // add Button to LinearLayout

    }

    fun addDiviceButton(measurement: String, bucket: String,divice: String){
        //define the Parent of the Buttons
        var linLay = findViewById(R.id.searchLayoutDivices) as LinearLayout

        //adds new layout for button
        var newlinLay= LinearLayout(this)
        // defines button
        val dynamicButton = Button(this)
        linLay.addView(newlinLay)
        // setting layout_width and layout_height using layout parameters
        dynamicButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dynamicButton.setOnClickListener(View.OnClickListener { view ->

            val bundle = Bundle()
            bundle.putString("measurement",measurement)
            bundle.putString("bucket",bucket)
            bundle.putString("divice",divice)
            val intent = Intent(this, LineChart::class.java)

            intent.putExtras(bundle)
            startActivity(intent)
        })

        newlinLay.addView(dynamicButton)
        dynamicButton.text = measurement
        // add Button to LinearLayout

    }



    fun getMeasurementList(bucket: String){
        var measurementList = mutableListOf<String>()
        GlobalScope.launch {
            measurementList = influxCommunication().getMesurment(bucket)
            runOnUiThread {
                for (measurement in measurementList) {
                    addMesurmentButton(measurement,bucket)
                }
            }
        }
    }

    fun getDivicesList(divice: String, bucket: String){
        var measurementList = mutableListOf<String>()
        GlobalScope.launch {
            measurementList = influxCommunication().getDivices(divice, bucket)
            runOnUiThread {
                for (measurement in measurementList) {
                    addDiviceButton(measurement, bucket, divice)
                }
            }
        }
    }


}

