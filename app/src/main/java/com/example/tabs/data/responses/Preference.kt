package com.example.tabs.data.responses

data class Preference(
        var group:String,
        var categories:List<String>,
        var selected:Boolean
)