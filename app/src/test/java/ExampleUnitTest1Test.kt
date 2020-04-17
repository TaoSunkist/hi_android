import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest1Test {

    @Test
    fun testDate() {
        val a = "http://52.81.84.200:8094/api/v1.0.0/staions/RL3H042003250001"
        val a1 = a.substring(a.lastIndexOf("/"), a.length)
        println(a1)
    }
}