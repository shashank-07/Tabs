package com.example.tabs.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tabs.data.repository.MainRepository
import com.example.tabs.data.repository.BaseRepository
import com.example.tabs.ui.auth.AuthViewModel
import com.example.tabs.ui.home.HomeViewModel
import com.example.tabs.ui.register.RegisterViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelfactory(
    private val repository: BaseRepository
):ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return when {
            //Define new model instance
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as MainRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository as MainRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as MainRepository) as T

            else -> throw IllegalArgumentException("ViewModelClass not found")
        }
    }

}