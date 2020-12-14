package top.thsunkist.brainhealthy.network.model

import com.google.gson.annotations.SerializedName

data class DebugAgoraVideoCallConfig(
    @SerializedName("channel_name")
    var agoraChannelName: String,

    @SerializedName("rong_room_name")
    var rongRoomName: String? = "N/A",

    @SerializedName("from_user_id")
    var fromUserID: String,

    @SerializedName("from_token")
    var fromToken: String,

    @SerializedName("to_token")
    var toToken: String,

    @SerializedName("to_user_id")
    var toUID: String,

    @SerializedName("user_diamonds")
    var userDiamonds: Float,

    @SerializedName("duration")
    var duration: Long,
) {
    companion object {
        fun fake() {

        }
    }
}