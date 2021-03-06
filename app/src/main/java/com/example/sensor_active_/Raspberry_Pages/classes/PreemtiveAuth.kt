package com.example.sensor_active_.Raspberry_Pages.classes

import android.util.Log
import okhttp3.*
import org.influxdb.impl.BasicAuthInterceptor

import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


var client = OkHttpClient()

open class PreemtiveAuth(_url: String, _extension: String, _username: String, _password: String) {
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
        // Missing Login
        client = OkHttpClient.Builder()
                .addInterceptor(
                    BasicAuthInterceptor(url, extension ,username, password)
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
            "/change_sensor" -> request = change_sensor(request)
            "/sensoractive_gateway" -> request = status(request)
            "/search_bluetooth-devices" -> request = searchBluetoothDevices(request)
            "/read_serial_address" -> request = searchBluetoothSerial(request)
            "/add_sensor" -> request = addSensor(request)
            "/remove_sensor" -> request = removeSensor(request)
            "/change_ud" -> request = change_userdata(request)

            else -> textResponse = "error"
        }
        Log.i("PreemtiveAuth-Method", request.toString())
        try {
            //Hier wird der finale Aufruf abgeschickt
            var response: Response = client.newCall(request).execute()
            textResponse = response.body!!.string()
            Log.i("TextResponse ", "Text: " + textResponse)
            if(extension == "/status"){

            }
            return textResponse


        } catch (e: Exception) {

            Log.i("Preem-Counter: ", "04-Error")

            println(e.toString())
            Log.i("TextResponse ", "Text: " + textResponse)
            textResponse = "error"
            return textResponse

        }

    }


    fun status(_request: Request): Request {
        Log.i("StatusPOST", "in Status")
        var request = _request
        request = request.newBuilder()
                .post(FormBody.Builder().build())
                .build()

        return request

    }

    open fun change_sensor(_request: Request): Request {
        var request = _request

        val formBody: RequestBody = FormBody.Builder()
                .add("sensor_id", "sensoreins")
                .add("sensor_new_name", "neuernam11e")
                .add("sync_interval", "1337")
                .build();
        request = request.newBuilder()
                .post(formBody)
                .build()

        return request

    }

    open fun searchBluetoothDevices(_request: Request) : Request {
        //wird eine oder mehrere "data": "ID-code,ID-code,ID-code" zurückgeben, wenn neue geräte verfügbar sind in bluetooth reichweite
        return _request
    }
    open fun searchBluetoothSerial(_request: Request) : Request {
        //wird eine "data": "ID-code" zurückgeben, wenn neue geräte verfügbar sind - wenn über serielle schnittstelle verbunden ist
        return _request
    }
    open fun addSensor(_request: Request) : Request {
        //gibt success true zurück, wenn sensor hinzugefügt wurde
        return _request
    }
    open fun removeSensor(_request: Request) : Request {
        //gibt success true zurück, wenn sensor entfernt wurde
        return _request
    }
    open fun change_userdata(_request: Request): Request {
        //gibt success true zurück, wenn Benutzerdaten geändert wurden wurde, benötigt old_pw und new_pw
        return _request
    }
}
