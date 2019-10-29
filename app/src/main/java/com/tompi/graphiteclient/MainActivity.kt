package com.tompi.graphiteclient

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import com.google.gson.JsonArray
import com.tompi.graphiteclient.data.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class MainActivity : AppCompatActivity() {

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
        val recyclerView = findViewById<RecyclerView>(R.id.list_recycler)

        progressBar.visibility = View.VISIBLE
        button.visibility = View.GONE

//        val settings = GraphiteSettingItem(false, server, port, target)
        val settings = GraphiteSettings.getMap().entries.first().value
        logger.debug(settings.servers.toString())
        val loader: GraphiteLoader = GraphiteLoader(settings, succes = {
            logger.debug("data: $it")
            text.setText(it.toString())
            progressBar.visibility = View.GONE
            button.visibility = View.VISIBLE


            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity)

                adapter = GraphiteDataAdapter(it) {
                    //TODO: onclick
                }

            }


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

class GraphiteDataAdapter(private val dataset: GraphiteDataSet, val itemSelected: (GraphiteDataItem) -> Unit) : RecyclerView.Adapter<GraphiteDataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphiteDataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GraphiteDataViewHolder(inflater, parent, itemSelected)
    }

    override fun getItemCount(): Int {
        return dataset.targetList.size
    }

    override fun onBindViewHolder(holder: GraphiteDataViewHolder, position: Int) {
        holder.bind(dataset.targetList[position])
    }

}

class GraphiteDataViewHolder(inflater: LayoutInflater, parent: ViewGroup, val itemSelected: (GraphiteDataItem) -> Unit) : RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_data, parent, false)), View.OnClickListener {
    private var a: TextView? = null
    private var b: TextView? = null
    var data: GraphiteDataItem? = null

    init {
        a = itemView.findViewById(R.id.textName)
        b = itemView.findViewById(R.id.textValue)
    }

    fun bind(holderData: GraphiteDataItem) {
        data = holderData
        itemView.setOnClickListener(this)

        a?.text = data?.name
        b?.text = data?.valueString

        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        data?.let {
            itemSelected(it)
        }
    }
}

