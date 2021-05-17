package com.example.sensor_active_.Raspberry_Pages.classes

import android.util.Log
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class BasicAuthInterceptor(_host: String, _extension: String, _username: String, _password: String) : Interceptor {
    var credentials: String = Credentials.basic(_username, _password)
    var host: String = _host
    var extension: String = _extension
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request();

        // Building the URLs together -adding https:// for the request.url.host and adding extensions
        var requestBuilder = "https://" + request.url.host + ":8888" + extension
        var hostRequestBuiler = host + extension
        Log.i("BasicAuth host", hostRequestBuiler)

        Log.i("Authceck, URLs fitting?", requestBuilder.equals(hostRequestBuiler).toString())


        if (requestBuilder.equals(hostRequestBuiler)) {
            request = request.newBuilder()
                .header("Authorization", credentials)
                .build()
        }
        Log.i("BasicAuth", "returning...")
        return chain.proceed(request)


    }

}
