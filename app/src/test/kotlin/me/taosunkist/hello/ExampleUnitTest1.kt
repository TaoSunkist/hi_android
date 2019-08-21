package me.taosunkist.hello

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest1 {
    @Test
    @Throws(Exception::class)
    fun additionIsCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    @Throws(Exception::class)
    fun testDate() {
        val sdf = SimpleDateFormat("yyyy-MM-dd",Locale.CHINA)
        sdf.calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))

        val sdf1 = SimpleDateFormat("yyyy-MM-dd",Locale.CHINA)
        sdf1.calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))
        sdf1.calendar.add(Calendar.DAY_OF_MONTH, 1)

        println(sdf.format(sdf.calendar.time))
        println(sdf1.format(sdf1.calendar.time))

        println(sdf.calendar.time.before(sdf1.calendar.time))

        sdf.calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))
        println("last open box date : ${sdf1.parse("2019-07-22")}," +
                " current open box date : ${sdf.format(sdf.calendar.time)}, ${sdf1.parse("2019-08-30").before(sdf.calendar.time)}")
    }
}