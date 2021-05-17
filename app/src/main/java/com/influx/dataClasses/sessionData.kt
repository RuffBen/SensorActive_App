package com.influx.dataClasses


import android.widget.*


/*
This class if for managing account informations and saving them as a kind of session.
If informations arent available this class sends notifikations to the active activity to ask
for the missing input.
 */

class sessionData() {

    companion object Factory {
        fun create():  sessionData =  sessionData()
    }

    val passwordInput= String
    val nameInput= String
    val orgInput= String
    val linkInput= String
    val websiteInput = String

    public fun setBuckets(org: String){


    }

    public fun getBuckets(){

    }


    public fun  getOrgs(){

        communicator.getOrgs()

    }

    public fun  loadOrgs(){


    }
}
