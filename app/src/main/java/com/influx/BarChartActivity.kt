package com.influx

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class BarChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart)



        val barChart = findViewById(R.id.barChart) as BarChart

        setBarChart(barChart)

    }

    fun setBarChart(barChart: BarChart){
        val cpu =arrayListOf<BarEntry>()

        cpu.add(BarEntry(2014f, 440f))
        cpu.add(BarEntry(2015f, 325f))
        cpu.add(BarEntry(2016f, 245f))
        cpu.add(BarEntry(2017f, 467f))

        val barDataSet = BarDataSet(cpu,"Tester im Einsatz")
        barDataSet.setColor(ColorTemplate.getHoloBlue())
        barDataSet.setValueTextColor(Color.BLACK)
        barDataSet.setValueTextSize(16f)

        val barData = BarData(barDataSet)
        barChart.setFitBars(true)
        barChart.setData(barData)
        barChart.getDescription().setText("Bar Chat Test")
        barChart.animateY(2000)
    }
}