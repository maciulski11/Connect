package com.example.connect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connect.R
import com.example.connect.data.Posts

class ProfileAdapter(private val imageList: ArrayList<Posts>): RecyclerView.Adapter<ProfileAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_small_image, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileAdapter.MyViewHolder, position: Int) {
        val profileImage: Posts = imageList[position]

        val image = holder.itemView.findViewById<ImageView>(R.id.small_image)
        Glide.with(holder.itemView)
            .load(profileImage.image_photo)
            .into(image)
    }

    override fun getItemCount(): Int = imageList.size

    inner class MyViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {

    }
}

