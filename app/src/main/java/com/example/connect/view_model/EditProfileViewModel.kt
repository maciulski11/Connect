package com.example.connect.view_model

import androidx.lifecycle.ViewModel
import com.example.connect.repository.EditProfileRepository

class EditProfileViewModel : ViewModel() {

    private val repository = EditProfileRepository()

    fun editProfileData(map: Map<String, String>) {
        repository.editProfileData(map)
    }

    val user = repository.getUserData()

    fun uploadUserPhoto(bytes: ByteArray) {
        repository.uploadUserPhoto(bytes)
    }

}