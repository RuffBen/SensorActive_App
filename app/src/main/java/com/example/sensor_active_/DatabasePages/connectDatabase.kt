package com.example.sensor_active_.DatabasePages


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class connectDatabase : ViewModel() {


    fun main(){

        connect()
    }
    fun connect() = runBlocking{
        val influxDBClient = InfluxDBClientKotlinFactory
                .create(
                        "http://sensoactiv.ddnss.de:8086",
                        "M-ArlTi4OG_V-eo0xBruL7j6WC1wekakW8ii6yZGjqGUjmGSV0nySLfxI9C7YNYghHxM8yxvu9-aXSdw1_ETaw==".toCharArray(),
                        "SensorActiv"
                )
        val fluxQuery = ("from(bucket: \"Test-Bucket\")\n"
                + " |> range(start: -5m)"
                // + " |> filter(fn: (r) => (r[\"_measurement\"] == \"cpu\" and r[\"_field\"] == \"usage_system\"))"
                + " |> sample(n: 5, pos: 1)")

        //Result is returned as a stream
        val results = influxDBClient.getQueryKotlinApi().queryRaw(fluxQuery)

        //print results
        results.consumeEach { println("Line: $it") }

        influxDBClient.close()

    }
}