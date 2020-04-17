package me.taosunkist.hello

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
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

    data class TestGson(val name: String, var isRead: Boolean?)

    @Test
    fun testGson() {
        val json = """{"name":"啊","iRead":true}"""
        val gson = Gson()
        val testGson = TestGson(name = "啊", isRead = false)
        var testGson2: TestGson
        val testGson21 = gson.fromJson("", TestGson::class.java)
//        val raw = gson.toJson(testGson2)
        println(testGson21)
    }

    @Test
    fun testObservable() {
        val s1 = Single.create<Int> {
            Thread.sleep(500)
            it.onSuccess(5)
        }.toObservable()
        val s2 = Single.create<Int> {
            Thread.sleep(2000)
            it.onSuccess(3)
        }.toObservable()

        Observables.zip(s1, s2) { left, right ->
            println("taohui ${left} ${right}")
            left + right
        }.subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(
                {
                    println("${it}")
                },
                {
                    println("${it}")
                }
        ).addTo(CompositeDisposable())
        while (true) {

        }
    }

    @Test
    fun compareVersion() {
        println("2.0.0(1)".substringBefore("("))
        println("${compareVersionNames("1.1.1", "1.1.1")}")
    }

    fun compareVersionNames(oldVersionName: String, newVersionName: String): Int {
        var res = 0

        val oldNumbers = oldVersionName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val newNumbers = newVersionName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val maxIndex = oldNumbers.size.coerceAtMost(newNumbers.size)

        for (i in 0 until maxIndex) {
            val oldVersionPart = Integer.valueOf(oldNumbers[i])
            val newVersionPart = Integer.valueOf(newNumbers[i])

            if (oldVersionPart < newVersionPart) {
                res = -1
                break
            } else if (oldVersionPart > newVersionPart) {
                res = 1
                break
            }
        }

        if (res == 0 && oldNumbers.size != newNumbers.size) {
            res = if (oldNumbers.size > newNumbers.size) 1 else -1
        }

        return res
    }


    data class OrderStatus(val orderStatus: OrderStatusType)

    enum class OrderStatusType(val value: String) {
        @SerializedName("PAID")
        PAID("PAID"),
        @SerializedName("UNPAID")
        UNPAID("UNPAID")
    }


    @Test
    fun testJson() {

        val jsonString = """{"httpStatusCode":200,"data":{"orderStatus":"PAID"},"error":{"code":0,"message":"请求成功"},"timestamp":"2019-10-31 14:13:34"}"""
        val gson = Gson()
        val tatameResponse = gson.fromJson<TatameResponse<OrderStatus>>(jsonString, object : TypeToken<TatameResponse<OrderStatus>>() {}.type)
        println(tatameResponse)
    }

    private var compositeDisposable = CompositeDisposable()

    @Test
    fun testComposite() {

        loop(30)

        while (true) {

        }
    }

    private fun loop(runOnTime: Int) {
        var runOnTimeAlternative = runOnTime
        compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
        Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .subscribe {
                        println("taohui runOnTime: $runOnTime, compositeDisposable: $compositeDisposable, it: $it")
                }.addTo(compositeDisposable)
    }
}