package com.example.connect.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connect.R
import com.example.connect.activity.MessageActivity
import com.example.connect.data.LastMessage
import com.example.connect.data.NotificationData
import com.example.connect.data.User
import de.hdodenhof.circleimageview.CircleImageView

class MessageUsersAdapter(private val context: Context, private val userList: ArrayList<User>,
                            private val lastList: ArrayList<LastMessage>):
    RecyclerView.Adapter<MessageUsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_users_message, parent, false)
        return UserViewHolder(itemView)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val user: User = userList[position]
        val last: LastMessage = lastList[position]

        holder.lastMessage.text = last.message
        holder.name.text = user.name
        Glide.with(holder.itemView)
            .load(user.image)
            .into(holder.image)

        holder.layoutUser.setOnClickListener {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("uid" , user.uid)
            context.startActivity(intent)

        }

    }


    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name = view.findViewById<TextView>(R.id.nameUsers)
        val image = view.findViewById<CircleImageView>(R.id.userImage)
        val layoutUser = view.findViewById<LinearLayout>(R.id.layoutUser)
        val lastMessage = view.findViewById<TextView>(R.id.lastMessage)

    }
}