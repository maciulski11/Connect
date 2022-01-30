package com.example.connect.data

import java.sql.Timestamp

data class Posts(
    val name: String? = null,
    val description: String? = null,
    val uid: String? = null,
    val timestamp: Timestamp? = null,
    val image_photo: String? = null,
    var place: String? = null,
    var image: String? = null
)
