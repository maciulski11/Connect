package com.example.connect.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.connect.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ShareRepository {

    private val REPO_DEBUG = "Cos jest nie tak"

    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()

    //twotzymy fun ktora ma nam zwrocic LiveData<User>
    fun getUserData() {
        val cloudResult = MutableLiveData<User>()
        val uid = auth.currentUser?.uid//-> wyciagamy uid uzytkownika ktory jest juz zalogowany

        //tworzymy zapytacie do kolekcji, podajemy sciezke czyli naszego user
        cloud.collection("user")
            .document(uid!!) //potem do jakiego dokumentu o nazwie uid
            .get()//potem uzyskaj ten dokument
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                cloudResult.postValue(user!!)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

}