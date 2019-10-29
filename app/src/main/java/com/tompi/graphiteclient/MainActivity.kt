package com.tompi.graphiteclient

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import com.android.volley.VolleyError
import org.json.JSONObject



class MainActivity : AppCompatActivity() {

    companion object {
        private val logger = LoggerFactory.getLogger("BroadcastActivity")!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://play.grafana.org/api/datasources/proxy/1/render?target=summarize(apps.fakesite.web_server_01.counters.requests.count,%271hour%27,%27last%27)&from=-1h&format=json"
        


    }
}
