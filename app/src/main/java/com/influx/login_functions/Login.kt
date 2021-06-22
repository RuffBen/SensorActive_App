package com.influx.login_functions
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.sensor_active_.R
import com.influx.Graph.BarChartActivity
import com.influx.Graph.SetInterface
import com.influx.dataClasses.sessionData
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Login : AppCompatActivity() {
    private var standardPORT = true

    private lateinit var cbSaveLogin:CheckBox
    private lateinit var websiteLink: EditText
    private lateinit var loginName: EditText
    private lateinit var loginPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.bt_Login)
        cbSaveLogin = findViewById(R.id.checkBoxSaveLogin)

        websiteLink = findViewById(R.id.text_WebsiteLogin)
        loginName = findViewById(R.id.editTextPersonName)
        loginPassword = findViewById(R.id.editTextPassword)

        loadData()
        println("loadWorks")

        btnLogin.setOnClickListener{
            if(cbSaveLogin.isChecked){
               saveData()
            }
            GlobalScope.launch {
                val link = getLink()
                checkLogin(link)
            }
        }

    }

    private fun getLink():String {

        val outStringList = ArrayList<String>()
        var portID = ":8086"
        val portIDAlternativ = findViewById<EditText>(R.id.text_portAlternativ)

        if (!standardPORT){
            portID = portIDAlternativ.text.toString()
        }

        println(websiteLink.text.toString())

        var websiteLinkUse= websiteLink.text.toString()

        if(!websiteLinkUse.contains(".")){

            return "###false###"
        }

        if(!websiteLink.text.toString().contains("http")){
            websiteLinkUse = "https://$websiteLinkUse"
            println(websiteLinkUse)
        }

        if(portID.contains(".*[a-zA-Z].*")&&!standardPORT){
            outStringList.add("###false###")
            return "###false###"
        }

        val out= websiteLinkUse+portID
        println(out)
        return  out
    }


    suspend fun checkLogin(link:String){

        var name= loginName.text.toString()
        var password = loginPassword.text.toString()
        Log.i("LoginData",loginName.text.toString()+"+"+loginPassword.text.toString())
        Log.i("LoginData",name+"+"+password)
        val influxDBClient = InfluxDBClientKotlinFactory.create(link,"qV_WvU2eJbUAvYrTRacX0VsV2SBncwq3kx4FH21z0Vq9XLYfikdqcdG7hialL4_ktpmiHJ2ui945Fq5SyotECQ==".toCharArray(), "SensorActive")
        sessionData.create()
        val fluxQuery = ("buckets()")

        val result=influxDBClient.getQueryKotlinApi().queryRaw(fluxQuery)
        var i=0;
        var more=false
        for(token in result){
            println(token)
            i++
            if(i==2){
                more=true
            }
        }
        if(more) {
            val intent = Intent(this, SetInterface::class.java)
            startActivity(intent)
        }

    }

    fun onRadioButtonClicked(view: View) {

        if (view is RadioButton) {
            val portIDAlternativ = findViewById<EditText>(R.id.text_portAlternativ)
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.RadioB_StandartPort->
                    if (checked) {
                        portIDAlternativ.visibility = View.GONE
                        standardPORT=true
                    }
                R.id.RadioB_differentPort ->
                    if (checked) {
                        portIDAlternativ.setVisibility(View.VISIBLE)
                        standardPORT=false
                    }
            }
        }
    }

    private fun saveData(){
        val sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply{
            putString("WEBSITE", websiteLink.text.toString())
            putString("loginName", loginName.text.toString())
            putString("loginPassword", loginPassword.text.toString())
        }.apply()
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
        println("Data Saved")
    }

    private fun loadData(){

        val sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val loginWebsiteText = sharedPreferences.getString("WEBSITE", "sensoractive.ddnss.de")
        val loginNameText = sharedPreferences.getString("loginName", null)
        val loginPasswordText = sharedPreferences.getString("loginPassword", null)

        websiteLink.setText(loginWebsiteText)
        loginName.setText(loginNameText)
        loginPassword.setText(loginPasswordText)
    }
}