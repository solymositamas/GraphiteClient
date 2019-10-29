package com.tompi.graphiteclient

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tompi.graphiteclient.data.GraphiteDataSet
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test

class ParseTest {
    @Test
    fun parseNull() {
        val result = "[{\"target\":\"summarize(tompi.home.furdoszoba_polc.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[27.1,1566676800],[27.1,1566680400]]},{\"target\":\"summarize(tompi.home.haloszoba_ablak.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[27.3,1566676800],[27.1,1566680400]]},{\"target\":\"summarize(tompi.home.haloszoba_virag.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1566676800],[27.3,1566680400]]},{\"target\":\"summarize(tompi.home.kamra.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1566676800],[27.9,1566680400]]},{\"target\":\"summarize(tompi.home.kert.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[26,1566676800],[25.4,1566680400]]},{\"target\":\"summarize(tompi.home.nappali_teglafal.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1566676800],[27.8,1566680400]]},{\"target\":\"summarize(tompi.home.nappali_tv.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1566676800],[27.8,1566680400]]},{\"target\":\"summarize(tompi.home.vera_galeria.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1566676800],[27.6,1566680400]]}]"
        val data =  GraphiteDataSet.CreateFromJson(JSONArray(result))

    }

    //[{"target":"summarize(tompi.home.furdoszoba_polc.temperature, \"1hour\", \"last\")","datapoints":[[23.7,1569823200],[23.7,1569826800]]},{"target":"summarize(tompi.home.haloszoba_ablak.temperature, \"1hour\", \"last\")","datapoints":[[22.8,1569823200],[23,1569826800]]},{"target":"summarize(tompi.home.haloszoba_virag.temperature, \"1hour\", \"last\")","datapoints":[[null,1569823200],[24,1569826800]]},{"target":"summarize(tompi.home.kamra.temperature, \"1hour\", \"last\")","datapoints":[[23.9,1569823200],[23.9,1569826800]]},{"target":"summarize(tompi.home.kert.temperature, \"1hour\", \"last\")","datapoints":[[18.8,1569823200],[19.7,1569826800]]},{"target":"summarize(tompi.home.nappali_teglafal.temperature, \"1hour\", \"last\")","datapoints":[[null,1569823200],[24.3,1569826800]]},{"target":"summarize(tompi.home.nappali_tv.temperature, \"1hour\", \"last\")","datapoints":[[24.8,1569823200],[24.9,1569826800]]},{"target":"summarize(tompi.home.vera_galeria.temperature, \"1hour\", \"last\")","datapoints":[[null,1569823200],[24.1,1569826800]]}]
    //2
    @Test
    fun parseNull2() {
        val result = "[{\"target\": \"summarize(tompi.home.haloszoba_virag.temperature, \\\"1hour\\\", \\\"last\\\")\", \"datapoints\": [[null, 1569823200], [24.0, 1569826800]]}]"
//        val result = "[{\"target\":\"summarize(tompi.home.furdoszoba_polc.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[23.7,1569823200],[23.7,1569826800]]},{\"target\":\"summarize(tompi.home.haloszoba_ablak.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[22.8,1569823200],[23,1569826800]]},{\"target\":\"summarize(tompi.home.haloszoba_virag.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1569823200],[24,1569826800]]},{\"target\":\"summarize(tompi.home.kamra.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[23.9,1569823200],[23.9,1569826800]]},{\"target\":\"summarize(tompi.home.kert.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[18.8,1569823200],[19.7,1569826800]]},{\"target\":\"summarize(tompi.home.nappali_teglafal.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1569823200],[24.3,1569826800]]},{\"target\":\"summarize(tompi.home.nappali_tv.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[24.8,1569823200],[24.9,1569826800]]},{\"target\":\"summarize(tompi.home.vera_galeria.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1569823200],[24.1,1569826800]]}]"

        val simple = "\"[ 2134, 1234]\""
//        val result = "[{ \"asd\": \"asd\", \"vew\" : \"vew\"}]"
        "{ \"firstName\":\"John\" , \"lastName\":\"von Neumann\" } ]}"
        val result2 = "{\"locations\":[{\"asd\": \"asd\", \"vew\" : \"vew\"}]}"

val aaa = JsonObject()
        val a1 = aaa.getAsJsonArray("data")
        val a3 = JSONArray(result)

        val obj = JSONObject(result2)
//val a4 = obj.getJSONArray("target")

        val data = GraphiteDataSet.CreateFromJson(a3, 2)

    }

}
