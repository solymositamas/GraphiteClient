package com.tompi.graphiteclient

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_setting_selector.*
import org.slf4j.LoggerFactory

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.tompi.graphiteclient.data.GraphiteSettings
import android.app.Activity



class SettingEditorActivity : AppCompatActivity(){

//    private lateinit var toolbar:Toolbar
    private lateinit var settingsId: String

    companion object {
        private val logger = LoggerFactory.getLogger("SettingEditorActivity")!!
        val SETTING_ID = "SETTING_ID"
        fun createIntent(context: Context, id: String): Intent {
            val i = Intent(context, SettingEditorActivity::class.java)
            i.putExtra(SETTING_ID,id)
            return i
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_editor)
//        setSupportActionBar(toolbar)
//
//        toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)

        settingsId = intent.getStringExtra(SETTING_ID) ?: "noope"
        val graphiteSettings = GraphiteSettings.getSettingsByID(settingsId)
        logger.debug("id: $settingsId - settings: ${graphiteSettings.toString()}")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
        getMenuInflater().inflate(R.menu.menu_editor, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_cancel -> {
            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
            true
        }
        R.id.action_done -> {
            val returnIntent = Intent()
            returnIntent.putExtra("settingsId", settingsId)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
