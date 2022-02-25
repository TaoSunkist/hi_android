package me.taosunkist.hello.data.net.model

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class ApiResponse<T : Any>(

    var message: String?,

    var code: Int,

    val data: T?,
) {
    companion object {
        fun <T : Any> success(data: T): ApiResponse<T> {
            return ApiResponse(
                code = 200,
                data = data,
                message = System.currentTimeMillis().toString()
            )
        }

        fun <T : Any> failure(): ApiResponse<T> {
            return ApiResponse(
                code = 400,
                data = null,
                message = "occurred some of errors"
            )
        }
    }

    fun isOk(): Boolean {
        return code == 200
    }
}
