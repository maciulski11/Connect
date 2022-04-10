package com.example.connect.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connect.R
import com.example.connect.activity.LogRegActivity
import com.example.connect.adapters.PostsAdapter
import com.example.connect.base.BaseFragment
import com.example.connect.data.Posts
import com.example.connect.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_posts.*

class PostsFragment: BaseFragment() {

    override val layout: Int = R.layout.fragment_posts

    private lateinit var userArrayList: ArrayList<Posts>
    private lateinit var adapter: PostsAdapter
    private lateinit var db: FirebaseFirestore
    private val fbAuth = FirebaseAuth.getInstance()

    override fun subscribeUi() {

        recyclerViewPosts.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewPosts.setHasFixedSize(true)

        //inicjujemy nasza liste:
        userArrayList = arrayListOf()

        adapter = PostsAdapter(requireContext(), userArrayList)
        recyclerViewPosts.adapter = adapter

        pobierzDaneZFirebase()

    }

    private fun pobierzDaneZFirebase() {
        db = FirebaseFirestore.getInstance()
        db.collection("posts")
            .limit(9)
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
                        //sprawdxzamy czy dokument zostal poprawnie dodany:
                        if (dc.type == DocumentChange.Type.ADDED) {

                            userArrayList.add(dc.document.toObject(Posts::class.java))
                            val user = User()
                            Log.i("dokumanty", "Post ${user.name}")
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
        inflater.inflate(R.menu.posts_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notificationsButton -> {
               // findNavController()
                   // .navigate(PostsFragmentDirections.actionPostsFragmentToNotificationsFragment().actionId)
            }
        }
        return false
    }

    override fun unsubscribeUi() {
    }

}