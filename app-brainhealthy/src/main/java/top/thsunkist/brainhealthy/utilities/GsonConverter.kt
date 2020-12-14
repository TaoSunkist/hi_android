package top.thsunkist.brainhealthy.utilities

/* for different base-url parse json */
@Target(AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class GsonConverter(val value: String = "faceline") {
    companion object {
        const val COMMON = "common"
    }
}