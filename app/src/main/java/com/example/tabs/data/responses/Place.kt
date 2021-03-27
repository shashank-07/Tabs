package com.example.tabs.data.responses

data class Place(
    val address: String,
    val category: String,
    val cluster: Int,
    val company: String,
    val description: String,
    val driving_time: Double,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val net_time: Double,
    val opening_hours: String,
    val particulars: String,
    val picture: String,
    val price: String,
    val score: Double,
    val time_spent: Double
)

