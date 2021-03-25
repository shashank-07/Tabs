package com.example.tabs.data.responses

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val food_preference: List<Any>,
    val name: String,
    val password: String,
    val places_preference: List<String>
)