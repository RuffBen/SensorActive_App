package com.example.sensor_active_.Raspberry_Pages

import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sensor_active_.R
import com.example.sensor_active_.Raspberry_Pages.classes.PreemtiveAuth
import com.example.sensor_active_.Raspberry_Pages.classes.checkAvailable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class sketchData : AppCompatActivity() {
    val SHARED_PREFS_ALL_IP = "IP_Addresses"
    val SHARED_PREFS_IP_STATUS = "IP_STATUS_LIST"
    var currentIP = ""
    var workingSensors = 0
    var totalSensors = 0
    var workingRaspberrys = 0
    var totalRaspberrys = 0
    var sensorsNeedCheckout = 0
    var minSinceLastCheckout: Long = 0
    var viewID = 1
    val SHARED_PREFS_PW_LIST = "PW_LIST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sketch_data)
        setTitle("Overview");

        val sharedPreferencesALL_IP = getSharedPreferences(SHARED_PREFS_IP_STATUS, MODE_PRIVATE)
        val allPrefs = sharedPreferencesALL_IP.all
        for ((key, value) in allPrefs) {
            var thisJSON =
                JSONObject(JSONObject(value.toString()).get("data").toString()).get("sensors")
                    .toString()
            val jsonObject = JSONObject(thisJSON)
            val keys: Iterator<String> = jsonObject.keys()
            //loop to get all sensor names
            while (keys.hasNext()) {
                val keySens = keys.next()
                if (jsonObject.get(keySens) is JSONObject) {
                    totalSensors++
                    var thisSensName =
                        JSONObject(JSONObject(thisJSON).get(keySens).toString()).get("sensor_name")
                            .toString()
                    var last_succ_trans =
                        JSONObject(
                            JSONObject(thisJSON).get(keySens).toString()
                        ).get("last_succ_trans")
                            .toString()

                    getTimeDiff(
                        last_succ_trans,
                        JSONObject(value.toString()).get("time").toString()
                    )


                    var status =
                        JSONObject(JSONObject(thisJSON).get(keySens).toString()).get("status")
                            .toString()
                    if (status == "on") {
                        workingSensors++
                    }
                    var textSenorsTime = "Last checkout was " + minSinceLastCheckout + " min ago"
                    var textSensors =
                        "at that time, " + workingSensors + " from " + totalSensors + " Sensors were online"
                    var textSenorsCheckout =
                        "at that time " + sensorsNeedCheckout + " from " + totalSensors + " Sensors have sent data within the last 15 min"
                    CreateNewTextView(key, textSenorsTime, textSensors, textSenorsCheckout)

                }
            }

        }

        /*val sharedPreferences_IP_STATUS = getSharedPreferences(SHARED_PREFS_IP_STATUS, MODE_PRIVATE)
        Log.i("SHARED PREFS IP_LIST", sharedPreferences_IP_STATUS.getString(currentIP, "no content for this status" + currentIP))
        var Data = sharedPreferences_IP_STATUS.getString(currentIP, "no content for this status" + currentIP)
        visData.text = Data */

    }

    fun getTimeDiff(_lastSuccTrans: String, _lastStatusTime: String) {
        var lastSuccTrans = _lastSuccTrans
        lastSuccTrans = lastSuccTrans.split(".")[0]
        lastSuccTrans = lastSuccTrans.replace(" ", "T")
        Log.i("last succ trans", lastSuccTrans)
        var last_succ_trans_time = LocalDateTime.parse(lastSuccTrans)

        var date = ZonedDateTime.now(ZoneId.of("Europe/Berlin")).toString()
        date = date.split(".")[0]
        var dateTime = LocalDateTime.parse(date)
        var timeDiffTrans: Long = ChronoUnit.MINUTES.between(last_succ_trans_time, dateTime)
        if (timeDiffTrans <= 10) {
            sensorsNeedCheckout++
        }

        var lastStatusTime = _lastStatusTime
        lastStatusTime = lastStatusTime.split(".")[0]
        lastStatusTime = lastStatusTime.replace(" ", "T")
        var lastStatusDateTime = LocalDateTime.parse(lastStatusTime)
        minSinceLastCheckout = ChronoUnit.MINUTES.between(lastStatusDateTime, dateTime)
        /*  Log.i("DATE:", dateTime.toString())
          Log.i("DATE-FORMATTED:", last_succ_trans_time.toString())
          Log.i("DIFF IN MIN", timeDiffTrans.toString())
          Log.i("SINCE LAST CHECKOUT", minSinceLastCheckout.toString()) */

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
        when (checkRaspberryAvailable(IPAddress)) {
            true -> IPAddress = "<font color=#008c58>$IPAddress</font>"
            false -> IPAddress = "<font color=#800000>$IPAddress</font>"

        }
        when (workingSensors) {
            totalSensors -> textSensors = "<font color=#008c58>$textSensors</font>"
            0 -> textSensors = "<font color=#800000>$textSensors</font>"
            else -> {
                textSensors = "<font color=#FFD300>$textSensors</font>"
            }
        }
        when (minSinceLastCheckout) {
            in 0..9 -> textTime = "<font color=#008c58>$textTime</font>"
            in 10..15 -> textTime = "<font color=#FFD300>$textTime</font>"
            else -> {
                textTime = "<font color=#800000>$textTime</font>"

            }
        }
        when (sensorsNeedCheckout) {
            0 -> textSensorsCheckout = "<font color=#800000>$textSensorsCheckout</font>"
            totalSensors -> textSensorsCheckout = "<font color=#008c58>$textSensorsCheckout</font>"
            else -> {
                textSensorsCheckout = "<font color=#FFD300>$textSensorsCheckout</font>"
            }
        }


        // setting text
        textView.setText(
            Html.fromHtml(
                "$viewID.  Gateway_IP: $IPAddress, <br>$textTime, <br> $textSensorsCheckout",
                FROM_HTML_MODE_LEGACY
            )
        )

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

        layout?.addView(textView, layoutParam)
        viewID++


        //  newlinLay.addView(dynamicButton)
        // dynamicButton.text = sensorID + " : " + sensorName
        // add Button to LinearLayout
    }

    fun statusAll(view: View) {
        val sharedPreferencesALL_IP = getSharedPreferences(SHARED_PREFS_IP_STATUS, MODE_PRIVATE)
        val allPrefs = sharedPreferencesALL_IP.all
        GlobalScope.launch {

            for ((key, value) in allPrefs) {
                var username = ""
                var password = ""
                val sharedPreferencesPW = getSharedPreferences(SHARED_PREFS_PW_LIST, MODE_PRIVATE)

                var userValue = sharedPreferencesPW.getString(key, "no Userdata found")
                if (userValue == "no Userdata found") {
                    Toast.makeText(
                        applicationContext,
                        "Please set Logindata on Gateways page for IP: " + key,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    break

                } else {
                    username = JSONObject(userValue).get("username").toString()
                    password = JSONObject(userValue).get("password").toString()


                    var textViewContent =
                        PreemtiveAuth("https://$key:8888", "/status", username, password).run()
                    runOnUiThread {
                        if(textViewContent.contains("Invalid credentials")){
                            Toast.makeText(applicationContext, "Wrong Username or Password for IP: " + key, Toast.LENGTH_SHORT).show()

                        }else {
                            val editor = sharedPreferencesALL_IP.edit()
                            Log.i("added to IP_LIST: ", textViewContent)
                            editor.putString(key, textViewContent)
                            editor.apply()
                        }
                    }
                }
            }

            runOnUiThread {
                finish();
                startActivity(getIntent());
            }
        }

    }

    fun checkRaspberryAvailable(_raspberryIP: String): Boolean? {
        var trueFalse: Boolean? = null
        GlobalScope.launch {
            trueFalse = checkAvailable().isReachable(_raspberryIP, 8888, 1200)
        }
        Thread.sleep(1200)
        if (trueFalse == null) {
            return false
        }
        return trueFalse

    }


}