package com.example.connect.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connect.R
import com.example.connect.activity.EditPostActivity
import com.example.connect.data.Posts

class FindAdapter(private val context: Context, private val postsList: ArrayList<Posts>): RecyclerView.Adapter<FindAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_find_photo, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val findPost: Posts = postsList[position]

        val image = holder.itemView.findViewById<ImageView>(R.id.findPhoto)
        Glide.with(holder.itemView)
            .load(findPost.image_photo)
            .into(image)

        holder.layoutFindPhoto.setOnClickListener {
            val intent = Intent(context, EditPostActivity::class.java)
            intent.putExtra("uid" , findPost.uid)
            intent.putExtra("name", findPost.name)
            intent.putExtra("image_photo", findPost.image_photo)
            intent.putExtra("description" , findPost.description)
            intent.putExtra("place", findPost.place)
            intent.putExtra("image", findPost.image)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = postsList.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)  {
        val layoutFindPhoto = view.findViewById<LinearLayout>(R.id.layoutFindPhoto)

    }
}