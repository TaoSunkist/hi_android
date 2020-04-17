package me.taosunkist.hello.utility.alarmmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import me.taosunkist.hello.R
import me.taosunkist.hello.ui.AppEntranceActivity

class AlarmReceiver : BroadcastReceiver() {

    companion object {

        const val TAG = "tagAlarmReceiver"
        const val NOTIFICATION_CHANNEL_ID = "com.zhimeng.tatame"

        fun sendNotification(context: Context,
                             id: Int,
                             title: String?,
                             timeInMillis: Long,
                             tips: String?) {

            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val notificationCompatBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            val pendingIntent = PendingIntent.getActivity(context, id,
                    Intent(context, AppEntranceActivity::class.java)
                            .putExtra(TAG, R.mipmap.ic_launcher)
                            .putExtra("title", title)
                            .putExtra("timeInMillis", timeInMillis)
                            .putExtra("tips", tips),
                    PendingIntent.FLAG_UPDATE_CURRENT)

            setupNotificationChannel(notificationManagerCompat)

            notificationCompatBuilder
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(title)
                    .setContentText(tips)
                    .setTicker(title)
                    .setLargeIcon((getDrawable(context, R.mipmap.ic_launcher) as BitmapDrawable).bitmap)
                    .setAutoCancel(true)/* 点击后自动取消 */
                    .setContentIntent(pendingIntent)
            val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context?.packageName + "/" + R.raw.reminder_getup)

//            if (reminderSoundUri.isNotEmpty()) {
                notificationCompatBuilder.setSound(soundUri)
//            } else {
//                notificationCompatBuilder.setDefaults(Notification.DEFAULT_ALL)
//            }

            notificationCompatBuilder.priority = NotificationCompat.PRIORITY_MAX
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                /*
                    Android 中的smallicon有一定的自定义和限制, 现有的测试得出的结论是
                    无法显示多通道的的图片
                    无法显示背景颜色不透明的图片
                    https://material.io/resources/icons/?search=notification&icon=notifications_none&style=baseline
                */
                notificationCompatBuilder.setSmallIcon(R.drawable.baseline_notifications_none_white_18)
                notificationCompatBuilder.color = Color.TRANSPARENT
            } else {
                notificationCompatBuilder.setSmallIcon(R.drawable.baseline_notifications_none_white_18)
            }
            notificationManagerCompat.notify(id, notificationCompatBuilder.build())
        }

        private fun setupNotificationChannel(notificationManagerCompat: NotificationManagerCompat) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "${NOTIFICATION_CHANNEL_ID}_channel_name"
                val description = "${NOTIFICATION_CHANNEL_ID}_channel_description"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
                channel.description = description
                notificationManagerCompat.createNotificationChannel(channel)
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {

        val id = intent.getIntExtra("id", R.layout.activity_app_entrance)
        val title = intent.getStringExtra("title")
        val tips = intent.getStringExtra("tips")
        val intervalMillis = intent.getLongExtra("intervalMillis", 0)
        val hour = intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val timeInMillis = intent.getLongExtra("timeInMillis", 0)

        /* 向系统设置下一次的闹钟 */
        AlarmManagerUtilities.setupNextReminder(intervalMillis, hour, minute, context, intent)
        /* 向系统发送闹钟提醒通知 */
        sendNotification(
                context = context,
                id = id,
                title = title,
                timeInMillis = timeInMillis,
                tips = tips
        )
    }
}
