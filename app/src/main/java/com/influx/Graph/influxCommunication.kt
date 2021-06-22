package com.influx.Graph

import android.util.Log
import com.influx.dataClasses.InfluxBucket

import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory

import java.util.*
import kotlinx.coroutines.runBlocking


import com.influxdb.client.domain.Organization

public class influxCommunication {



    fun getBuckets(org: String) : ArrayList<InfluxBucket>
    {

        var buckets = ArrayList<InfluxBucket>()
        //Log.i("LoginData",sessionData.websiteLinkInput+"+"+sessionData.keyInput)
        val influxDBClient = InfluxDBClientKotlinFactory.create("https://sensoractive.ddnss.de:8086","qV_WvU2eJbUAvYrTRacX0VsV2SBncwq3kx4FH21z0Vq9XLYfikdqcdG7hialL4_ktpmiHJ2ui945Fq5SyotECQ==".toCharArray(), "SensorActive")

        runBlocking {

            val fluxQuery = ("buckets()")

            var nameCommas=-1
            var idCommas=-1
            var orgIDCommas=-1

            var foundName=false
            var foundId=false
            var foundOrgId=false

            val result = influxDBClient.getQueryKotlinApi().queryRaw(fluxQuery)
            for(bucketLine in result){

                if(foundName&&foundId&&foundOrgId){
                    var words=bucketLine.split(',')
                    println("The first Bucketline is ${words.size}")
                    if(words.size<nameCommas&&words.size<idCommas&&words.size<orgIDCommas){
                        println("Finish")
                        break
                    }
                    buckets.add(InfluxBucket(words[nameCommas].removePrefix(","),words[idCommas].removePrefix(","),words[orgIDCommas].removePrefix(",")))

                }

                if(!foundName){
                    if(bucketLine.contains("name")){
                        if(countCommas(bucketLine, "name")>-1) {
                            nameCommas = countCommas(bucketLine, "name")
                            foundName=true
                            println("Key Name Found")
                        }
                    }else{
                        println("Key name not Found")
                    }
                }
                if(!foundId){
                    if(bucketLine.contains("id")){
                        if(countCommas(bucketLine, "id")>-1) {
                            idCommas = countCommas(bucketLine, "id")
                            foundId=true
                            println("Key id Found")
                        }
                    }else{
                        println("Key id not Found")
                    }
                }
                if(!foundOrgId){
                    if(bucketLine.contains("organizationID")){
                        if(countCommas(bucketLine, "organizationID")>-1) {
                            orgIDCommas = countCommas(bucketLine, "organizationID")
                            foundOrgId=true
                            println("Key organizationID Found")
                        }
                    }else{
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
            //result = influxDBClient.getOrganizationsApi().findOrganizations().stream()
             //   .findFirst()
             //   .orElseThrow(IllegalStateException::new)


            //for(org in result) {
            //    outList.add(org)

            //}
            return outList
    }

    private fun countCommas(stringToSearch : String, key : String): Int{
        var commas =-1
        var words=stringToSearch.split(',')
        println(words.size)
        for (i in words.indices) {

            if (words[i].contains(key)){
                return i
            }
        }
        return commas
    }

    fun getKey(stringToSearch: String,keyNumber : Int): String{
        var words=stringToSearch.split(',')
        if(words.size<keyNumber){
            return "end"
        }
        return words[keyNumber]
    }



    fun getMesurment(bucketName: String){
        Log.i("MesurmentData",bucketName)
        runBlocking {
            val fluxQuery = ("from(bucket:  \"GatewayData\")\n" +
                    "  |> range(start: v.timeRangeStart, stop: v.timeRangeStop)")


            val influxDBClient = InfluxDBClientKotlinFactory.create("https://sensoractive.ddnss.de:8086","qV_WvU2eJbUAvYrTRacX0VsV2SBncwq3kx4FH21z0Vq9XLYfikdqcdG7hialL4_ktpmiHJ2ui945Fq5SyotECQ==".toCharArray(), "SensorActive")


            val result  = influxDBClient.getQueryKotlinApi().queryRaw(fluxQuery)
            if(result!=null) {
                for (bucketLine in result) {
                    println(bucketLine)
                }
            }

        }
    }

}
