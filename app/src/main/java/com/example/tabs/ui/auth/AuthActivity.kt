package com.example.tabs.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.tabs.R
import com.example.tabs.data.UserPreferences
import com.example.tabs.ui.home.HomeActivity
import com.example.tabs.ui.startNewActivity

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userPreferences = UserPreferences(this)
        userPreferences.authToken.asLiveData().observe(this, Observer {
            val activity = HomeActivity::class.java
            if(it!=null) startNewActivity(activity);
        })
    }
}