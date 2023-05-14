package com.blockdev.birdy.adaper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blockdev.birdy.model.User
import com.blockdev.birdy.R
import com.blockdev.birdy.adaper.AddFriendAdapter.AddFriendViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.Query

/**
 * Created by Saka on 04-Jun-17.
 */
class AddFriendAdapter(
    modelClass: Class<User?>?,
    modelLayout: Int,
    viewHolderClass: Class<AddFriendViewHolder?>?,
    ref: Query?,
) : FirebaseRecyclerAdapter<User, AddFriendViewHolder>(
    modelClass,
    modelLayout,
    viewHolderClass,
    ref,
) {
    private var recyclerItemClick: OnAddFriendItemClick? = null
    fun setOnItemClickListener(recyclerItemClick: OnAddFriendItemClick?) {
        this.recyclerItemClick = recyclerItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(mModelLayout, parent, false)
        return AddFriendViewHolder(view)
    }

    override fun populateViewHolder(viewHolder: AddFriendViewHolder, model: User, position: Int) {
        viewHolder.name.text = model.name
    }

    inner class AddFriendViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val imgProfile: ImageView
        val name: TextView

        init {
            imgProfile = itemView.findViewById<View>(R.id.vh_add_friend_img) as ImageView
            name = itemView.findViewById<View>(R.id.vh_add_friend_txt_name) as TextView
        }
    }

    interface OnAddFriendItemClick {
        fun onItemClick(v: View?, position: Int)
    }
}
