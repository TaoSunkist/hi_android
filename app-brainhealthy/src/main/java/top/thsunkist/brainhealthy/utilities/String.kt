package top.thsunkist.brainhealthy.utilities

import android.annotation.SuppressLint
import android.text.format.DateFormat
import java.lang.System.currentTimeMillis
import java.math.BigInteger
import java.security.MessageDigest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun getDate(timestampInMillis: Long, timeZone: Locale = Locale.getDefault(), inFormat: String = "yyyy-MM-dd"): String {
    val calendar = Calendar.getInstance(timeZone)
    calendar.timeInMillis = timestampInMillis
    return DateFormat.format(inFormat, calendar).toString()
}

fun getDateInSecs(timestampInSeconds: Long, timeZone: Locale = Locale.getDefault(), inFormat: String = "yyyy-MM-dd"): String {
    return if (timestampInSeconds == 0L) {
        ""
    } else {
        val calendar = Calendar.getInstance(timeZone)
        calendar.timeInMillis = timestampInSeconds * 1000
        DateFormat.format(inFormat, calendar).toString()
    }
}

fun getDateHmsInSecond(timestampInSeconds: Long, timeZone: Locale = Locale.getDefault(), inFormat: String = "yyyy-MM-dd HH:mm"): String {
    return try {
        val calendar = Calendar.getInstance(timeZone)
        calendar.timeInMillis = timestampInSeconds * 1000
        DateFormat.format(inFormat, calendar).toString()
    } catch (e: Throwable) {
        ""
    }

}

fun currentTimeSecs(): Long = currentTimeMillis() / 1000

val constantTimeSecs: Long by lazy { currentTimeMillis() / 1000.toLong() }

@SuppressLint("SimpleDateFormat")
fun findDifference(startDate: String, endDate: String) {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    try {

        val d1: Date = sdf.parse(startDate)
        val d2: Date = sdf.parse(endDate)

        val differenceInTime = d2.time - d1.time

        val differenceInSeconds = ((differenceInTime
                / 1000)
                % 60)
        val differenceInMinutes = ((differenceInTime
                / (1000 * 60))
                % 60)
        val differenceInHours = ((differenceInTime
                / (1000 * 60 * 60))
                % 24)
        val differenceInYears = (differenceInTime
                / (1000L * 60 * 60 * 24 * 365))
        val differenceInDays = ((differenceInTime
                / (1000 * 60 * 60 * 24))
                % 365)

        println("Difference " + "between two dates is: ")
        println("$differenceInYears years, $differenceInDays days, $differenceInHours hours, $differenceInMinutes minutes, $differenceInSeconds seconds")
    } catch (e: ParseException) {
        e.printStackTrace()
    }
}
