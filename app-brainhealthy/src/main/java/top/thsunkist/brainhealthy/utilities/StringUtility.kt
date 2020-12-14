package top.thsunkist.brainhealthy.utilities

import java.io.Closeable
import java.io.IOException
import java.util.*
import java.util.regex.Pattern
import kotlin.math.min

val String.isDigit: Boolean
    get() = this.matches("\\d+".toRegex())

val String.isValidPhoneNumber: Boolean
    get() = this.isDigit && (this.length == 11 || this.length == 12)

/* 判断是否只包含数字/英文/汉字 */
val String.isLetterDigitOrChinese: Boolean
    get() = !this.matches("^[a-z0-9A-Z\u4e00-\u9fa5]+$".toRegex())

/* 判断是否包含了特殊字符 */
val String.isSpecialCharacter: Boolean
    get() {
        val regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t"
        val p = Pattern.compile(regEx)
        val m = p.matcher(this)
        return m.find()
    }

fun isNewerVersion(current: String, version: String?): Boolean {
    if (version == null) {
        return false
    }
    val left = current.split(".")
    val right = version.split(".")

    for (i in 0 until min(left.size, right.size)) {
        val d1 = left[i].toIntOrNull() ?: 0
        val d2 = right[i].toIntOrNull() ?: 0
        if (d1 > d2) {
            return false
        } else if (d1 < d2) {
            return true
        }
    }
    return if (left.size > right.size) {
        false
    } else {
        false
    }
}

fun formatSecond2Hms(totalSecs: Long): String {
    val hours = totalSecs / 3600;
    val minutes = (totalSecs % 3600) / 60;
    val seconds = totalSecs % 60;

    return String.format("%02dh%02dm%02ds", hours, minutes, seconds);
}

/**
 * 获取数值的位数，例如9返回1，99返回2，999返回3
 *
 * @param number 要计算位数的数值，必须>0
 * @return 数值的位数，若传的参数小于等于0，则返回0
 */
fun getNumberDigits(number: Int): Int {
    return if (number <= 0) 0 else (Math.log10(number.toDouble()) + 1).toInt()
}


fun getNumberDigits(number: Long): Int {
    return if (number <= 0) 0 else (Math.log10(number.toDouble()) + 1).toInt()
}


fun formatNumberToLimitedDigits(number: Int, maxDigits: Int): String? {
    return if (getNumberDigits(number) > maxDigits) {
        val result = StringBuilder()
        for (digit in 1..maxDigits) {
            result.append("9")
        }
        result.append("+")
        result.toString()
    } else {
        number.toString()
    }
}

/**
 * 规范化价格字符串显示的工具类
 *
 * @param price 价格
 * @return 保留两位小数的价格字符串
 */
fun regularizePrice(price: Float): String? {
    return String.format(Locale.CHINESE, "%.2f", price)
}

/**
 * 规范化价格字符串显示的工具类
 *
 * @param price 价格
 * @return 保留两位小数的价格字符串
 */
fun regularizePrice(price: Double): String? {
    return String.format(Locale.CHINESE, "%.2f", price)
}


fun isNullOrEmpty(string: CharSequence?): Boolean {
    return string == null || string.length == 0
}

fun close(c: Closeable?) {
    if (c != null) {
        try {
            c.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun objectEquals(a: Any?, b: Any): Boolean {
    return a === b || a != null && a == b
}

fun constrain(amount: Int, low: Int, high: Int): Int {
    return if (amount < low) low else if (amount > high) high else amount
}

fun constrain(amount: Float, low: Float, high: Float): Float {
    return if (amount < low) low else if (amount > high) high else amount
}

fun Float.format2Bit(): String {
    return String.format("%.2f", this)
}