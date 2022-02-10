package me.taosunkist.hello.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import me.taosunkist.hello.data.net.model.ApiResponse

class ProfileViewModel : ViewModel() {

    override fun onCleared() {
        super.onCleared()
    }

    fun getUserDetails() = liveData(Dispatchers.IO) {
        emit(ApiResponse.success(Any()))
        try {
            emit(ApiResponse.success(Any()))
        } catch (exception: Exception) {
            emit(ApiResponse.success(Any()))
        }
    }
}