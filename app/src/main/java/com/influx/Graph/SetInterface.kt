package com.influx.Graph

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.sensor_active_.R
import com.example.sensor_active_.Raspberry_Pages.classes.checkAvailable
import com.example.sensor_active_.Raspberry_Pages.connectRaspberry

import com.influx.dataClasses.InfluxBucket
import kotlinx.android.synthetic.main.activity_add_gateway.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SetInterface : AppCompatActivity() {

    var sessionData = com.influx.dataClasses.sessionData



    var listOfInfluxBuckets = ArrayList<InfluxBucket>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        // get reference to button
        val btn_BarChart = findViewById(R.id.buttonBarChart) as Button


        var listOfBuckets =mutableListOf<String>()

        btn_BarChart.setOnClickListener {
            for(bucket in listOfBuckets){
                addBucketButtons(bucket)
            }
        }


        GlobalScope.launch {
            var listOfOrgs  =influxCommunication().getBuckets("SensorActive")

            for(bucket in listOfOrgs){
                listOfBuckets.add(bucket.name)
            }


            println("list listOfInfluxBuckets is loadet${listOfInfluxBuckets.size} ")
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
            GlobalScope.launch {
                addMesurment(dynamicButton.text.toString())
            }
        })

        newlinLay.addView(dynamicButton)
        dynamicButton.text = orgName
        // add Button to LinearLayout

    }

    private fun addMesurment(bucketName: String){
        listOfInfluxBuckets = influxCommunication().getBuckets(bucketName)
        var linLay = findViewById(R.id.searchLayoutBuckets) as LinearLayout
        var newlinLay: LinearLayout = LinearLayout(this)

        influxCommunication().getMesurment(bucketName)


    }


}

