package me.taosunkist.hello.data

import me.taosunkist.hello.data.model.User

class UserStore {

    companion object {

        val shared: UserStore = UserStore()
    }

    var user: User? = null

    fun login() {
        user = User.fake()
    }


    fun refreshUser(user: User?, shouldSilence: Boolean = false) {
        user?.let {
            this.user = user
            if (shouldSilence) {
                /* TODO */
            } else {
                /* TODO */
            }
        }
    }

    fun logout() {
        user = null
    }
}