package kr.kro.fatcats.widgettest.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.kro.fatcats.widgettest.MainActivity.Companion.IMAGE_INDEX_CAT
import kr.kro.fatcats.widgettest.MainActivity.Companion.SHARED_PRES
import kr.kro.fatcats.widgettest.MainActivity.Companion.changeJob
import kr.kro.fatcats.widgettest.MainActivity.Companion.imageListCat
import kr.kro.fatcats.widgettest.R


class AppWidgetProviderClass : AppWidgetProvider() {

    private val myOnClick1 = "myOnClickTag1"
    private val myOnClick2 = "myOnClickTag2"

    // 위젯 메타 데이터를 구성 할 때 updatePeriodMillis 라는 업데이트 주기 값을 설정하게 되며, 이 주기에 따라 호출 됩니다.
    // 또한 앱 위젯이 추가 될 떄에도 호출 되므로 Service 와의 상호작용 등의 초기 설정이 필요 할 경우에도 이 메소드를 통해 구현합니다
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
                R.id.ivWidgetMain,
                pendingIntent
            )
//            val intent2 = Intent(context, AppWidgetProviderClass::class.java)
//            intent2.action = myOnClick2
//            intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
//            val pendingIntent2 = PendingIntent.getBroadcast(
//                context,
//                1, intent2, PendingIntent.FLAG_IMMUTABLE
//            )
//
//            views.setOnClickPendingIntent(
//                R.id.ivWidgetSub,
//                pendingIntent2
//            )

            appWidgetManager.updateAppWidget(appWidgetIds, views)
        }
    }

    // 앱의 브로드캐스트를 수신하며 해당 메서드를 통해 각 브로드캐스트에 맞게 메서드를 호출한다.
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
            repeat(500){
                val prefs = context.getSharedPreferences(SHARED_PRES,Context.MODE_PRIVATE)
                val imageIndex = prefs?.getInt(IMAGE_INDEX_CAT,0)?:0
                var index = 0

                if(imageIndex != imageListCat.size - 1) index = imageIndex + 1
                val views = RemoteViews(
                    context.packageName,
                    R.layout.appwidget_layout
                )
                views.setImageViewResource(R.id.ivWidgetMain, imageListCat[index])
                AppWidgetManager.getInstance(context).updateAppWidget(
                    ComponentName(context, AppWidgetProviderClass::class.java), views
                )
                val editor = prefs.edit()
                editor.putInt(IMAGE_INDEX_CAT, index)
                editor.apply()
                delay(16)
            }
        }
    }

    // 앱 위젯은 여러개가 등록 될 수 있는데, 최초의 앱 위젯이 등록 될 때 호출 됩니다. (각 앱 위젯 인스턴스가 등록 될때마다 호출 되는 것이 아님)
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    // onEnabled() 와는 반대로 마지막의 최종 앱 위젯 인스턴스가 삭제 될 때 호출 됩니다
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

    // android 4.1 에 추가 된 메소드 이며, 앱 위젯이 등록 될 때와 앱 위젯의 크기가 변경 될 때 호출 됩니다.
    // 이때, Bundle 에 위젯 너비/높이의 상한값/하한값 정보를 넘겨주며 이를 통해 컨텐츠를 표시하거나 숨기는 등의 동작을 구현 합니다
    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }



    // 이 메소드는 앱 데이터가 구글 시스템에 백업 된 이후 복원 될 때 만약 위젯 데이터가 있다면 데이터가 복구 된 이후 호출 됩니다.
    // 일반적으로 사용 될 경우는 흔치 않습니다.
    // 위젯 ID 는 UID 별로 관리 되는데 이때 복원 시점에서 ID 가 변경 될 수 있으므로 백업 시점의 oldID 와 복원 후의 newID 를 전달합니다
    override fun onRestored(context: Context, oldWidgetIds: IntArray, newWidgetIds: IntArray) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
    }

    // 해당 앱 위젯이 삭제 될 때 호출 됩니다
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
    }





}

