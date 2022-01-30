package com.example.connect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connect.R
import com.example.connect.data.Posts
import kotlinx.android.synthetic.main.item_post.view.*

class PostsAdapter (private val postsList: ArrayList<Posts>): RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostsAdapter.MyViewHolder, position: Int) {
        val postss: Posts = postsList[position]

        holder.bindView(postss)

        val image = holder.itemView.findViewById<ImageView>(R.id.imagePosts)
        Glide.with(holder.itemView)
            .load(postss.image_photo)
            .into(image)

        val photoSmall = holder.itemView.findViewById<ImageView>(R.id.photoUserSmall)
        Glide.with(holder.itemView)
            .load(postss.image)
            .circleCrop()
            .into(photoSmall)

        //val photoZomm = PhotoViewAttacher(image)
        //photoZomm.update()

    }

    override fun getItemId(i: Int): Long = i.toLong()


    override fun getItemCount(): Int = postsList.size

    //w tej klasie wypelnimy nasze widoki z layoutu
    inner class MyViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {

        fun bindView(p: Posts) {
            view.nameTV.text = p.name
            view.descriptionTV.text = p.description
            view.placeTV.text = p.place
        }

    }
}