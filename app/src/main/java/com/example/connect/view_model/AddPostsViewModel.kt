package com.example.connect.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.connect.repository.AddPostsRepository

class AddPostsViewModel: ViewModel() {

    private val repository = AddPostsRepository()

    fun uploadUserPhoto(bytes: ByteArray) {
        repository.uploadUserPhoto(bytes)
    }

    fun uploadGalerryPhoto(imageUri: Uri){
        repository.uploadGalerryPhoto(imageUri)
    }

    fun uploadVideo(videoUri: Uri){
        repository.uploadVideo(videoUri)
    }
}