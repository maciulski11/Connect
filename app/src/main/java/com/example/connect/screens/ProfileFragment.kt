package com.example.connect.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.connect.R
import com.example.connect.activity.LogRegActivity
import com.example.connect.adapters.ProfileAdapter
import com.example.connect.base.BaseFragment
import com.example.connect.data.Posts
import com.example.connect.data.User
import com.example.connect.view_model.EditProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: BaseFragment() {

    override val layout: Int = R.layout.fragment_profile

    private val fbAuth = FirebaseAuth.getInstance()
    val profileVM = EditProfileViewModel()
    private lateinit var postsArrayList: ArrayList<Posts>
    private lateinit var adapter: ProfileAdapter
    private lateinit var db: FirebaseFirestore

    override fun subscribeUi() {
        profileVM.user.observe(viewLifecycleOwner, { user ->
            bindUserData(user)
            pobierzDaneZFirebase()
        })

        recyclerViewProfile.layoutManager = GridLayoutManager(context, 3)
        recyclerViewProfile.setHasFixedSize(true)

        postsArrayList = arrayListOf()


        adapter = ProfileAdapter(postsArrayList)
        recyclerViewProfile.adapter = adapter

        edit_account.setOnClickListener {
             findNavController()
             .navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment().actionId)

        }
    }

    private fun bindUserData(user: User) {
        full_name_profile.setText(user.name)
        describe_profile.setText(user.describe_profile)
        nick_profile.setText(user.nick)


        Glide.with(this)
            .load(user.image)
            .circleCrop()
            .into(image_profile)
    }

    private fun pobierzDaneZFirebase() {

        db = FirebaseFirestore.getInstance()
        db.collection("posts")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {

                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {

                    if (error != null) {
                        Log.e("Jest blad", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value!!.documentChanges) {

                        if (dc.type == DocumentChange.Type.ADDED) {

                            postsArrayList.add(dc.document.toObject(Posts::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.log_out_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutAction -> {
                fbAuth.signOut()
                requireActivity().finish()
                val intent = Intent(requireContext(), LogRegActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun unsubscribeUi() {

    }

}

