package com.tompi.graphiteclient

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import org.slf4j.LoggerFactory
import android.app.PendingIntent
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS
import android.widget.Toast
import com.tompi.graphiteclient.data.GraphiteLoader
import com.tompi.graphiteclient.data.GraphiteSettingItem
import com.tompi.graphiteclient.data.GraphiteSettings

const val REFRESH_ACTION = "com.tompi.graphiteclient.REFRESH_ACTION"

class GraphiteAppWidget : AppWidgetProvider() {

    companion object {
        private val logger = LoggerFactory.getLogger("GraphiteClient")!!

    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            logger.debug("updating... $appWidgetId")
            val settingID = SettingSelectorActivity.loadSelectedSetting(context, appWidgetId)
            updateAppWidget(context, appWidgetManager, appWidgetId, settingID)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            SettingSelectorActivity.deleteSelectedSetting(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        logger.debug("onReceive")
        if (intent.action == REFRESH_ACTION) {
            val mgr: AppWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetId: Int = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            logger.debug("onReceive refresh id: $appWidgetId")
            val settingID = SettingSelectorActivity.loadSelectedSetting(context, appWidgetId)
            updateAppWidget(context, mgr, appWidgetId, settingID)
        }


    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        logger.debug("onAppWidgetOptionsChanged")
        if(context != null && appWidgetManager != null) {
            val settingID = SettingSelectorActivity.loadSelectedSetting(context, appWidgetId)
            updateAppWidget(context, appWidgetManager, appWidgetId, settingID)
        }
    }

    internal fun updateAppWidget(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        settingID: String
    ) {
        logger.debug("updateAppWidget id: $appWidgetId, settingID: $settingID")
        val views = RemoteViews(context.packageName, R.layout.graphite_app_widget)

        views.setTextViewText(R.id.appwidget_text, "ooOoOooo $appWidgetId")


        val settings = GraphiteSettings.getSettingsByID(settingID)
        if (settings == null) return

        val loader: GraphiteLoader = GraphiteLoader(settings, succes = {
            logger.debug("update success :$appWidgetId")
            views.setTextViewText(R.id.appwidget_text, it.toString())

            logger.debug("updateAppwidget: $appWidgetId")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        },
            fail = {
                views.setTextViewText(R.id.appwidget_text, "Error: $it")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            })
        loader.load(context)

        val intent = Intent(context, javaClass)
        intent.action = REFRESH_ACTION
        intent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId)

        val pi = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.refresh_button,pi)


        // Instruct the widget manager to update the widget
        logger.debug("updateAppwidget: $appWidgetId")
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }


}

