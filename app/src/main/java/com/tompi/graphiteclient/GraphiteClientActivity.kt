package com.tompi.graphiteclient

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.tompi.graphiteclient.data.GraphiteSettingItem

import kotlinx.android.synthetic.main.activity_setting_selector.*
import org.slf4j.LoggerFactory

import android.content.Context

class GraphiteClientActivity : AppCompatActivity(), SettingSelectorFragment.OnListFragmentInteractionListener {
    override fun onListItemEditClicked(id: String, item: GraphiteSettingItem?) {
        logger.debug("id: $id, item: $item")
    }

    companion object {
        private val logger = LoggerFactory.getLogger("GraphiteClientActivity")!!

    }


    override fun onListItemClicked(id: String, item: GraphiteSettingItem?) {
        logger.debug("id: $id, item: $item")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphite_client)
        setSupportActionBar(toolbar)

    }


}
