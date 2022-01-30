package com.example.connect.repository

import com.example.connect.data.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterRepository{

  private val cloud = FirebaseFirestore.getInstance()
  val database = FirebaseDatabase.getInstance("https://instugram-4e633-default-rtdb.europe-west1.firebasedatabase.app/")


  fun createNewUser(user: User) {
    cloud.collection("user")
      .document(user.uid!!)
      .set(user)
  }

  fun writeNewUserRealtimeFirebase(user: User){
    database.reference.child("user")
      .child(user.uid!!)
      .setValue(user)
  }


}