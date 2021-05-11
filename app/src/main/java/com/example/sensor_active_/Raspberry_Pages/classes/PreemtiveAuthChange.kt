package com.example.sensor_active_.Raspberry_Pages.classes

import android.os.Parcel
import android.os.Parcelable
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody

class PreemtiveAuthChange(_url: String, _extension: String, _username: String, _password: String, _sensor_id:String, _sensor_new_name:String, _sync_interval:String)  : PreemtiveAuth(_url, _extension,
    _username,
    _password
) {
    val sensor_id = _sensor_id
    val sync_interval = _sync_interval
    val sensor_new_name = _sensor_new_name
    override fun change_sensor(_request: Request): Request{
        var request = _request

        var formBody: RequestBody = FormBody.Builder()
            .add("sensor_id", sensor_id)
            .add("sensor_new_name", sensor_new_name)
            .add("sync_interval", sync_interval)
            .build();
        request = request.newBuilder()
            .post(formBody)
            .build()

        return request

    }

}