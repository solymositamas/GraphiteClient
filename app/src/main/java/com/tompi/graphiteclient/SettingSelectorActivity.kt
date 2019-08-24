package com.tompi.graphiteclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.tompi.graphiteclient.data.GraphiteSettingItem

import kotlinx.android.synthetic.main.activity_setting_selector.*
import org.slf4j.LoggerFactory

class SettingSelectorActivity : AppCompatActivity(), SettingSelectorFragment.OnListFragmentInteractionListener {

    companion object {
        private val logger = LoggerFactory.getLogger("SettingSelectorActivity")!!
    }


    override fun onListFragmentInteraction(id: String, item: GraphiteSettingItem?) {
        logger.debug("id: $id, item: $item")
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_selector)
        setSupportActionBar(toolbar)

    }

}
