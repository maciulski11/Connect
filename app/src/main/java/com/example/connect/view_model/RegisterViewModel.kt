package com.example.connect.view_model

import androidx.lifecycle.ViewModel
import com.example.connect.data.User
import com.example.connect.repository.RegisterRepository

class RegisterViewModel: ViewModel() {

    private val repository = RegisterRepository()

    fun createNewUser(user: User){
        repository.createNewUser(user)
    }

    fun writeNewUserRealtimeFirebase(user: User){
        repository.writeNewUserRealtimeFirebase(user)
    }
}