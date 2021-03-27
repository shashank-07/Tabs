package com.example.tabs.ui.auth

import Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabs.data.repository.MainRepository
import com.example.tabs.data.responses.LoginResponse
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: MainRepository
):ViewModel() {

    private val _loginResponse :MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse : LiveData<Resource<LoginResponse>>
        get()=_loginResponse
    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(email, password)
    }

    suspend fun saveAuthToken(token: String) {
        repository.saveAuthToken(token)
    }

}

