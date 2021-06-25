package com.influx.Graph

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sensor_active_.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_line_chart.*

class LineChart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)

        //Part1
        val entries = ArrayList<Entry>()

//Part2
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 7f))
        entries.add(Entry(4f, 20f))
        entries.add(Entry(5f, 16f))

//Part3
        val vl = LineDataSet(entries, "My Type")

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
        lineChart.description.text = "Days"
        lineChart.setNoDataText("No forex yet!")

//Part10
        //lineChart.animateX(1800, Easing.EaseInExpo)

//Part11
        //val markerView = CustomMarker(this@ShowForexActivity, R.layout.marker_view)
        //lineChart.marker = markerView
    }
}