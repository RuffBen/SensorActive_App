package com.example.sensor_active_.Raspberry_Pages

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sensor_active_.R
import kotlinx.android.synthetic.main.activity_lora_connect2.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


var client = OkHttpClient()

class loraConnect : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lora_connect2)
    }

    fun getLoraData(view: View) {

        GlobalScope.launch {
            client = OkHttpClient.Builder()
                //   .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                //  .hostnameVerifier { _, _ -> true }
                .build()

            var request: Request = Request.Builder()
              //  .url("https://eu1.cloud.thethings.network/api/v3/applications/esp32container/devices?field_mask=name")
                .url("https://eu1.cloud.thethings.network/api/v3/applications")

                .header(
                     //"Authorization","Bearer NNSXS.OIWT3CO2VEZWMNYAYLILAGA3HSVXDYGARC4ASXQ.7RII3ERWUGD6G2EZJ5K6CVY2E6RG4K4RTJPE65VP4WUUKO77YXBQ"
                "Authorization", "Bearer NNSXS.NPFQARHFKU2OEZORWWRVZAFSKM7NLG2NI2HZSTI.YX2OJIV4SI7Q2GBH2CWBRILDH33IVOFBHIG6IMN3QMP4CDO3F6BQ"
                    )
                .build()
        //    request.newBuilder()
              //  .header(
               //     "Authorization","Bearer NNSXS.OIWT3CO2VEZWMNYAYLILAGA3HSVXDYGARC4ASXQ.7RII3ERWUGD6G2EZJ5K6CVY2E6RG4K4RTJPE65VP4WUUKO77YXBQ"
            //    )
           //     .build();

            var response: Response = client.newCall(request).execute()
            runOnUiThread {
                var textResponse = response.body!!.string()

                Log.i("response", textResponse.toString())
                textLora.text = textResponse.toString()
            }
        }


    }



}