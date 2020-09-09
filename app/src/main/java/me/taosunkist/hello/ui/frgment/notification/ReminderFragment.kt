package me.taosunkist.hello.ui.frgment.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.flyco.tablayout.SegmentTabLayout
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.google.gson.annotations.SerializedName
import me.taosunkist.hello.R
import me.taosunkist.hello.ui.activity.AppEntranceActivity
import me.taosunkist.hello.utility.alarmmanager.AlarmManagerUtilities
import java.text.SimpleDateFormat
import java.util.*

enum class OccasionType(value: String, val tag: String, val id: Int) {
	DAILY("DAILY", "每天", 1), WEEKDAY("WEEKDAY", "工作日", 2), NONE("NONE", "不重复", 0)
}

enum class ReminderType(val value: String, val tag: String, val reminderSoundUri: String) {
	@SerializedName("REMINDER_GETUP")
	REMINDER_GETUP("REMINDER_GETUP", "起床提醒", "android.resource://com.zhimeng.tatame/raw/reminder_getup"),

	@SerializedName("REMINDER_LUNCH")
	REMINDER_LUNCH("REMINDER_LUNCH", "午饭提醒", "android.resource://com.zhimeng.tatame/raw/reminder_lunch"),

	@SerializedName("REMINDER_DINNER")
	REMINDER_DINNER("REMINDER_DINNER", "晚餐提醒", "android.resource://com.zhimeng.tatame/raw/reminder_dinner"),

	@SerializedName("REMINDER_SLEEP")
	REMINDER_SLEEP("REMINDER_SLEEP", "睡觉提醒", "android.resource://com.zhimeng.tatame/raw/reminder_sleep"),

	@SerializedName("REMINDER_APPOINTMENT")
	REMINDER_APPOINTMENT("REMINDER_APPOINTMENT", "约会提醒", "android.resource://com.zhimeng.tatame/raw/reminder_appointment"),

	@SerializedName("REMINDER_CUSTOM")
	REMINDER_CUSTOM("REMINDER_CUSTOM", "自定义提醒", "");
}

class ReminderFragment : Fragment() {

	private lateinit var segmentTabLayout: SegmentTabLayout
	private lateinit var singleDateAndTimePicker: SingleDateAndTimePicker

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let { }
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_setup_reminder, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		singleDateAndTimePicker = view.findViewById(R.id.view_controller_reminder_detail_single_date_and_time_picker)
		segmentTabLayout = view.findViewById(R.id.view_controller_reminder_detail_status_segment_tab_layout)

		val calendar = Calendar.getInstance(Locale.CHINESE)
		calendar.timeInMillis = System.currentTimeMillis()
		singleDateAndTimePicker.setDayFormatter(SimpleDateFormat("MMMd日 EEE", Locale.CHINESE))
		singleDateAndTimePicker.setCustomLocale(Locale.CHINESE)
		singleDateAndTimePicker.setStepMinutes(1)
		singleDateAndTimePicker.setDefaultDate(calendar.time)
		singleDateAndTimePicker.setIsAmPm(true)

		segmentTabLayout.setTabData(OccasionType.values().map { it.tag }.toTypedArray())

