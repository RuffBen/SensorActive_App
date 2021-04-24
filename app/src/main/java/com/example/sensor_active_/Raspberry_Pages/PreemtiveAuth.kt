package com.example.sensor_active_.Raspberry_Pages

import android.util.Log
import okhttp3.*
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

var client = OkHttpClient()

class PreemtiveAuth(_url: String, _extension: String, _username: String, _password: String, _POST:Boolean) {
    var url: String = _url
    var extension: String = _extension
    var username: String = _username
    var password: String = _password
    var POST: Boolean = _POST
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
                BasicAuthInterceptor(url, extension, username, password))
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

        var request: Request = Request.Builder()
            .url(url + extension)
            .build()
        var textResponse: String = "Server is not responding - check your IP address and try again"

        //IF POST Request - POST == true
        if(POST) {
            val formBody: RequestBody = FormBody.Builder()
                .add("changeintervall?sensor", "m5stack2")
                .add("intervall", "22")
                    .add("intervall", "1337")

                    .build();


            Log.i("FormBuilder", formBody.toString())

            request = request.newBuilder()
                .post(formBody)
                .build()
        }


        Log.i("PreemtiveAuth-Method", request.toString())


        try {
            //Hier wird der finale Aufruf abgeschickt

            var response: Response = client.newCall(request).execute()
            textResponse = response.body!!.string()

        } catch (e: Exception) {

            Log.i("Preem-Counter: ", "04-Error")

            println(e.toString())
        }
        Log.i("TextResponse ", textResponse)
        return textResponse

    }


}
