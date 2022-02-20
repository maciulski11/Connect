package com.example.connect.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connect.R
import com.example.connect.RetrofitInstance
import com.example.connect.adapters.MessageAdapter
import com.example.connect.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.item_users_message.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageActivity: AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    var chatList = ArrayList<Chat>()
    var userList = ArrayList<User>()
    val db = FirebaseFirestore.getInstance()
    var topic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)


        messageRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        val intent = getIntent()
        val userUid = intent.getStringExtra("uid")

        imgBack.setOnClickListener {
            //cofnięcie do poprzedniego zdarzenia
            onBackPressed()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance("https://instugram-4e633-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("user").child(userUid!!)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)
                tvUserName.text = user!!.name
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        sendButton.setOnClickListener {

            val message: String = messageEdit.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                messageEdit.setText("")
            } else {
                reference2 = FirebaseDatabase.getInstance("https://instugram-4e633-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("user").child(firebaseUser.uid)

                reference2.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val user = snapshot.getValue(User::class.java)
                        val namee: String = user!!.name.toString()

                        sendMessage(firebaseUser.uid, userUid, message)
                        messageEdit.setText("")
                        topic = "/topics/${userUid}"
                        PushNotification(
                            NotificationData("${namee} :", message),
                            topic
                        ).also {
                            sendNotification(it)
                        }

                        db.collection("user").document(userUid)
                            .collection("last_message")
                            .document(firebaseUser.uid)
                            .update("message", message)

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            }
        }

        readMessage(firebaseUser.uid, userUid)
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        val reference = FirebaseDatabase.getInstance("https://instugram-4e633-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference()

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        reference.child("Chat").push().setValue(hashMap)

    }

    fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance("https://instugram-4e633-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId == (senderId) && chat.receiverId == (receiverId) ||
                        chat.senderId == (receiverId) && chat.receiverId == (senderId)
                    ) {
                        chatList.add(chat)
                    }
                }

                val messageAdapter = MessageAdapter(this@MessageActivity, chatList)

                messageRecyclerView.adapter = messageAdapter
            }
        })
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val response = RetrofitInstance.api.postNotification(notification)

            if (response.isSuccessful){
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            }else{
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch (e: Exception){
            Log.e("TAG", e.toString())
        }
    }
}