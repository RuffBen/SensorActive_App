package com.influx.Graph

import com.influx.dataClasses.InfluxBucket
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import java.util.*
import kotlinx.coroutines.runBlocking

public class influxCommunication {

    public fun getBuckets(link: String, key: String, org: String) : ArrayList<InfluxBucket>
    {
        var buckets = ArrayList<InfluxBucket>()
        val influxDBClient = InfluxDBClientKotlinFactory.create(link, key.toCharArray(), org)

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


    public fun getOrganisations(name: String, password:String){


    }

    public fun countCommas(stringToSearch : String,key : String): Int{
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

    public fun getKey(stringToSearch: String,keyNumber : Int): String{
        var words=stringToSearch.split(',')
        if(words.size<keyNumber){
            return "end"
        }
        return words[keyNumber]
    }
}
