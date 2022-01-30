package com.example.connect.view_model

import com.example.connect.repository.ShareRepository

class ShareViewModel {

    private val repository = ShareRepository()

    val user = repository.getUserData()

}