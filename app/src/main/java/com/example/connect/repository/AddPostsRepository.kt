package com.example.connect.repository

import android.net.Uri
import android.util.Log
import com.example.connect.data.Posts
import com.example.connect.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddPostsRepository {

    private val REPO_DEBUG = "Cos jest nie tak"

    val random1 = Random().nextInt(999999999)
    val posts = Posts()
    val user = User()

    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()


    fun uploadUserPhoto(bytes: ByteArray) {
        storage.getReference("photoPosts")
            .child("${random1}.jpg")
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

     fun getPhotoDownloadUrl(storage: StorageReference) {
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
            .update("image_posts", url)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "UPDATE USER PHOTO!")

            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    fun uploadGalerryPhoto(imageUri: Uri){
        storage.getReference("photoPosts")
            .child("${random1}.jpg")
            .putFile(imageUri)
            .addOnSuccessListener {
                getPhotoDownloadUrl(it.storage)
            }
    }

    fun uploadVideo(videoUri: Uri){
        storage.getReference("videoPosts")
            .child("${random1}.png")
            .putFile(videoUri)
            .addOnSuccessListener {
                getPhotoDownloadUrl(it.storage)
            }
    }
}