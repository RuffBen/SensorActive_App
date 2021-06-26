package com.influx.Graph


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.sensor_active_.R
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_line_chart.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LineChart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)

        val btn_BarChart = findViewById(R.id.buttonBack) as Button
        btn_BarChart.setOnClickListener {
            val intent = Intent(this, SetInterface::class.java)
            startActivity(intent)
        }
        var _measurement =""
        var _bucket =""
        var _divice =""
        val bundle = intent.extras
        if (bundle != null) {
            _measurement = bundle.getString("measurement").toString()
            _bucket = bundle.getString("bucket").toString()
            _divice = bundle.getString("divice").toString()


            println(_measurement ); println(_bucket); println(_divice)
            GlobalScope.launch {
                getSensorList(_measurement, _bucket, _divice)
            }

        }

    }

    fun addSensorButton(value: String, measurement: String, bucket: String, divice: String){
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
                getValueList(value, measurement, bucket, divice)
            }
        })

        newlinLay.addView(dynamicButton)
        dynamicButton.text = value
        // add Button to LinearLayout


    }


    fun addValueList(value: String){
        //define the Parent of the Buttons
        var linLay = findViewById(R.id.searchLayoutValues) as LinearLayout

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

        newlinLay.addView(dynamicButton)
        dynamicButton.text = value
        // add Button to LinearLayout


    }



    fun getSensorList(measurement: String, bucket: String, divice :String){
        println(measurement ); println(bucket); println(divice)

        GlobalScope.launch {
            var measurementList = mutableListOf<String>()
            measurementList = influxCommunication().getMeasurement(divice, bucket, measurement)
            runOnUiThread {
                for (value in measurementList) {

                    addSensorButton(value,  bucket,measurement, divice)

                }
            }
        }

    }

    fun getValueList(measurement: String, bucket: String, divice :String, value:String){
        var measurementList = mutableListOf<String>()
        var graphParameter = ArrayList<String>()
        GlobalScope.launch {
            //fun getValues(diviceName: String,bucketName: String, measurment:String, value:String): ArrayList<String>{
            measurementList = influxCommunication().getValues(divice, bucket, measurement, value)
            runOnUiThread {
                for (data in measurementList) {
                    addValueList(data)
                    graphParameter.add(data)
                    println(data)
                }
                makeGraph(measurement, divice, graphParameter)
            }
        }
    }



    fun makeGraph(valueName: String, beconName: String, values:ArrayList<String>){
        val entries = ArrayList<Entry>()

//Part2
        var i =0f
        for (data in values){
            i= i+ 1f
            entries.add(Entry(i, data.toFloat()))
        }


//Part3
        val vl = LineDataSet(entries, valueName)

//Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.colorLight
        vl.fillAlpha = R.color.colorDark

//Part5
        lineChart.xAxis.labelRotationAngle = 0f

//Part6
        lineChart.data = LineData(vl)

//Part7
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.axisMaximum = entries.count()+0.1f

//Part8
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

//Part9
        lineChart.description.text = beconName
        lineChart.setNoDataText("No forex yet!")

//Part10
        //lineChart.animateX(1800, Easing.EaseInExpo)

//Part11
        //val markerView = CustomMarker(this@ShowForexActivity, R.layout.marker_view)
        //lineChart.marker = markerView
    }
}