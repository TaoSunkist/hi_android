package top.thsunkist.brainhealthy.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @SerializedName("user")
    val user: User,

    @SerializedName("token")
    val token: String,

    @SerializedName("rongyunToken")
    val rongCloudToken: String,

    ) {
    companion object {
        fun fake(): UserResponse {
            return UserResponse(
                user = User.fake(),
                token = "NA",
                rongCloudToken = "N/A"
            )
        }
    }
}

