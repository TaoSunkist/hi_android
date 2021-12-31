package me.taosunkist.hello.data.net

/* for different base-url parse json */
@Target(AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class GsonConverter(val value: String = "app") {
    companion object {
        const val COMMON = "common"
    }
}