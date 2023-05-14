package com.blockdev.birdy.adaper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blockdev.birdy.R
import com.blockdev.birdy.model.Post
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.Query

class PostAdapter(
    modelClass: Class<Post?>?,
    modelLayout: Int,
    viewHolderClass: Class<PostViewHolder?>?,
    ref: Query?,
) : FirebaseRecyclerAdapter<Post, PostAdapter.PostViewHolder>(
    modelClass,
    modelLayout,
    viewHolderClass,
    ref,
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(mModelLayout, parent, false)
        return PostViewHolder(view)
    }

    override fun populateViewHolder(viewHolder: PostViewHolder, model: Post, position: Int) {
        viewHolder.txtAuthor.text = model.author
        viewHolder.txtTitle.text = model.title
        viewHolder.txtBody.text = model.body
        viewHolder.txtNumStar.text = model.starCount.toString()
        // viewHolder.imgStar.setImageResource(R.drawable.ic_star_outline);
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView
        val txtAuthor: TextView
        val txtBody: TextView

        // private ImageView imgStar;
        val txtNumStar: TextView

        init {
            txtTitle = itemView.findViewById<View>(R.id.card_post_title) as TextView
            txtAuthor = itemView.findViewById<View>(R.id.card_post_author) as TextView
            txtBody = itemView.findViewById<View>(R.id.card_post_body) as TextView
            // imgStar = (ImageView) itemView.findViewById(R.id.card_post_star);
            txtNumStar = itemView.findViewById<View>(R.id.card_post_num_star) as TextView
        }
    }
}
