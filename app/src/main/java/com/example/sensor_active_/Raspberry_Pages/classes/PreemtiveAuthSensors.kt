package com.example.sensor_active_.Raspberry_Pages.classes

import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody

class PreemtiveAuthSensors(_url: String, _extension: String, _username: String, _password: String,_device_id:String, _bluetoothAddress:String, _newName:String)  : PreemtiveAuth(_url, _extension,
    _username,
    _password
){
    private val bluetoothAddress = _bluetoothAddress
    private val newName = _newName

    override fun searchBluetoothDevices(_request: Request) : Request {
        //wird eine oder mehrere "data": "ID-code,ID-code,ID-code" zurückgeben, wenn neue geräte verfügbar sind in bluetooth reichweite
        var request = _request
        val formBody: RequestBody = FormBody.Builder()
            .build()
        request = request.newBuilder()
            .post(formBody)
            .build()

        return request
    }

    override fun searchBluetoothSerial(_request: Request) : Request {
        //wird eine "data": "ID-code" zurückgeben, wenn neue geräte verfügbar sind - wenn über serielle schnittstelle verbunden ist
        var request = _request
        val formBody: RequestBody = FormBody.Builder()
            .build()
        request = request.newBuilder()
            .post(formBody)
            .build()

        return request
    }

    override fun addSensor(_request: Request) : Request {
        //gibt success true zurück, wenn sensor hinzugefügt wurde
        var request = _request

        val formBody: RequestBody = FormBody.Builder()
            .add("address", bluetoothAddress)
            .add("new_name", newName)
            .build()
        request = request.newBuilder()
            .post(formBody)
            .build()

        return request
    }


}