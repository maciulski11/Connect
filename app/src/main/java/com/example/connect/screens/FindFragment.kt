package com.example.connect.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.connect.R
import com.example.connect.adapters.FindAdapter
import com.example.connect.base.BaseFragment
import com.example.connect.data.Posts
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_find.*

class FindFragment: BaseFragment() {
    override val layout: Int = R.layout.fragment_find

    private lateinit var findArrayList: ArrayList<Posts>
    private lateinit var adapter: FindAdapter
    private lateinit var db: FirebaseFirestore


    override fun subscribeUi() {

        recycler_view_find.layoutManager = GridLayoutManager(context, 3)
        recycler_view_find.setHasFixedSize(true)

        findArrayList = arrayListOf()


        adapter = FindAdapter(requireContext(), findArrayList)
        recycler_view_find.adapter = adapter


        pobierzDaneZFirebase()
    }

    private fun pobierzDaneZFirebase(){

        db = FirebaseFirestore.getInstance()
        db.collection("posts")
            .limit(40)
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

                            findArrayList.add(dc.document.toObject(Posts::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

            })
    }

    override fun unsubscribeUi() {
    }
}