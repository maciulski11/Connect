package com.example.connect.screens

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connect.R
import com.example.connect.adapters.MessageUsersAdapter
import com.example.connect.base.BaseFragment
import com.example.connect.data.Chat
import com.example.connect.data.LastMessage
import com.example.connect.data.User
import com.example.connect.firebase.FirebaseServices
import com.example.connect.view_model.EditProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_message_users.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_users_message.*

class MessageUsersFragment: BaseFragment() {
    override val layout: Int = R.layout.fragment_message_users

    var userList = ArrayList<User>()

    override fun subscribeUi() {
        //FirebaseMessaging.getInstance().token.addOnCompleteListener {
          //  FirebaseServices.token = it.result
       //}

        userMessageRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        userMessageRecyclerView.setHasFixedSize(true)

        getUsersList()
    }

    //wczytanie listy kontaktow do recyclerview
    private fun getUsersList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")

        val databaseReference = FirebaseDatabase
            .getInstance("https://instugram-4e633-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("user")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message+"COS IDZIE XZLE!!!!!", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.uid.equals(userid)) {
                        userList.add(user)

                    }
                }

                val userAdapter = MessageUsersAdapter(context!!, userList)
                userMessageRecyclerView.adapter = userAdapter
            }

        })
    }

    override fun unsubscribeUi() {

    }
}