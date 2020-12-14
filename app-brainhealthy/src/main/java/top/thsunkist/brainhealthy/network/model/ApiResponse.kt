package top.thsunkist.brainhealthy.network.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T : Any>(
    @SerializedName("message")
    var message: String?,

    @SerializedName("code")
    var code: Int,

    @SerializedName("data")
    val data: T?
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
