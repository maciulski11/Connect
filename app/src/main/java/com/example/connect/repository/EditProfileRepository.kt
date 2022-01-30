package com.example.connect.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.connect.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditProfileRepository {

    private val REPO_DEBUG = "Cos jest nie tak"

    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()


    //wczytanie zdjecia
    fun uploadUserPhoto(bytes: ByteArray) {
        storage.getReference("photoUsers")
            .child("${auth.currentUser!!.uid}.jpg")
            .putBytes(bytes)
            .addOnCompleteListener {
                Log.d(REPO_DEBUG, "COMPLETE UPLOAD PHOTO")
            }
            .addOnSuccessListener {
                getPhotoDownloadUrl(it.storage)

            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    private fun getPhotoDownloadUrl(storage: StorageReference) {
        storage.downloadUrl
            .addOnSuccessListener {
                updateUserPhoto(it.toString())
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    private fun updateUserPhoto(url: String?) {
        cloud.collection("user")
            .document(auth.currentUser!!.uid)
            .update("image", url)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "UPDATE USER PHOTO!")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    //twotzymy fun ktora ma nam zwrocic LiveData<User>
    fun getUserData(): LiveData<User> {
        val cloudResult = MutableLiveData<User>()
        val uid = auth.currentUser?.uid//-> wyciagamy uid uzytkownika ktory jest juz zalogowany

        //tworzymy zapytacie do kolekcji, podajemy sciezke czyli naszego user
        cloud.collection("user")
            .document(uid!!) //potem do jakiego dokumentu o nazwie uid
            .get()//potem uzyskaj ten dokument
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                cloudResult.postValue(user)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }

        return cloudResult//zwracamy nasz LiveData
    }

    fun editProfileData(map: Map<String, String>) {
        cloud.collection("user")
            .document(auth.currentUser!!.uid)
            .update(map)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "Zaktualizowano dane!")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

}