package com.tompi.graphiteclient

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.*
import com.android.volley.toolbox.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity() {

    //        val url = "https://play.grafana.org/api/datasources/proxy/1/render?target=summarize(apps.fakesite.web_server_01.counters.requests.count,%271hour%27,%27last%27)&from=-1h&format=json"
//        val url = "https://play.grafana.org/api/datasources/proxy/1/render?target=summarize(apps.fakesite.*.counters.requests.count,%271hour%27,%27last%27)&from=-1h&format=json"
    val server = "https://play.grafana.org/api/datasources/proxy/1/"
    val target = "apps.fakesite.*.counters.requests.count"

    companion object {
        private val logger = LoggerFactory.getLogger("GraphiteClient")!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logger.debug("onCreate:")
        val text = findViewById<TextView>(R.id.outText)
        val button = findViewById<Button>(R.id.refreshButton)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        progressBar.visibility = View.VISIBLE
        button.visibility = View.GONE

        val loader: GraphileLoader = GraphileLoader(server, target, succes = {
            logger.debug("data: $it")
            text.setText(it.toString())
            progressBar.visibility = View.GONE
            button.visibility = View.VISIBLE
        }, fail = {
            logger.error("FAIL: $it")
            progressBar.visibility = View.GONE
            button.visibility = View.VISIBLE
        })
        loader.load(this)

        button.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            button.visibility = View.GONE
            loader.load(this)
        }
    }



}

class GraphileLoader(val server:String , val target: String, val succes: (GraphiteData) -> Unit, val fail: (String) -> Unit) {
    val url = String.format("${server}render?target=summarize(${target},'1hour','last')&from=-1h&format=json")
    val request: JsonArrayRequest

    init {

        request = JsonArrayRequest(Request.Method.GET,url,null,
            Response.Listener { response ->
                val data = GraphiteData.CreateFromJson(response, target.split(".").indexOf("*"))
                if(data == null) {
                    fail("Parse error")
                } else {
                    succes(data)
                }
            }, Response.ErrorListener{
                fail(it.toString())
            })

        request.retryPolicy = DefaultRetryPolicy( DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,0, 1f )
    }

    fun load(context: Context) {
        VolleySingleton.getInstance(context).addToRequestQueue(request)
    }
}

data class GraphiteData(val targetList: Array<GraphiteStringTarget>) {
    private val created: Long = System.currentTimeMillis()
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd : hh-mm-ss")

    companion object {
        private val logger = LoggerFactory.getLogger("GraphiteClient")!!

        fun CreateFromJson(array: JSONArray, targetIdx: Int = -1): GraphiteData? {
            var targetListArray = mutableListOf<GraphiteStringTarget>()
            try {
                for (i in 0..(array.length() - 1)) {
                    val item = array.getJSONObject(i)
                    targetListArray.add(parseTarget(item, targetIdx))

                }
            }catch (e:Exception){
                logger.debug("Exception: $e")
                return null
            }
            return GraphiteData(targetListArray.toTypedArray())
        }


        fun parseTarget(array: JSONObject, targetIdx: Int): GraphiteStringTarget{
            val data = array.getJSONArray("datapoints").getJSONArray(0)[0].toString()
            val targetName =
                if(targetIdx >= 0) {
                    getTargetName(targetIdx, array.optString("target"))
                } else {
                    array.optString("target")
                }
            return GraphiteStringTarget(targetName, data)
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

data class GraphiteStringTarget(val name: String, val value: String) {
    override fun toString(): String {
        return "$name: $value"
    }
}
