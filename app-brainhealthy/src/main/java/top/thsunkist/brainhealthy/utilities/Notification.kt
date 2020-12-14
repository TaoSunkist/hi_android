package top.thsunkist.brainhealthy.utilities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import top.thsunkist.brainhealthy.BrainhealthyApplication
import top.thsunkist.brainhealthy.R
import top.thsunkist.brainhealthy.ui.home.MainActivity
import java.util.concurrent.atomic.AtomicInteger

const val NOTIFICATION_CHANNEL_ID = "top.thsunkist.brainhealthy"

const val ACTION_OF_OPEN_CHAT = "ACTION_OF_OPEN_CHAT"

val NOTIFICATION_MAX_ID: AtomicInteger = AtomicInteger(Int.MAX_VALUE)

fun sendNotice() {
    val notificationManagerCompat = NotificationManagerCompat.from(BrainhealthyApplication.CONTEXT)
    val notificationCompatBuilder =
        NotificationCompat.Builder(
            BrainhealthyApplication.CONTEXT,
            NOTIFICATION_CHANNEL_ID
        )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "${NOTIFICATION_CHANNEL_ID}_channel_random_pairing"
        val description =
            "${NOTIFICATION_CHANNEL_ID}_channel_description_random_pairing"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
        channel.description = description
        notificationManagerCompat.createNotificationChannel(channel)
    }

    notificationCompatBuilder
        .setDefaults(Notification.DEFAULT_ALL)
        .setWhen(System.currentTimeMillis())
        .setContentTitle("uMessage.title")
        .setContentText("uMessage.text")
        .setTicker("uMessage.ticker")
        .setLargeIcon(
            (getDrawable(
                BrainhealthyApplication.CONTEXT.resources, R.mipmap.ic_launcher, 0
            ) as BitmapDrawable).bitmap
        )
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setAutoCancel(true)/* 点击后自动取消 */
        .setContentIntent(makeMoodIntent(context = BrainhealthyApplication.CONTEXT, moodId = R.mipmap.ic_launcher))
        .setVibrate(longArrayOf(0, 1000, 1000, 1000))
        .setLights(Color.GREEN, 1000, 2000)
        .setSmallIcon(R.drawable.ic_status_bar_icon)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        notificationCompatBuilder.color = Color.TRANSPARENT
    }

    notificationManagerCompat.notify(
        NOTIFICATION_MAX_ID.getAndDecrement(),
        notificationCompatBuilder.build()
    )
}

fun makeMoodIntent(context: Context, name: String = "", moodId: Int): PendingIntent {
    return PendingIntent.getActivity(
        context, 10,
        Intent(context, MainActivity::class.java).putExtra(name, moodId),
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}