package me.taosunkist.hello.utility.alarmmanager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import me.taosunkist.hello.ui.notification.OccasionType
import me.taosunkist.hello.ui.notification.ReminderType
import java.util.*

object AlarmManagerUtilities {

    @SuppressLint("SimpleDateFormat")
    private fun setAlarmTime(context: Context, timeInMillis: Long, intent: Intent) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val id = intent.getIntExtra("id", 0)
        val intervalMillis = intent.getLongExtra("intervalMillis", 0)
        val sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val nextTimeInMillis = timeInMillis + intervalMillis

        am.setWindow(AlarmManager.RTC_WAKEUP, nextTimeInMillis, intervalMillis, sender)
    }

    fun cancelAlarm(context: Context, id: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pi)
    }

    /**
     * @param flag            周期性时间间隔的标志,flag = 0 表示一次性的闹钟, flag = 1 表示每天提醒的闹钟(1天的时间间隔),flag = 2
     * 表示按周每周提醒的闹钟（一周的周期性时间间隔）
     * @param hour            时
     * @param minute          分
     * @param id              闹钟的id
     * @param week            week=0表示一次性闹钟或者按天的周期性闹钟，非0 的情况下是几就代表以周为周期性的周几的闹钟
     * @param tips            闹钟提示信息
     */
    @SuppressLint("ObsoleteSdkInt")
    fun setAlarm(
            context: Context,
            flag: Int,
            hour: Int,
            minute: Int,
            id: Int,
            week: Int,
            title: String,
            tips: String,
            reminderType: ReminderType,
            occasionType: OccasionType) {

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance(Locale.CHINESE)
        var intervalMillis: Long = 0
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hour, minute, 0)
        when (flag) {
            0 -> intervalMillis = 0
            1 -> intervalMillis = (24 * 3600 * 1000).toLong()
            2 -> intervalMillis = (24 * 3600 * 1000 * 7).toLong()
        }
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("intervalMillis", intervalMillis)
        intent.putExtra("timeInMillis", calendar.timeInMillis)
        intent.putExtra("hour", hour)
        intent.putExtra("minute", minute)
        intent.putExtra("title", title)
        intent.putExtra("tips", tips)
        intent.putExtra("id", id)
        intent.putExtra("flag", flag)
        intent.putExtra("week", week)
        intent.putExtra("reminderType", reminderType.ordinal)
        intent.putExtra("occasionType", occasionType.ordinal)

        val sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        am.setWindow(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.timeInMillis), intervalMillis, sender)

    }

    /**
     * @param weekflag 传入的是周几
     * @param dateTime 传入的是时间戳（设置当天的年月日+从选择框拿来的时分秒）
     * @return 返回起始闹钟时间的时间戳
     */
    private fun calMethod(weekflag: Int, dateTime: Long): Long {
        var time: Long = 0

        if (weekflag != 0) { /* weekflag == 0表示是按天为周期性的时间间隔或者是一次行的，weekfalg非0时表示每周几的闹钟并以周为时间间隔 */

            val c = Calendar.getInstance()
            var week = c.get(Calendar.DAY_OF_WEEK)
            when (week) {
                1 -> week = 7
                2 -> week = 1
                3 -> week = 2
                4 -> week = 3
                5 -> week = 4
                6 -> week = 5
                7 -> week = 6
            }

            when {
                weekflag == week -> time = if (dateTime > System.currentTimeMillis()) {
                    dateTime
                } else {
                    dateTime + 7 * 24 * 3600 * 1000
                }
                weekflag > week -> time = dateTime + (weekflag - week) * 24 * 3600 * 1000
                weekflag < week -> time = dateTime + (weekflag - week + 7) * 24 * 3600 * 1000
            }

        } else {
            time = if (dateTime > System.currentTimeMillis()) {
                dateTime
            } else {
                dateTime + 24 * 3600 * 1000
            }
        }
        return time
    }

    fun setupNextReminder(intervalMillis: Long, hour: Int, minute: Int, context: Context, intent: Intent) {
        if (intervalMillis != 0L) {
            /* 获取当前的时间 */
            val calendar = Calendar.getInstance()
            /* 省略掉hour/minute/second */
            calendar.timeInMillis = System.currentTimeMillis()
            /* 手动计算下一天的时间, 设置下一次的闹钟 */
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            setAlarmTime(context, calendar.timeInMillis, intent)
        }
    }
}