		view.findViewById<View>(R.id.fragment_blank_send_notification_button).setOnClickListener { sendNotificationButtonPressed() }
		view.findViewById<View>(R.id.view_controller_reminder_detail_confirm_image_button).setOnClickListener { confirmButtonPressed() }
		view.findViewById<View>(R.id.view_controller_reminder_detail_delete_image_button).setOnClickListener { deleteButtonPressed() }
		view.findViewById<View>(R.id.view_controller_reminder_detail_cancel_image_button).setOnClickListener { cancelButtonPressed() }
	}

	private fun cancelButtonPressed() {}

	private fun deleteButtonPressed() {}

	private fun confirmButtonPressed() {

		val calendar = Calendar.getInstance().apply { time = singleDateAndTimePicker.date }
		val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
		val minute = calendar.get(Calendar.MINUTE)
		var weekStep = 1
		val reminderType = ReminderType.values().random()
		val reminderAlarmIdSet = HashSet<String>() /* 这里需要对工作日的闹钟ID进行特殊处理  */
		val flag: Int = when (segmentTabLayout.currentTab) {
			OccasionType.NONE.id -> {
				OccasionType.NONE.id
			}
			OccasionType.DAILY.id -> {
				OccasionType.DAILY.id
			}
			OccasionType.WEEKDAY.id -> {  /* 表示按周每周提醒的闹钟（一周的周期性时间间隔） */
				val currentTimeSec = System.currentTimeMillis() / 1000
				var reminderAlarmId = (currentTimeSec).toInt()
				for (i in 0..4) reminderAlarmIdSet.add("${reminderAlarmId++}")
				OccasionType.WEEKDAY.id
			}
			else -> throw IllegalArgumentException("unkonwn argument.")
		}
		val occasionType = when (segmentTabLayout.currentTab) {
			OccasionType.NONE.id -> OccasionType.NONE
			OccasionType.DAILY.id -> OccasionType.DAILY
			OccasionType.WEEKDAY.id -> OccasionType.WEEKDAY
			else -> OccasionType.NONE
		}

		AlarmManagerUtilities.setAlarm(
			context = context!!,
			flag = flag,
			hour = hourOfDay,
			minute = minute,
			id = System.currentTimeMillis().toInt(),
			week = if (reminderAlarmIdSet.size == 1) 0 else weekStep++,/* 设置周一到周五的闹钟 */
			tips = "tips",
			reminderType = reminderType,
			occasionType = occasionType,
			title = "title")
	}

	private fun sendNotificationButtonPressed() {
		val notificationManagerCompat = NotificationManagerCompat.from(context!!)
		val notificationCompatBuilder = NotificationCompat.Builder(context!!, NOTIFICATION_CHANNEL_ID)
		val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context?.packageName + "/" + R.raw.reminder_getup)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val name = "${NOTIFICATION_CHANNEL_ID}_channel_name"
			val description = "${NOTIFICATION_CHANNEL_ID}_channel_description"
			val importance = NotificationManager.IMPORTANCE_HIGH
			val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)

			channel.description = description
			channel.lightColor = Color.GRAY
			channel.description = description
			channel.enableLights(true)
			channel.setSound(soundUri, AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_NOTIFICATION).build())
			notificationManagerCompat.createNotificationChannel(channel)
		}

		notificationCompatBuilder
			.setWhen(System.currentTimeMillis())
			.setContentTitle("title")
			.setContentText("tips")
			.setSubText("subText")
			.setWhen(System.currentTimeMillis())
			.setTicker("ticker")
			.setAutoCancel(true)
			.setPriority(NotificationCompat.PRIORITY_MAX)
			.setLargeIcon((ContextCompat.getDrawable(context!!, R.mipmap.ic_launcher) as BitmapDrawable).bitmap)
			.setDefaults(Notification.DEFAULT_ALL)
			.setAutoCancel(true)/* 点击后自动取消 */
			.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context?.packageName + "/" + R.raw.reminder_getup))
			.setPriority(NotificationCompat.PRIORITY_MAX)
			.setContentIntent(makeMoodIntent(context!!, R.mipmap.ic_launcher))

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { /* https://material.io/resources/icons/?search=notification&icon=notifications_none&style=baseline */
			notificationCompatBuilder.setSmallIcon(R.drawable.ic_stat_ta)
		} else {
			notificationCompatBuilder.setSmallIcon(R.drawable.ic_stat_ta)
		}

		notificationCompatBuilder.color = Color.TRANSPARENT
		notificationManagerCompat.notify(id, notificationCompatBuilder.build())
	}

	companion object {

		const val TAG: String = "Notification"
		const val NOTIFICATION_CHANNEL_ID = "com.zhimeng.tatame"

		@JvmStatic
		fun newInstance() = ReminderFragment().apply { arguments = Bundle().apply { } }
	}


	private fun makeMoodIntent(context: Context, moodId: Int): PendingIntent {
		return PendingIntent.getActivity(context, 0,
			Intent(context, AppEntranceActivity::class.java).putExtra("", moodId),
			PendingIntent.FLAG_UPDATE_CURRENT)
	}
}
