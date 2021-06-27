package com.influx.Graph


import android.util.Log
import com.influx.dataClasses.InfluxBucket
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

public class influxCommunication {


    fun getBuckets(): ArrayList<InfluxBucket> {

        var buckets = ArrayList<InfluxBucket>()
        //Log.i("LoginData",sessionData.websiteLinkInput+"+"+sessionData.keyInput)
        val influxDBClient = InfluxDBClientKotlinFactory.create(
            "https://sensoractive.ddnss.de:8086",
            "qV_WvU2eJbUAvYrTRacX0VsV2SBncwq3kx4FH21z0Vq9XLYfikdqcdG7hialL4_ktpmiHJ2ui945Fq5SyotECQ==".toCharArray(),
            "SensorActive"
        )

        runBlocking {

            val fluxQuery = ("buckets()")

            var nameCommas = -1
            var idCommas = -1
            var orgIDCommas = -1

            var foundName = false
            var foundId = false
            var foundOrgId = false

            val result = influxDBClient.getQueryKotlinApi().queryRaw(fluxQuery)
            for (bucketLine in result) {

                if (foundName && foundId && foundOrgId) {
                    var words = bucketLine.split(',')
                    println("The first Bucketline is ${words.size}")
                    if (words.size < nameCommas && words.size < idCommas && words.size < orgIDCommas) {
                        println("Finish")
                        break
                    }
                    buckets.add(
                        InfluxBucket(
                            words[nameCommas].removePrefix(","),
                            words[idCommas].removePrefix(","),
                            words[orgIDCommas].removePrefix(",")
                        )
                    )

                }

                if (!foundName) {
                    if (bucketLine.contains("name")) {
                        if (countCommas(bucketLine, "name") > -1) {
                            nameCommas = countCommas(bucketLine, "name")
                            foundName = true
                            println("Key Name Found")
                        }
                    } else {
                        println("Key name not Found")
                    }
                }
                if (!foundId) {
                    if (bucketLine.contains("id")) {
                        if (countCommas(bucketLine, "id") > -1) {
                            idCommas = countCommas(bucketLine, "id")
                            foundId = true
                            println("Key id Found")
                        }
                    } else {
                        println("Key id not Found")
                    }
                }
                if (!foundOrgId) {
                    if (bucketLine.contains("organizationID")) {
                        if (countCommas(bucketLine, "organizationID") > -1) {
                            orgIDCommas = countCommas(bucketLine, "organizationID")
                            foundOrgId = true
                            println("Key organizationID Found")
                        }
                    } else {
                        println("Key organizationID not Found")
                    }
                }
            }
            influxDBClient.close()
        }
        return buckets
    }


    fun getOrganisations(): MutableList<String> {

        var outList = mutableListOf<String>()
        val fluxQuery = ("findOrganizations()")
        val influxDBClient = InfluxDBClientKotlinFactory.create(
            "https://sensoractive.ddnss.de:8086",
            "qV_WvU2eJbUAvYrTRacX0VsV2SBncwq3kx4FH21z0Vq9XLYfikdqcdG7hialL4_ktpmiHJ2ui945Fq5SyotECQ==".toCharArray()
        )

        return outList
    }

    private fun countCommas(stringToSearch: String, key: String): Int {
        var commas = -1
        var words = stringToSearch.split(',')
        println(words.size)
        for (i in words.indices) {

            if (words[i].contains(key)) {
                return i
            }
        }
        return commas
    }

    fun getKey(stringToSearch: String, keyNumber: Int): String {
        var words = stringToSearch.split(',')
        if (words.size < keyNumber) {
            return "end"
        }
        return words[keyNumber]
    }


    fun getMesurment(bucketName: String): ArrayList<String> {
        var gateways = ArrayList<String>()
        runBlocking {
                val fluxQuery = ("from(bucket: \"${bucketName}\")\n"
                        + " |> range(start: -30d)"
                        // + " |> filter(fn: (r) => (r[\"_measurement\"] == \"cpu\" and r[\"_field\"] == \"usage_system\"))"
                        )

                val influxDBClient = InfluxDBClientKotlinFactory.create(
                    "https://sensoractive.ddnss.de:8086",
                    "qV_WvU2eJbUAvYrTRacX0VsV2SBncwq3kx4FH21z0Vq9XLYfikdqcdG7hialL4_ktpmiHJ2ui945Fq5SyotECQ==".toCharArray(),
                    "SensorActive"
                )


                val result = influxDBClient.getQueryKotlinApi().queryRaw(fluxQuery)

                var foundMeasurement =false
                var measurementCommas = -1

                for (bucketLine in result) {

                    if (foundMeasurement) {
                        var words = bucketLine.split(',')
                        println("The measurementline is ${words.size}")

                        if (words.size < measurementCommas ) {
                            println("Finish")
                            break
                        }
                        if(!gateways.contains(words[measurementCommas].removePrefix(","))) {
                            gateways.add(words[measurementCommas].removePrefix(","))
                        }
                    }

                    if (!foundMeasurement) {
                        if (bucketLine.contains("_measurement")) {
                            if (countCommas(bucketLine, "_measurement") > -1) {
                                measurementCommas = countCommas(bucketLine, "_measurement")
                                foundMeasurement = true
                                println("_measurement Found")
                            }
                        } else {
                            println("_measurement not Found")
                        }
                    }
            }
            influxDBClient.close()
        };return gateways
    }


