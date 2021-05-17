package com.influx.Graph

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sensor_active_.R

import com.influx.dataClasses.InfluxBucket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GettingGraph : AppCompatActivity() {
    var listOfInfluxBuckets = ArrayList<InfluxBucket>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        // get reference to button
        val btn_BarChart = findViewById(R.id.buttonBarChart) as Button
        val btn_PieChart = findViewById(R.id.buttonPieChart) as Button
        val btn_RadarChart = findViewById(R.id.buttonRadarChart) as Button

        btn_BarChart.setOnClickListener {
            val intent = Intent(this, BarChartActivity::class.java)
            startActivity(intent)
        }

        btn_PieChart.setOnClickListener {
            val intent = Intent(this, PieChartActivity::class.java)
            startActivity(intent)
        }

        btn_RadarChart.setOnClickListener {
            val intent = Intent(this, RadarChartActivity::class.java)
            startActivity(intent)
        }
        GlobalScope.launch {
            //listOfInfluxBuckets = influxCommunication().getBuckets()
            println("list listOfInfluxBuckets is loadet${listOfInfluxBuckets.size} ")
            getSpinnerItems()
        }

    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.daysRadio ->
                    if (checked) {
                        // Pirates are the best
                    }
                R.id.hoursRadio ->
                    if (checked) {
                        // Ninjas rule
                    }
            }
        }
    }

    fun getSpinnerItems(){
        var listOfBucketNames = ArrayList<String>()
        for(bucket in listOfInfluxBuckets){
            listOfBucketNames.add(bucket.name)
        }
        val s = findViewById<View>(R.id.buckets_spinner) as Spinner
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,listOfBucketNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        s.adapter = adapter

    }



}

