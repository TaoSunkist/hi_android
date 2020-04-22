package me.taosunkist.hello.algorithm

import org.junit.Test
import kotlin.random.Random

object Example {
    /* 比较 */
    fun less(i: Comparable<Number>, j: Number) {
        i < j
    }

    /* 排序 */
    fun sort() {
        TODO("具体的排序实现")
    }

    /* 交换 */
    fun swap(array: Array<Number>, j: Int, k: Int) {
        var jVal = array[j]
        array[j] = array[k]
        array[k] = jVal
    }
}

class SortTest {
    /* 每次获取未排序的子序列中的最小值, 进行比较位置交换 */
    @Test
    @Throws(Exception::class)
    fun selectionSort() {
        val array = arrayOfNulls<Int>(10)

        for (i in 0 until 2) {
            array[i] = Random.nextInt(20)
        }

        println(array.contentToString())
        for (i in array.indices) {
            var min = i
            var j: Int = i + 1
            while (j < array.size) {
                if (array[j]!! < array[min]!!) {
                    min = j
                }
                j++
            }
            val minVal = array[min]
            array[min] = array[i]
            array[i] = minVal
        }
        val iterator = array.iterator()
        while (iterator.hasNext()) {
            println(iterator.next())
        }
    }
}
