package me.taosunkist.hello.data.net.model


import androidx.annotation.Keep
import com.mooveit.library.Fakeit
import me.taosunkist.hello.data.model.User
import top.thsunkist.appkit.utility.Debug

@Keep
data class UserDetails(

    val userID: String,

    val age: Int?,

    val avatar: String,

    val nickname: String,

    val gender: Int,

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
    }
}