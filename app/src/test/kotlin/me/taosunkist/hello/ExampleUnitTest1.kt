package me.taosunkist.hello

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

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
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        sdf.calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))

        val sdf1 = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        sdf1.calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))
        sdf1.calendar.add(Calendar.DAY_OF_MONTH, 1)

        println(sdf.format(sdf.calendar.time))
        println(sdf1.format(sdf1.calendar.time))

        println(sdf.calendar.time.before(sdf1.calendar.time))

        sdf.calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))
        println("last open box date : ${sdf1.parse("2019-07-22")}," +
                " current open box date : ${sdf.format(sdf.calendar.time)}, ${sdf1.parse("2019-08-30").before(sdf.calendar.time)}")
    }

    @SuppressLint("CheckResult")
    @Test
    fun testRxkotlinRetry() {
        val random = Random(20)
        val counter = AtomicInteger(0)
        var sleepSecond: Long = 2

//        Single.create<Int> {
//            val randomValue = random.nextInt(10)
//            println("current random value: $randomValue")
//            if (randomValue == 10) {
//                it.onSuccess(randomValue)
//            } else {
//                it.onError(RuntimeException("randomValue: $randomValue"))
//            }
//        }.retryWhen { errors ->
//            errors.flatMap {
//                if (counter.incrementAndGet() > 3) {
//                    println(it.localizedMessage)
//                    return Flowable<Throwable>.delay(5,TimeUnit.SECONDS)
//                    return@flatMap Observable.error<Throwable>(it)
//                } else {
//                    println("retry wait for 2 seconds.")
//                    return@flatMap Observable.interval(2000, TimeUnit.SECONDS)
//                }
//            }
//        }.subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe({
//                    println("$it")
//                }, {
//                    println("$it")
//                }).addTo(CompositeDisposable())

        Observable.create<Int> {
            val randomValue = random.nextInt(10)
            println("current random value: $randomValue")
            if (randomValue == 10) {
                it.onNext(randomValue)
            } else {
                it.onError(RuntimeException("randomValue: $randomValue"))
            }
        }.retryWhen { errors ->
            errors.flatMap {
                if (counter.incrementAndGet() > 3) {
                    println(it.localizedMessage + "1111")
                    return@flatMap Observable.error<Throwable>(it)
                } else {
                    println("retry wait for $sleepSecond seconds.")
                    sleepSecond *= 2
                    return@flatMap Observable.timer(sleepSecond, TimeUnit.SECONDS)
                }
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    println("onNext: $it")
                }, {
                    println("onError: $it")
                }).addTo(CompositeDisposable())

        while (counter.get() < 3) {
            println("wait for $counter")
            Thread.sleep(2000)
        }
    }

    @Test
    fun testTime() {
        val currentOfDayTimestamp = beginningOfDayTime(useUTC = true).time

        val currentOfDate = Calendar.getInstance().apply {
            timeInMillis = currentOfDayTimestamp
        }.time

        val oldOfDayTimestamp = beginningOfDayTime(useUTC = true).time
        val oldDayOfDate = Calendar.getInstance().apply {
            timeInMillis = oldOfDayTimestamp
            add(Calendar.DAY_OF_MONTH, -1)
        }.time

        println("$currentOfDate after $oldDayOfDate ${currentOfDate.after(oldDayOfDate)}")
    }

    fun beginningOfDayTime(useUTC: Boolean = false): Date {
        val cal = if (useUTC) Calendar.getInstance(TimeZone.getTimeZone("UTC")) else Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }
}