package me.taosunkist.hello.data.model

import androidx.annotation.IntDef
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.mooveit.library.Fakeit
import me.taosunkist.hello.data.model.GenderDef.Companion.Female
import me.taosunkist.hello.data.model.GenderDef.Companion.Male
import top.thsunkist.appkit.utility.Debug

@Retention(AnnotationRetention.SOURCE)
@IntDef(Male, Female)
annotation class GenderDef {
    companion object {
        const val Male = 1

        const val Female = 2
    }
}


data class User(
    val userID: String,

    @Bindable
    val nickname: String,

    @Bindable
    val avatar: String,

    @GenderDef
    val gender: Int,

    val age: Int?,
) : BaseObservable() {

    companion object {

        fun fake(): User {
            return User(
                userID = java.util.UUID.randomUUID().toString(),
                nickname = Fakeit.book().author(),
                avatar = Debug.images.random(),
                gender = arrayOf(GenderDef.Male, GenderDef.Female).random(),
                age = (1..100).random(),
            )
        }

    }
}