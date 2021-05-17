package com.influx.login_functions
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.influx.R


class Login : AppCompatActivity() {
    var standardPORT = true


    internal lateinit var cb_saveLogin:CheckBox
    internal lateinit var websiteLink: EditText
    internal lateinit var loginName: EditText
    internal lateinit var loginPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn_Login = findViewById(R.id.bt_Login) as Button
        cb_saveLogin = findViewById(R.id.checkBoxSaveLogin) as CheckBox

        websiteLink = findViewById(R.id.text_WebsiteLogin) as EditText
        loginName = findViewById(R.id.editTextPersonName) as EditText
        loginPassword = findViewById(R.id.editTextPassword) as EditText

        loadData()
        println("loadWorks")

        btn_Login.setOnClickListener{
            if(cb_saveLogin.isChecked){
               saveData()
            }
            getUserInfos()
        }

    }

    fun getUserInfos():ArrayList<String> {

        var outStringList = ArrayList<String>()
        var portID = "8086"
        var portIDAlternativ = findViewById(R.id.text_portAlternativ) as EditText

        if (!standardPORT){
            portID = portIDAlternativ.text.toString()
        }

        println(websiteLink.text.toString())

        var websiteLinkUse= websiteLink.text.toString()

        if(!websiteLinkUse.contains(".")){
            outStringList.add("###false###")
            return outStringList
        }

        if(!websiteLink.text.toString().contains("http")){
            websiteLinkUse = "https"+ websiteLinkUse
        }

        if(portID.contains(".*[a-zA-Z].*")&&!standardPORT){
            outStringList.add("###false###")
            return outStringList
        }

        println(websiteLinkUse+":"+portID+loginName.text.toString() +loginPassword.text.toString())

        //val influxDBClient = InfluxDBClientKotlinFactory.create(websiteLink.toString()+":"+portID,loginName.toString(),loginPassword.toString().toCharArray())

        return outStringList
    }

    fun onRadioButtonClicked(view: View) {

        if (view is RadioButton) {
            var portIDAlternativ = findViewById(R.id.text_portAlternativ) as EditText
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.RadioB_StandartPort->
                    if (checked) {
                        portIDAlternativ.setVisibility(View.GONE)
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