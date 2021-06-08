package com.example.sensor_active_.Raspberry_Pages.classes

import android.util.Log
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody

class PreemtiveAuthUserData(
    _url: String,
    _extension: String,
    _username: String,
    _password: String,
    _newUsername: String,
    _newPassword: String
) : PreemtiveAuth(
    _url, _extension,
    _username,
    _password
) {
    private val newUsername = _newUsername
    private val newPassword = _newPassword

    override fun change_userdata(_request: Request): Request {
        var request = _request
        Log.i("changeUD", newUsername + " + " + newPassword)
        var formBody: RequestBody = FormBody.Builder()
            .add("old_un", username)
            .add("new_un", newUsername)
            .add("old_pw", password)
            .add("new_pw", newPassword)
            .build();
        request = request.newBuilder()
            .post(formBody)
            .build()

        return request

    }


}