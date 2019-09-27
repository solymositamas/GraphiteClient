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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_editor)
    }

}
