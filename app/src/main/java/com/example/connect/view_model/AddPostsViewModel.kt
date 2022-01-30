package com.example.connect.view_model

import androidx.lifecycle.ViewModel
import com.example.connect.repository.AddPostsRepository

class AddPostsViewModel: ViewModel() {

    private val repository = AddPostsRepository()

    fun uploadUserPhoto(bytes: ByteArray) {
        repository.uploadUserPhoto(bytes)
    }
}