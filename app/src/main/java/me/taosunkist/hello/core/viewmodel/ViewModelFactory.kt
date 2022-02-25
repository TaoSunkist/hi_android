package me.taosunkist.hello.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.taosunkist.hello.data.net.ServerApi
import me.taosunkist.hello.ui.profile.ProfileViewModel

class ViewModelFactory(private val apiHelper: ServerApi) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(apiHelper) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}