    fun getDivices(diviceName: String,bucketName: String): ArrayList<String> {
        var gateways = ArrayList<String>()
        runBlocking {
            val fluxQuery = ("from(bucket: \"${bucketName}\")\n" +
                    " |> range(start: -30d)" +
                    " |> filter(fn: (r) => r[\"_measurement\"] == \"${diviceName}\")\n"
                    )

            val influxDBClient = InfluxDBClientKotlinFactory.create(
                "https://sensoractive.ddnss.de:8086",
                "qV_WvU2eJbUAvYrTRacX0VsV2SBncwq3kx4FH21z0Vq9XLYfikdqcdG7hialL4_ktpmiHJ2ui945Fq5SyotECQ==".toCharArray(),
                "SensorActive"
            )


            val result = influxDBClient.getQueryKotlinApi().queryRaw(fluxQuery)

            var foundMeasurement =false
            var measurementCommas = -1

            for (bucketLine in result) {

                if (foundMeasurement) {
                    var words = bucketLine.split(',')
                    println("The beacon_name is ${words.size}")

                    if (words.size < measurementCommas ) {
                        println("Finish")
                        break
                    }
                    if(!gateways.contains(words[measurementCommas].removePrefix(","))) {
                        gateways.add(words[measurementCommas].removePrefix(","))
                    }
                }

                if (!foundMeasurement) {
                    if (bucketLine.contains("beacon_name")) {
                        if (countCommas(bucketLine, "beacon_name") > -1) {
                            measurementCommas = countCommas(bucketLine, "beacon_name")
                            foundMeasurement = true
                            println("beacon_name Found")
                        }
                    } else {
                        println("beacon_name not Found")
                    }
                }
            }
            influxDBClient.close()
        };return gateways
    }

    fun getMeasurement(diviceName: String,bucketName: String, measurment:String): ArrayList<String>{

            var meaurments = ArrayList<String>()
            runBlocking {
                val fluxQuery = ("from(bucket: \"${bucketName}\")\n" +
                        " |> range(start: -30d)" +
                        "  |> filter(fn: (r) => r[\"_measurement\"] == \"${diviceName}\")\n"+
                        "|> filter(fn: (r) => r[\"beacon_name\"] == \"${measurment}\")\n"
                        )

                val influxDBClient = InfluxDBClientKotlinFactory.create(
                    "https://sensoractive.ddnss.de:8086",
                    "qV_WvU2eJbUAvYrTRacX0VsV2SBncwq3kx4FH21z0Vq9XLYfikdqcdG7hialL4_ktpmiHJ2ui945Fq5SyotECQ==".toCharArray(),
                    "SensorActive"
                )


                val result = influxDBClient.getQueryKotlinApi().queryRaw(fluxQuery)

                var foundMeasurement =false
                var measurementCommas = -1

                for (bucketLine in result) {

                    if (foundMeasurement) {
                        var words = bucketLine.split(',')
                        println("The beacon_name is ${words.size}")

                        if (words.size < measurementCommas ) {
                            println("Finish")
                            break
                        }
                        if(!meaurments.contains(words[measurementCommas].removePrefix(","))) {
                            meaurments.add(words[measurementCommas].removePrefix(","))
                            println(words[measurementCommas].removePrefix(","))
                        }
                    }

                    if (!foundMeasurement) {
                        if (bucketLine.contains("measurement")) {
                            if (countCommas(bucketLine, "measurement") > -1) {
                                measurementCommas = countCommas(bucketLine, "measurement")+2
                                foundMeasurement = true
                                println("measurement Found")
                            }
                        }else {
                            println("measurement not Found")
                        }
                    }
                }
                influxDBClient.close()
            };return meaurments
    }


    fun getValues(diviceName: String,bucketName: String,  value:String, measurment:String): ArrayList<String>{

        var meaurments = ArrayList<String>()
        runBlocking {
            val fluxQuery = ("from(bucket: \"${bucketName}\")\n" +
                    "|> range(start: -30d)" +
                    "|> filter(fn: (r) => r[\"_measurement\"] == \"${measurment}\")\n"+
                    "|> filter(fn: (r) => r[\"beacon_name\"] == \"${diviceName}\")\n" +
                    "|> filter(fn: (r) => r[\"measurement\"] == \"${value}\")\n"

                    )



            println(" diviceName="+diviceName+" bucketName="+bucketName+" measurment="+measurment+" value="+value)
            val influxDBClient = InfluxDBClientKotlinFactory.create(
                "https://sensoractive.ddnss.de:8086",
                "qV_WvU2eJbUAvYrTRacX0VsV2SBncwq3kx4FH21z0Vq9XLYfikdqcdG7hialL4_ktpmiHJ2ui945Fq5SyotECQ==".toCharArray(),
                "SensorActive"
            )


            val result = influxDBClient.getQueryKotlinApi().queryRaw(fluxQuery)

            var foundMeasurement =false
            var measurementCommas = -1

            for (bucketLine in result) {

                if (foundMeasurement) {
                    var words = bucketLine.split(',')
                    println("_value ${words.size}")

                    if (words.size < measurementCommas ) {
                        println("Finish")
                        break
                    }
                    if(!meaurments.contains(words[measurementCommas].removePrefix(","))) {
                        meaurments.add(words[measurementCommas].removePrefix(","))
                        println(words[measurementCommas].removePrefix(","))
                    }
                }

                if (!foundMeasurement) {
                    if (bucketLine.contains("value")) {
                        if (countCommas(bucketLine, "value") > -1) {
                            measurementCommas = countCommas(bucketLine, "value")
                            foundMeasurement = true
                            println("_value Found")
                        }
                    }else {
                        println("_value not Found")
                    }
                }
            }
            influxDBClient.close()
        };return meaurments
    }

    fun getBattery(){

        val bucketlist=getBuckets()

        for(bucket in bucketlist) {
            val fluxQuery = ("from(bucket: \"${bucket}\")\n" +
                    "|> range(start: -30d)" +
                    "|> filter(fn: (r) => r[\"measurement\"] == \"bat\")"
                    )

        }
    }

}


