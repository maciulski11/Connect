package com.example.connect.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.connect.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_edit_post.*

class EditPostActivity: AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        imgBackEditPost.setOnClickListener {
            //cofnięcie do poprzedniego zdarzenia
            onBackPressed()
        }

        val intent = getIntent()
        val postUid = intent.getStringExtra("uid")
        val postName = intent.getStringExtra("name")
        val postImage = intent.getStringExtra("image_photo")
        val postDescription = intent.getStringExtra("description")
        val place = intent.getStringExtra("place")
        val image = intent.getStringExtra("image")

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        db.collection("post")
            .addSnapshotListener (object : EventListener<QuerySnapshot>{
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {

                    if (error != null) {
                        Log.e("Jest blad", error.message.toString())
                        return
                    }

                    Log.i("dokumanty", "Post ${postUid!!}")



                    nameEditPost.text = postName
                    placeTV.text = place
                    descriptionEditPost.text = postDescription

                    Glide.with(this@EditPostActivity)
                        .load(postImage)
                        .into(editImage)

                    Glide.with(this@EditPostActivity)
                        .load(image)
                        .circleCrop()
                        .into(photoUserSmallEdit)
                }


            })
        
    }
    
}