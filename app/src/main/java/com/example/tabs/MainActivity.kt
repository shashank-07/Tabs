package com.example.tabs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.tabs.data.UserPreferences
import com.example.tabs.ui.auth.AuthActivity
import com.example.tabs.ui.home.HomeActivity
import com.example.tabs.ui.startNewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}