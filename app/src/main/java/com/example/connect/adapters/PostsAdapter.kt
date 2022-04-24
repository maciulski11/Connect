package com.example.connect.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connect.R
import com.example.connect.activity.CommentActivity
import com.example.connect.activity.EditUserProfileActivity
import com.example.connect.data.Posts
import com.example.connect.data.User
import kotlinx.android.synthetic.main.item_post.view.*
import uk.co.senab.photoview.PhotoViewAttacher
import java.util.*
import kotlin.collections.ArrayList

class PostsAdapter (private val context: Context, private val postsList: ArrayList<Posts>): RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostsAdapter.MyViewHolder, position: Int) {
        val post: Posts = postsList[position]

        holder.bindView(post)

        val image = holder.itemView.findViewById<ImageView>(R.id.imagePosts)
        val video = holder.itemView.findViewById<VideoView>(R.id.videoPosts)

            Glide.with(holder.itemView)
            .load(post.image_photo)
            .into(image)

        val photoSmall = holder.itemView.findViewById<ImageView>(R.id.photoUserSmall)
        Glide.with(holder.itemView)
            .load(post.image)
            .circleCrop()
            .into(photoSmall)

        val photoZomm = PhotoViewAttacher(image)
        photoZomm.update()

        holder.profileUser.setOnClickListener{
            val intent = Intent(context, EditUserProfileActivity::class.java)
            context.startActivity(intent)
        }

        holder.comment.setOnClickListener {
            val intent = Intent(context, CommentActivity::class.java)
            context.startActivity(intent)
        }

        holder.fireClick.setOnClickListener {

            holder.fireClickCollor.visibility = View.VISIBLE
            holder.fireClick.visibility = View.INVISIBLE
        }

        holder.fireClickCollor.setOnClickListener {

            holder.fireClick.visibility = View.VISIBLE
            holder.fireClickCollor.visibility = View.INVISIBLE
        }
    }

    override fun getItemId(i: Int): Long = i.toLong()


    override fun getItemCount(): Int = postsList.size

    //w tej klasie wypelnimy nasze widoki z layoutu
    inner class MyViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {

        val comment = view.findViewById<ImageView>(R.id.commentIcon)
        val fireClick = view.findViewById<ImageView>(R.id.fireButton)
        val fireClickCollor = view.findViewById<ImageView>(R.id.fireButtonCollor)
        val profileUser = view.findViewById<LinearLayout>(R.id.profileUserClick)


        fun bindView(p: Posts) {
            view.nameTV.text = p.name
            view.descriptionTV.text = p.description
            view.timestampTV.text = p.date
            view.placeTV.text = p.place
            view.liczbaPodpalaczy.text = p.fire
            view.liczbaComments.text = p.comments
        }

    }
}