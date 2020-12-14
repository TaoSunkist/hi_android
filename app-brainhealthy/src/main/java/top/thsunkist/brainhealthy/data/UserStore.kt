package top.thsunkist.brainhealthy.data

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.jakewharton.rxrelay2.BehaviorRelay
import top.thsunkist.brainhealthy.data.model.User
import top.thsunkist.brainhealthy.data.service.UserService
import top.thsunkist.brainhealthy.utilities.*
import com.tencent.mmkv.MMKV
import top.thsunkist.brainhealthy.BrainhealthyApplication
import top.thsunkist.brainhealthy.data.UserStore.Companion.shared

enum class UserObjectEventType {
    /**
     * just only user via AuthenticationVC login, UserObjectEventType the value will became LOGIN
     * @see top.thsunkist.brainhealthy.ui.authentication.AuthenticationViewController.login
     */
    LOGIN,

    /* refreshed user information */
    REFRESH,

    /* include FillUserData and NeighborhoodsNote */
    AUTHENTICATE,

    /* user auto logout or server post block-status */
    LOGOUT
}

data class UserObjectEvent(
    val user: User?,
    val timestamp: Long,
    val type: UserObjectEventType,
) {
    /* all data was ready! */
    val isAuthenticated: Boolean
        get() = user != null && user.isCompleteMaterial()
}

class UserStore internal constructor(private val context: Context) {

    companion object {

        /* user json cache */
        private const val keyUser = "keyUser"

        /* access server api token */
        private const val keyAuthToken = "keyAuthToken"

        lateinit var shared: UserStore

        fun init(context: Context) {
            shared = UserStore(context)
        }
    }

    var user: User? = null
        private set

    var authenticationToken: String? = null
        set(value) {
            field = value
            MMKV.defaultMMKV().putString(keyAuthToken, field)
        }
        get() = if (user != null) {
            val field = MMKV.defaultMMKV().getString(keyAuthToken, field)
            field
        } else {
            null
        }

    val isAuthenticated: Boolean
        get() {
            return user != null
                    && user?.isCompleteMaterial() == true
        }

    val isVip: Boolean
        get() {
            return true
        }

    val userObjectRelay = BehaviorRelay.create<UserObjectEvent>()

    init {
        val rawUser = MMKV.defaultMMKV().getString(keyUser, null)
        if (rawUser == null) {
            /* Not logged in */
        } else {
            try {
                user = BrainhealthyApplication.GSON.fromJson(rawUser, User::class.java)
                when {
                    user == null -> {
                        authenticationToken = null
                    }
                    authenticationToken == null -> {
                        user = null
                    }
                }
            } catch (e: Exception) {
                printf(e)
                /* Cannot decode json, consider as not logged in */
            }
        }
        userObjectRelay.accept(
            UserObjectEvent(
                user = user,
                timestamp = System.currentTimeMillis(),
                type = UserObjectEventType.REFRESH
            )
        )
    }

    private fun persistUserObject() {
        user?.let { user ->
            val rawUser = BrainhealthyApplication.GSON.toJson(user)
            MMKV.defaultMMKV().putString(keyUser, rawUser)
        }
    }

    /**
     * @param shouldSilence Does dispatch to all observers.
     */
    fun refreshUserInfo(
        user: User?,
        token: String? = null,
        type: UserObjectEventType = UserObjectEventType.REFRESH,
        shouldSilence: Boolean = false,
    ) {

        this.user = user
        /* refresh userinfo for rongcloud-service */

        if (type == UserObjectEventType.AUTHENTICATE) {
            if (token == null)
                throw RuntimeException("user event is login, but is empty of token the value")
            else
                this.authenticationToken = token
        }

        persistUserObject()
        if (shouldSilence.not()) {
            this.user?.let {
                userObjectRelay.accept(
                    UserObjectEvent(
                        user = it,
                        timestamp = System.currentTimeMillis(),
                        type = type
                    )
                )
            }
        }
    }

    fun logout() {
        NotificationManagerCompat.from(BrainhealthyApplication.CONTEXT).cancelAll()
        MMKV.defaultMMKV().clearAll()
        user = null
        authenticationToken = null
        userObjectRelay.accept(
            UserObjectEvent(
                user = null,
                timestamp = System.currentTimeMillis(),
                type = UserObjectEventType.LOGOUT
            )
        )
    }

    fun fetchUserDetailInfo(shouldSilence: Boolean = false) {
        if (isAuthenticated) {
            UserService.shared.fetchUserDetailInfo().observeOnMainThread(
                onSuccess = {
                    refreshUserInfo(user = it, type = UserObjectEventType.REFRESH, shouldSilence = shouldSilence)
                },
                onError = {
                    printf(it.localizedMessage ?: "")
                })
        }
    }
}
