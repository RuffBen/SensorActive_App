package com.influx.dataClasses


import android.widget.*
import com.influx.Graph.influxCommunication
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory


/*
This class if for managing account informations and saving them as a kind of session.
If informations arent available this class sends notifikations to the active activity to ask
for the missing input.
 */

class sessionData {

    companion object Factory {
        fun create():  sessionData =  sessionData()
    }

    public val passwordInput= Char
    public val nameInput= String
    public val orgInput= "SensorActive"
    public val keyInput= "qV_WvU2eJbUAvYrTRacX0VsV2SBncwq3kx4FH21z0Vq9XLYfikdqcdG7hialL4_ktpmiHJ2ui945Fq5SyotECQ==".toCharArray()
    public val websiteLinkInput = "https://sensoractive.ddnss.de:8086"

    public val listofTokens = listOf<InfluxBucket>()
    public val organisations = listOf<String>()
    public val buckets = listOf<InfluxBucket>()




}
