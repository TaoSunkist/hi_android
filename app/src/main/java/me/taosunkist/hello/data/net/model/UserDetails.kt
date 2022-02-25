package me.taosunkist.hello.data.net.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.mooveit.library.Fakeit
import me.taosunkist.hello.data.model.User
import top.thsunkist.appkit.utility.Debug

@Keep
data class UserDetails(

    @SerializedName("user_id")
    val userID: String,

    @SerializedName("age")
    val age: Int?,

    @SerializedName("avatar")
    val avatar: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("gender")
    val gender: Int,

    @SerializedName("signature")
    var signature: String? = null,
) {

    companion object {

        fun fake(userInfo: User): UserDetails {
            return UserDetails(
                userID = userInfo.userID,
                age = userInfo.age,
                avatar = userInfo.avatar,
                nickname = userInfo.nickname,
                gender = userInfo.gender,
                signature = Fakeit.book().genre()
            )
        }

        fun fake(): UserDetails {
            val userInfo: User = User.fake()
            return UserDetails(
                userID = userInfo.userID,
                age = userInfo.age,
                avatar = userInfo.avatar,
                nickname = userInfo.nickname,
                gender = userInfo.gender,
                signature = Fakeit.book().genre()
            )
        }
    }
}