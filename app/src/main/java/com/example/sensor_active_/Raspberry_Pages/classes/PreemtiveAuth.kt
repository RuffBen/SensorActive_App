package com.example.sensor_active_.Raspberry_Pages.classes

import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


var client = OkHttpClient()

class PreemtiveAuth(_url: String, _extension: String, _username: String, _password: String) {
    var url: String = _url
    var extension: String = _extension
    var username: String = _username
    var password: String = _password

    init {
        //Wenn man kein vertrauenswürdiges certifikat hat: https://stackoverflow.com/questions/25509296/trusting-all-certificates-with-okhttp
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory


        //Build Client
        client = OkHttpClient.Builder()
                .addInterceptor(
                    BasicAuthInterceptor(url, extension, username, password)
                )
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .build()


        //Wenn man ein vertrauenswürdiges Certifikat hat:
        /*
        client = OkHttpClient.Builder()
                .addInterceptor(
                        BasicAuthInterceptor(url, extension, username, password))
            .build()
            */
    }


    fun run(): String {
        var textResponse: String = "Server is not responding - check your IP address and try again"

        var request: Request = Request.Builder()
                .url(url + extension)
                .build()

        when (extension) {
            "/status" -> request = status(request)
            "/change" -> request = changeintervall(request)
            "/sensoractive_gateway" -> request = status(request)

            else -> textResponse = "Unknown extension, please contact an admin on gitHub"
        }
        Log.i("PreemtiveAuth-Method", request.toString())
        try {
            //Hier wird der finale Aufruf abgeschickt
            var response: Response = client.newCall(request).execute()
            textResponse = response.body!!.string()
            var iDS = JSONObject(JSONObject(textResponse).get("data").toString()).get("sensors").toString()
            val jsonObject = JSONObject(iDS)
            val keys: Iterator<String> = jsonObject.keys()
       /*     //loop to get all sensor names
            while (keys.hasNext()) {
                val key = keys.next()
                if (jsonObject.get(key) is JSONObject) {
                    Log.i("iterator", key.toString())
                  //  var addSensor = SensorsIDs(AssertSensor("a", "b", "c", "d", "f", "g", "b"))
                    // ADD keys to dataclass SensorsIDs
                    // add Button
                }
            } */

            //var textViewSensors =  textResponse.split(delimiter)[1]

           // textViewSensors = "{ " + delimiter + textViewSensors.dropLast(1) + " \"\"\""
           // Log.i("DELIMCHECK: ", textViewSensors)
            val gSon = Gson().fromJson<Assert>(textResponse, Assert::class.java)
            // val entity = gson.fromJson(textResponse, AssertFeed::class.java)
           // itemList = gSon.fromJson<List<String>>(itemListJsonString, itemType)

           // Log.i("name1", gSon.data?.main_info?.device_name)
            //Log.i("name1", gSon.data?.sensors?.)


          //  Log.i("name2", gSon[1].toString())
            /** Output student will be like below **/
           // Log.i("name1", parsedJSON.sensors[0].sensor_Name)
            //Log.i("GSON", "filler: " + entity.sensors[1])
           // Log.i("AssertFeed()", AssertFeed(entity.sensors).toString())
           // Assert.assertEquals(entity.description, "Test")


           // val json = JSON().gson(response)
          //  Log.i("Gson", json)


        } catch (e: Exception) {

            Log.i("Preem-Counter: ", "04-Error")

            println(e.toString())
        }
        Log.i("TextResponse ", "Text: " + textResponse)
        return textResponse

    }


    fun status(_request: Request): Request {
        Log.i("StatusPOST", "in Status")
        var request = _request
        request = request.newBuilder()
                .post(FormBody.Builder().build())
                .build()

        return request

    }

    fun changeintervall(_request: Request): Request {
        var request = _request

        var formBody: RequestBody = FormBody.Builder()
                .add("sensor", "m5stack2")
                .add("intervall", "22")
                .add("intervall", "1337")
                .build();
        request = request.newBuilder()
                .post(formBody)
                .build()

        return request

    }
}
