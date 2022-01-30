package com.example.connect.data

data class User(
    val uid: String? = null,
    val email: String? = null,
    val name:String? = null,
    val image: String? = null,
    val nick: String? = null,
    val image_posts: String? = null,
    var posts: String? = null,
    val describe_profile: String? = null,
    val list_posts: List<String>? = null,
    )
