package me.taosunkist.hello.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import me.taosunkist.hello.data.net.model.ApiResponse
import me.taosunkist.hello.data.net.model.UserDetails

class ProfileViewModel : ViewModel() {

    override fun onCleared() {
        super.onCleared()
    }

    fun getUserDetails() = liveData(Dispatchers.IO) {
        emit(ApiResponse.success(UserDetails.fake()))
        try {
            emit(ApiResponse.success(UserDetails.fake()))
        } catch (exception: Exception) {
            emit(ApiResponse.success(UserDetails.fake()))
        }
    }
}