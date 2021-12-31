package me.taosunkist.hello.data.net

import com.google.gson.annotations.SerializedName

data class ResponseError(
        @SerializedName("message")
        var message: String?,

        @SerializedName("code")
        var code: Int
)

data class TatameResponse<T : Any>(
    @SerializedName("httpStatusCode") val httpStatusCode: Int,
    @SerializedName("data") val data: T?,
    @SerializedName("error") val error: ResponseError?,
    @SerializedName("timestamp") val timestamp: String
) {
    companion object {
        fun <T : Any> success(data: T): TatameResponse<T> {
            return TatameResponse(httpStatusCode = 200,
                    data = data,
                    error = null,
                    timestamp = System.currentTimeMillis().toString())
        }
    }
}

data class TatameErrorResponse(
    @SerializedName("httpStatusCode") val httpStatusCode: Int,
    @SerializedName("error") val error: ResponseError?,
    @SerializedName("timestamp") val timestamp: String
)
