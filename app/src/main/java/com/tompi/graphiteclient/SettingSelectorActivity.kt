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

class SettingSelectorActivity : AppCompatActivity(), SettingSelectorFragment.OnListFragmentInteractionListener {

    internal var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    companion object {
        private val logger = LoggerFactory.getLogger("SettingSelectorActivity")!!

        private val PREFS_NAME = "com.tompi.graphiteclient.GraphiteAppWidget"
        private val PREF_PREFIX_KEY = "appwidget_"

        internal fun saveSelectedSetting(context: Context, appWidgetId: Int, id: String) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.putString(PREF_PREFIX_KEY + appWidgetId, id)
            prefs.apply()
        }

        internal fun loadSelectedSetting(context: Context, appWidgetId: Int): String {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val id = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
            return id.orEmpty()
        }

        internal fun deleteSelectedSetting(context: Context, appWidgetId: Int) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.remove(PREF_PREFIX_KEY + appWidgetId)
            prefs.apply()
        }
    }


    override fun onListFragmentInteraction(id: String, item: GraphiteSettingItem?) {
        logger.debug("id: $id, item: $item")

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
        resultValue.putExtra("GRAPHITE_SETTING_ID", id)
        saveSelectedSetting(this, mAppWidgetId, id)

        setResult(Activity.RESULT_OK, resultValue)
        finish()


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_selector)
        setSupportActionBar(toolbar)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
    }




/*
    private val PREFS_NAME = "com.example.android.apis.appwidget.ExampleAppWidgetProvider"
    private val PREF_PREFIX_KEY = "prefix_"
    // Write the prefix to the SharedPreferences object for this widget
    fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
        prefs.commit()
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    fun loadTitlePref(context: Context, appWidgetId: Int): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val prefix = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
        return if (prefix != null) {
            prefix
        } else {
            context.getString(R.string.appwidget_prefix_default)
        }
    }
*/

}
