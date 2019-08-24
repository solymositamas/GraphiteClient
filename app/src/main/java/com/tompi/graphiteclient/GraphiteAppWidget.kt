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

//        private val MyOnClick = "myOnClickTag"
//        protected fun getPendingSelfIntent(context: Context, action: String): PendingIntent {
//            val intent = Intent(context, javaClass)
//            intent.action = action
//            return PendingIntent.getBroadcast(context, 0, intent, 0)
//        }


    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            GraphiteAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId)
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
        if (intent.action == REFRESH_ACTION) {
            val mgr: AppWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetId: Int = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            logger.debug("refreshing: $appWidgetId")
            updateAppWidget(context, mgr, appWidgetId)
        }


    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        if(context != null && appWidgetManager != null) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    internal fun updateAppWidget(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        logger.debug("update")
        val widgetText = GraphiteAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId)
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.graphite_app_widget)

        views.setTextViewText(R.id.appwidget_text, "ooOoOooo")


        val settings = GraphiteSettings.getMap().entries.first().value
        val loader: GraphiteLoader = GraphiteLoader(settings, succes = {
            logger.debug("update success")
//                    views.setTextViewText(R.id.appwidget_text, "ooOoOooo!!!")
            views.setTextViewText(R.id.appwidget_text, it.toString())

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        },
            fail = {
                views.setTextViewText(R.id.appwidget_text, "EEEEE")
            })
        loader.load(context)

        val intent = Intent(context, javaClass)
        intent.action = REFRESH_ACTION
        intent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId)

        val pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.refresh_button,pi)


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }


}

