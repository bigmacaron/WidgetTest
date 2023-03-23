package kr.kro.fatcats.widgettest.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import kotlinx.coroutines.*
import kr.kro.fatcats.widgettest.MainActivity.Companion.IMAGE_INDEX

import kr.kro.fatcats.widgettest.MainActivity.Companion.SHARED_PRES
import kr.kro.fatcats.widgettest.MainActivity.Companion.changeJob
import kr.kro.fatcats.widgettest.MainActivity.Companion.imageList
import kr.kro.fatcats.widgettest.R


class AppWidgetProviderClass : AppWidgetProvider() {

    private val myOnClick1 = "myOnClickTag1"


    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.appwidget_layout)

            val intent = Intent(context, AppWidgetProviderClass::class.java)
            intent.action = myOnClick1
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0, intent, PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(
                R.id.ivWidget,
                pendingIntent
            )
            appWidgetManager.updateAppWidget(appWidgetIds, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (myOnClick1 == intent.action) {
            if(changeJob == null){
                playImage(context)
            }else{
                changeJob?.cancel()
                changeJob = null
            }

        }
        super.onReceive(context, intent)
    }

    private fun playImage(context: Context){
        changeJob = CoroutineScope(Dispatchers.Main).launch {
            Log.e("TAG", "onReceive: ${changeJob.hashCode()}", )
            repeat(5){
                val prefs = context.getSharedPreferences(SHARED_PRES,Context.MODE_PRIVATE)
                val imageIndex = prefs?.getInt(IMAGE_INDEX,0)?:0
                var index = 0

                if(imageIndex != imageList.size - 1) index = imageIndex + 1
                val views = RemoteViews(
                    context.packageName,
                    R.layout.appwidget_layout
                )
                views.setImageViewResource(R.id.ivWidget, imageList[index])
                AppWidgetManager.getInstance(context).updateAppWidget(
                    ComponentName(context, AppWidgetProviderClass::class.java), views
                )
                val editor = prefs.edit()
                editor.putInt(IMAGE_INDEX, index)
                editor.apply()
                delay(500)
            }
        }
    }




}

