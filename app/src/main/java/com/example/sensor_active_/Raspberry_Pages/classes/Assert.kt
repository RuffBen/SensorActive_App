package com.example.sensor_active_.Raspberry_Pages.classes

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

data class Raspberry(
    val device_name: String?,
    val local_adress: String?,
    val global_adress: String?

)


data class AssertSensor(
    val sensor_name: String? = null,
    val sensor_type: String? = null,
    val sensor_bluetooth_adress: String? = null,
    val helpCharacteristic_UUID: String? = null,
    val dataCharacteristic_UUID: String? = null,
    val sync_interval: String? = null,
    val status: String? = null
) {}

data class SensorsIDs(
    val sensoreins: AssertSensor?,
    val sensorzwei: AssertSensor?,
    val sensorv: AssertSensor?,
    val sensorf: AssertSensor?,
    val sensordrei: AssertSensor?


)

class SensorsIDsFeed(
    val IDs: List<SensorsIDs>?
    )


data class MainOrSensors(
    var main_info: Raspberry? = null,
    var sensors: SensorsIDs?
) {}

data class Assert(
    val sensor_name: String? = null,
    val sensor_type: String? = null,
    val sensor_bluetooth_adress: String? = null,
    val helpCharacteristic_UUID: String? = null,
    val dataCharacteristic_UUID: String? = null,
    val sync_interval: String? = null,
    val status: String? = null

) {}
/*
class AssertDeserializer : JsonDeserializer<Raspberry> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Raspberry {
        json as JsonObject
        val device_name = json.get("device_name").toString()

        val local_adress: String? = json.get("local_adress").asString
        val global_adress = json.get("global_adress").asString


        // val address = if (addressJson.isJsonObject) addressJson.asJsonObject.toString() else addressJson.asString

        return Raspberry(
            device_name,
            local_adress,
            global_adress
        )
    }
}
*/