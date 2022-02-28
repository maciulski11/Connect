package com.example.connect.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import com.example.connect.R
import com.example.connect.activity.MainActivity
import com.example.connect.base.BaseFragment
import com.example.connect.data.User
import com.example.connect.view_model.EditProfileViewModel
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_share_posts.*
import java.io.ByteArrayOutputStream

class EditProfileFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_edit_profile

    private val PROFILE_DEBUG = "PROFILE_DEBUG"
    private val REQUEST_IMAGE_CAPTURE = 1

    private val profileVM = EditProfileViewModel()

    override fun subscribeUi() {

        profileVM.user.observe(viewLifecycleOwner) { user ->
            bindUserData(user)
        }

      //  setupTakePictureClick()
        setOnClckZapiszDane()
    }

    //wczytywanie danych podczas edycji danych profilowych
    private fun bindUserData(user: User) {
        Log.d(PROFILE_DEBUG, user.toString())
        podajImie.setText(user.name)
        nickET.setText(user.nick)
        describe.setText(user.describe_profile)
       Glide.with(this)
            .load(user.image)
            .circleCrop()
            .into(userImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            Log.d(PROFILE_DEBUG, "BITMAP: " + imageBitmap.byteCount.toString())

            Glide.with(this)
                .load(imageBitmap)
                .circleCrop()
                .into(userImage)

            val stream = ByteArrayOutputStream()
            val result = imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()

            if (result) profileVM.uploadUserPhoto(byteArray)
        }
    }

    private fun setupTakePictureClick() {
        //funkcja ktora odpowiada za zrobienie zdjecia po klikniecu w nasz imagebutton
        userImage.setOnClickListener {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
            try {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            } catch (exc: Exception) {
                Log.d(PROFILE_DEBUG, exc.message.toString())
            }
        }
    }

    private fun setOnClckZapiszDane() {
        zapiszNickBT.setOnClickListener {
            val name = podajImie.text.toString()
            val describe = describe.text.toString()
            val nick = nickET.text.toString()

            val map = mapOf("name" to name, "describe_profile" to describe, "nick" to nick)
            profileVM.editProfileData(map)

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun unsubscribeUi() {
    }
}