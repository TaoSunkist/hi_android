package me.taosunkist.hello.kotlin

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.junit.Test


class KotlinFlowTest {

    @Test
    fun testFlow() {

        flow {

            for (i in 1..5) {
                delay(100)
                emit(i)
                println(i)
            }
        }
    }
}

