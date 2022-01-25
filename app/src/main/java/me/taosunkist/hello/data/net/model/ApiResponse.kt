package me.taosunkist.hello.data.net.model

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
    }

    fun isOk(): Boolean {
        return code == 200
    }
}
