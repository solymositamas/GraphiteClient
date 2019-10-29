package com.tompi.graphiteclient.data

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.tompi.graphiteclient.VolleySingleton
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat

/*

[{"target": "summarize(tompi.home.haloszoba_virag.temperature, \"1hour\", \"last\")", "datapoints": [[null, 1569823200], [24.0, 1569826800]]}]

 */

data class GraphiteDataSet(val targetList: Array<GraphiteDataItem>) {
    private val created: Long = System.currentTimeMillis()
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd : hh-mm-ss")

    companion object {
        private val logger = LoggerFactory.getLogger("GraphiteClient")!!

        fun CreateFromJson(array: JSONArray, targetIdx: Int = -1): GraphiteDataSet? {
            var targetListArray = mutableListOf<GraphiteDataItem>()
            try {
                for (i in 0..(array.length() - 1)) {
                    val item = array.getJSONObject(i)
                    targetListArray.add(parseTarget(item, targetIdx))
                }
            }catch (e:Exception){
                logger.debug("Exception: $e")
                return null
            }
            return GraphiteDataSet(targetListArray.toTypedArray())
        }


        fun parseTarget(array: JSONObject, targetIdx: Int): GraphiteDataItem{
            val data: String = array.getJSONArray("datapoints").getJSONArray(0)[0].toString()
            val value:Double = data.toDoubleOrNull() ?: 0.00001
            val targetName =
                if(targetIdx >= 0) {
                    getTargetName(targetIdx, array.optString("target"))
                } else {
                    array.optString("target")
                }
            return GraphiteDataItem(targetName, value)
        }

        fun getTargetName(targetIdx: Int, result: String): String = result.split(".").get(targetIdx)

    }

    fun getFormattedDate(): String = simpleDateFormat.format(created)

    override fun toString(): String {
        return targetList.joinToString(separator = "\n")
    }
}

data class GraphiteDataItem(val name: String, val value: Double) {
    val decimalFormat = DecimalFormat("#.00")

    val valueString: String = decimalFormat.format(value)

    override fun toString(): String {
        return "$name: ${decimalFormat.format(value)}"
    }
}

class GraphiteLoader(setting: GraphiteSettingItem, val succes: (GraphiteDataSet) -> Unit, val fail: (String) -> Unit) {
    private val GRAPHITE_REQUEST_TAG = "GRAPHITE_REQUEST_TAG"

    lateinit var context: Context

    val requestList: MutableList<JsonArrayRequest> = mutableListOf()
    companion object {
        private val logger = LoggerFactory.getLogger("GraphiteClient")!!
    }


    init {
        setting.servers.forEach {
            logger.debug(it.toString())
            val request = JsonArrayRequest(Request.Method.GET,it.url,null,
                Response.Listener { response ->
                    val data = GraphiteDataSet.CreateFromJson(response, setting.targetIdx)
                    logger.debug(response.toString())
                    if(data == null) {
                        fail("Parse error")
                    } else {
                        VolleySingleton.getInstance(context).cancelAll(GRAPHITE_REQUEST_TAG)
                        succes(data)
                    }
                }, Response.ErrorListener{
                    logger.error(it.toString())
                    fail(it.toString())
                })
            request.tag = GRAPHITE_REQUEST_TAG
            request.retryPolicy = DefaultRetryPolicy( DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,0, 1f )

            requestList.add(request)
        }
    }

    fun load(context: Context) {
        this.context = context
        try {
            requestList.forEach {
                logger.debug("loading: ${it.toString()}")
                VolleySingleton.getInstance(context).addToRequestQueue(it)
            }
        } catch (e: Exception ){
            logger.error(e.toString())
        }
    }
}
