package me.taosunkist.hello.data.net.model


import androidx.annotation.Keep

@Keep
data class UserInfo(
    val age: Int?,

    val avatar: String?,

    val name: String?,

    val uid: Int?,
)