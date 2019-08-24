package com.tompi.graphiteclient.data

import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat


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
            val value = data.toDouble()
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

        var arraysString = ""
        targetList.forEach {
            arraysString += it.toString() + "\n"
        }
        return "${getFormattedDate()} \n$arraysString"
    }
}

data class GraphiteDataItem(val name: String, val value: Double) {
    val decimalFormat = DecimalFormat("#.00")

    val valueString: String = decimalFormat.format(value)

    override fun toString(): String {
        return "$name: ${decimalFormat.format(value)}"
    }
}
