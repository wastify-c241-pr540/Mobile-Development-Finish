package com.ramm.ui.user

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    private val _textUser = MutableLiveData<String>().apply {
        value = "Username"
    }

    private val _textEmail = MutableLiveData<String>().apply {
        value = "Email@gmail.com"
    }

    private val _pieChartData = MutableLiveData<List<Float>>()
    val pieChartData: LiveData<List<Float>>
        get() = _pieChartData

    private val _profilePictureUrl = MutableLiveData<String?>()
    val profilePictureUrl: LiveData<String?>
        get() = _profilePictureUrl

    fun updatePieChartData(data: List<Float>) {
        _pieChartData.value = data
    }

    fun updateProfilePictureUrl(url: String?) {
        _profilePictureUrl.value = url
    }

    val username: LiveData<String> = _textUser
    val email: LiveData<String> = _textEmail
}