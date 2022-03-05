package com.example.connect.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.connect.R
import com.example.connect.base.BaseFragment
import com.example.connect.data.Posts
import com.example.connect.data.User
import com.example.connect.view_model.AddPostsViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_add_posts.*
import java.io.ByteArrayOutputStream
import java.util.*

class AddPostsFragment() : BaseFragment() {

    override val layout: Int = R.layout.fragment_add_posts

    private val PROFILE_DEBUG = "PROFILE_DEBUG"
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_VIDEO_CAPTURE = 2

    val db = FirebaseFirestore.getInstance()
    val random = Random().nextInt(999999999)
    val fbAuth = FirebaseAuth.getInstance()

    val addVM = AddPostsViewModel()
    val posts = Posts()
    val user = User()

    companion object {
        //image pick code
        private val IMAGE_CHOOSE = 1000

        //permission code
        private val PERMISSION_CODE = 1001
    }

    override fun subscribeUi() {
        addPhotoClick()
        takePhotoClick()
        dispatchTakeVideoIntent()

        shareButton.setOnClickListener {
            findNavController()
                .navigate(AddPostsFragmentDirections.actionAddPostsFragmentToSharePosts().actionId)
            next()
        }

    }

    private fun addPhotoClick() {
        takePhotoBT.setOnClickListener {
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Log.d(PROFILE_DEBUG, e.message.toString())
            }
        }
    }

    private fun takePhotoClick() {
        addPhotoBT.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_DENIED
                ) {

                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    chooseImageGallery()
                }
            } else {
                chooseImageGallery()
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakeVideoIntent() {
        videoBT.setOnClickListener {
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageGallery()
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            wybraneZdjecie.visibility = View.VISIBLE
            val imageBitmap = data?.extras?.get("data") as Bitmap
            wybraneZdjecie.setImageBitmap(imageBitmap)

            Log.d(PROFILE_DEBUG, "BITMAP: " + imageBitmap.byteCount.toString())

            Glide.with(this)
                .load(imageBitmap)
                .into(wybraneZdjecie)

            val stream = ByteArrayOutputStream()
            val result = imageBitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream)
            val byteArray = stream.toByteArray()

            if (result) addVM.uploadUserPhoto(byteArray)


        } else if (requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {
            wybraneZdjecie.visibility = View.VISIBLE

            val imageUri = data?.data!!
            wybraneZdjecie.setImageURI(imageUri)

            addVM.uploadGalerryPhoto(imageUri)


        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            wybraneVideo.visibility = View.VISIBLE
            refreshVideoBT.visibility = View.VISIBLE
            val videoUri : Uri = data?.data!!
            wybraneVideo.setVideoURI(videoUri)
            wybraneVideo.start()

            addVM.uploadVideo(videoUri)

            refreshVideoBT.setOnClickListener {
                wybraneVideo.setVideoURI(videoUri)
                wybraneVideo.start()
            }
        }
    }

    private fun next() {
        val uid = fbAuth.currentUser!!.uid

        val dataPosts = hashMapOf(
            "date" to Timestamp.now(),
            "image_photo" to "",
            "name" to "",
            "uid" to uid,
            "place" to "",
            "image" to ""
        )
        db.collection("posts").document(random.toString())
            .set(dataPosts, SetOptions.merge())

        val dataUser = hashMapOf(
            "posts" to random.toString(),


            )
        db.collection("user").document(fbAuth.currentUser!!.uid)
            .set(dataUser, SetOptions.merge())


    }

    override fun unsubscribeUi() {

    }
}