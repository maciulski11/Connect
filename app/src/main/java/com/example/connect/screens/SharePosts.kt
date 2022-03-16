package com.example.connect.screens

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.connect.R
import com.example.connect.activity.MapsActivity
import com.example.connect.base.BaseFragment
import com.example.connect.data.User
import com.example.connect.view_model.EditProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_share_posts.*

class SharePosts: BaseFragment() {
    override val layout: Int = R.layout.fragment_share_posts

    var userName = ""
    var id = ""
    var imagePosts = ""
    var videoPosts = ""
    var image = ""

    private val fbAuth = FirebaseAuth.getInstance()
    val shareVM = EditProfileViewModel()
    val user = User()
    val db = FirebaseFirestore.getInstance()

    override fun subscribeUi() {
        shareVM.user.observe(viewLifecycleOwner) { user ->
            bindUserData(user)
        }

       /* locationClick.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            startActivity(intent)
        } */

        sharePosts()
    }

    private fun bindUserData(user: User) {
        userName = user.name.toString()
        imagePosts = user.image_posts.toString()
        videoPosts = user.video_posts.toString()
        id = user.posts.toString()
        image = user.image.toString()


        Glide.with(this)
            .load(user.image_posts)
            .into(editPhoto)
    }

        private fun sharePosts(){
        shareBT.setOnClickListener {
            val description = descriptionET.text.toString()
            val place = placeET.text.toString()

            val dataPosts = hashMapOf(
                "description" to description,
                "name" to userName,
                "image_photo" to imagePosts,
                "video" to videoPosts,
                "place" to place,
                "image" to image,
            )
            db.collection("posts").document(id)
                .set(dataPosts, SetOptions.merge())

            val dataUser = hashMapOf(
                "image_posts" to "",
                "video_posts" to "",
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