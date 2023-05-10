package com.example.saka.myapplication.adaper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.saka.myapplication.R
import com.example.saka.myapplication.adaper.UserAdapter.UserViewHolder
import com.example.saka.myapplication.model.User
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.Query

class UserAdapter
/**
 * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
 * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
 * instance of the corresponding view with the data from an instance of modelClass.
 * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
 * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
 * combination of `limit()`, `startAt()`, and `endAt()`.
 */(
    modelClass: Class<User>?,
    modelLayout: Int,
    viewHolderClass: Class<UserViewHolder>?,
    ref: Query?,
) : FirebaseRecyclerAdapter<User, UserViewHolder>(modelClass, modelLayout, viewHolderClass, ref) {
    private var recyclerItemClick: OnRecyclerItemClick? = null
    override fun populateViewHolder(viewHolder: UserViewHolder, model: User, position: Int) {
        viewHolder.name.text = model.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(mModelLayout, parent, false)
        return UserViewHolder(rootView)
    }

    fun setOnItemClickListener(recyclerItemClick: OnRecyclerItemClick?) {
        this.recyclerItemClick = recyclerItemClick
    }

    inner class UserViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imgProfile: ImageView
        val name: TextView
        val message: TextView
        val timeStamp: TextView

        init {
            imgProfile = itemView.findViewById(R.id.vh_user_img)
            name = itemView.findViewById<View>(R.id.vh_user_name) as TextView
            message = itemView.findViewById<View>(R.id.vh_user_msg) as TextView
            timeStamp = itemView.findViewById<View>(R.id.vh_user_time_stamp) as TextView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (recyclerItemClick != null) {
                recyclerItemClick!!.onItemClick(v, position)
            }
        }
    }

    interface OnRecyclerItemClick {
        fun onItemClick(v: View?, position: Int)
    }
}
