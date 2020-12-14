package top.thsunkist.brainhealthy.utilities

class Optional<T>(t: T? = null) {

    companion object {
        fun <T> ofNull(): Optional<T> {
            return Optional()
        }
    }

    var value: T? = t

    val exists: Boolean
        get() = value != null

}

inline fun <T : Any> optionalBuilder(crossinline builder: () -> T) =
    object : OptionalBuilder<T>() {
        override fun buildValue(): T = builder()
    }

abstract class OptionalBuilder<T : Any> {
    var value: T? = null
        private set

    val exists: Boolean get() = value != null

    fun build(): T {
        val it = buildValue()
        this.value = it
        return it
    }

    fun getOrBuild(): T = value ?: build()
    fun clear() {
        value = null
    }

    protected abstract fun buildValue(): T
    inline fun <Ta> ifExists(function: (T) -> Ta): Ta? {
        val v = value
        return if (v != null) {
            function(v)
        } else null
    }
}