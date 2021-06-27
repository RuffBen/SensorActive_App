package com.example.sensor_active_.Raspberry_Pages

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
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
    var viewID = 1

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
    fun CreateNewTextView(
        _IPAddress: String,
        _textTime: String,
        _textSensors: String,
        _textSensorsCheckout: String
    ) {
        var textSensors = _textSensors
        var textTime = _textTime
        var textSensorsCheckout = _textSensorsCheckout
        var IPAddress = _IPAddress
        val layout = findViewById<RelativeLayout>(R.id.root)

        // Create TextView programmatically.
        val textView = TextView(this)
        textView.setId(viewID)

        // setting height and width
        val layoutParam: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (viewID > 1) {
            layoutParam.addRule(RelativeLayout.BELOW, (viewID - 1));
        } else {
            layoutParam.addRule(
                RelativeLayout.BELOW,
                (findViewById<Button>(R.id.statusAll).getId())
            );

        }

        // setting text
        textView.setText(
            Html.fromHtml(
                "$viewID.  Gateway_IP: $IPAddress, <br>$textTime, <br> $textSensorsCheckout",
                Html.FROM_HTML_MODE_LEGACY
            )
        )

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

        layout?.addView(textView, layoutParam)
        viewID++


        //  newlinLay.addView(dynamicButton)
        // dynamicButton.text = sensorID + " : " + sensorName
        // add Button to LinearLayout
    }



}