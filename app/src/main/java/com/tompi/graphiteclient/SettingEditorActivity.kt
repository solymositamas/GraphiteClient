package com.tompi.graphiteclient

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_setting_selector.*
import org.slf4j.LoggerFactory

import android.content.Context

class SettingEditorActivity : AppCompatActivity(){

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
        setSupportActionBar(toolbar)

        val id = intent.getStringExtra(SETTING_ID) ?: "noope"
        logger.debug("id: $id")
    }



}
