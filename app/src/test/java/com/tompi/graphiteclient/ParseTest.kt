package com.tompi.graphiteclient

import com.tompi.graphiteclient.data.GraphiteDataSet
import org.json.JSONArray
import org.junit.Test

class ParseTest {
    @Test
    fun parseNull() {
        val result = "[{\"target\":\"summarize(tompi.home.furdoszoba_polc.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[27.1,1566676800],[27.1,1566680400]]},{\"target\":\"summarize(tompi.home.haloszoba_ablak.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[27.3,1566676800],[27.1,1566680400]]},{\"target\":\"summarize(tompi.home.haloszoba_virag.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1566676800],[27.3,1566680400]]},{\"target\":\"summarize(tompi.home.kamra.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1566676800],[27.9,1566680400]]},{\"target\":\"summarize(tompi.home.kert.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[26,1566676800],[25.4,1566680400]]},{\"target\":\"summarize(tompi.home.nappali_teglafal.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1566676800],[27.8,1566680400]]},{\"target\":\"summarize(tompi.home.nappali_tv.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1566676800],[27.8,1566680400]]},{\"target\":\"summarize(tompi.home.vera_galeria.temperature, \\\"1hour\\\", \\\"last\\\")\",\"datapoints\":[[null,1566676800],[27.6,1566680400]]}]"
        val data =  GraphiteDataSet.CreateFromJson(JSONArray(result))

    }

}
