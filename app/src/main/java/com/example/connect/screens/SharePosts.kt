package com.example.connect.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.connect.R
import com.example.connect.base.BaseFragment
import com.example.connect.data.User
import com.example.connect.view_model.AddPostsViewModel
import com.example.connect.view_model.EditProfileViewModel
import com.example.connect.view_model.ShareViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_add_posts.*
import kotlinx.android.synthetic.main.fragment_share_posts.*
import java.io.ByteArrayOutputStream

class SharePosts: BaseFragment() {
    override val layout: Int = R.layout.fragment_share_posts

    private val PROFILE_DEBUG = "PROFILE_DEBUG"
    private val REQUEST_IMAGE_CAPTURE = 1

    var userName = ""
    var id = ""
    var imagePosts = ""
    var image = ""

    private val fbAuth = FirebaseAuth.getInstance()
    val shareVM = EditProfileViewModel()
    val user = User()
    val db = FirebaseFirestore.getInstance()
    val addVM = AddPostsViewModel()

    override fun subscribeUi() {
        shareVM.user.observe(viewLifecycleOwner) { user ->
            bindUserData(user)
        }
        sharePosts()
    }

    private fun bindUserData(user: User) {
        userName = user.name.toString()
        imagePosts = user.image_posts.toString()
        id = user.posts.toString()
        image = user.image.toString()

        Glide.with(this)
            .load(user.image_posts)
            .into(edytujZdjecie)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            wybraneZdjecie.setImageBitmap(imageBitmap)

            Log.d(PROFILE_DEBUG, "BITMAP: " + imageBitmap.byteCount.toString())

            Glide.with(this)
                .load(imageBitmap)
                .into(edytujZdjecie)

            val stream = ByteArrayOutputStream()
            val result = imageBitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream)
            val byteArray = stream.toByteArray()

            if (result) addVM.uploadUserPhoto(byteArray)
        }
    }
        private fun sharePosts(){
        shareBT.setOnClickListener {
            val description = descriptionET.text.toString()
            val place = placeET.text.toString()

            val dataPosts = hashMapOf(
                "description" to description,
                "name" to userName,
                "image_photo" to imagePosts,
                "place" to place,
                "image" to image,
            )
            db.collection("posts").document(id)
                .set(dataPosts, SetOptions.merge())

            val dataUser = hashMapOf(
                "image_posts" to "",
                "posts" to id,
            )
            db.collection("user").document(fbAuth.currentUser!!.uid)
                .set(dataUser, SetOptions.merge())

            db.collection("user")
                .document(fbAuth.currentUser!!.uid)
                .update("list_posts", FieldValue.arrayUnion(id))

            findNavController()
                .navigate(SharePostsDirections.actionSharePostsToPostsFragment().actionId)
        }
    }

    override fun unsubscribeUi() {

    }
}