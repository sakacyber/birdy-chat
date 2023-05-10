package com.example.saka.myapplication

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.saka.myapplication.model.MyConstant
import com.example.saka.myapplication.model.MyFirebase.myUid
import com.example.saka.myapplication.model.Post
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction

class FragmentNews : Fragment() {
    private var databaseReference: DatabaseReference? = null
    private var adapter: FirebaseRecyclerAdapter<Post, PostViewHolder>? = null
    private val currentUid = myUid ?: "uid"
    private var click = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_news, container, false)
        val recyclerView = rootView.findViewById<View>(R.id.news_recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)

        // Set up layout manager, reverse layout
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager

        // Set up FirebaseRecyclerAdapter
        databaseReference = FirebaseDatabase.getInstance().reference
        val postsQuery = databaseReference!!.child(MyConstant.POSTS).limitToFirst(100)
        adapter = object : FirebaseRecyclerAdapter<Post, PostViewHolder>(
            Post::class.java,
            R.layout.viewholder_post,
            PostViewHolder::class.java,
            postsQuery,
        ) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(mModelLayout, parent, false)
                return PostViewHolder(view)
            }

            override fun populateViewHolder(
                viewHolder: PostViewHolder,
                model: Post,
                position: Int,
            ) {
                val postReference = getRef(position)
                // Set click listener for the whole post view
                viewHolder.itemView.setOnClickListener {
                    click++
                    val handler = Handler()
                    handler.postDelayed({
                        if (click == 2) {
                            // need to write to both place the post is stored
                            val globalPostRef = databaseReference!!.child(MyConstant.POSTS)
                                .child(postReference.key!!)
                            val userPostRef = databaseReference!!.child(MyConstant.USER_POSTS)
                                .child(model.uid!!).child(postReference.key!!)
                            // run two transaction
                            onStarClicked(globalPostRef)
                            onStarClicked(userPostRef)
                        }
                        click = 0
                    }, 500)
                }

                // Determine if the current user has like this post and UI accordingly
                if (model.stars.containsKey(currentUid)) {
                    viewHolder.imgHeart.setImageResource(R.drawable.ic_heart_filled)
                } else {
                    viewHolder.imgHeart.setImageResource(R.drawable.ic_heart_outline)
                }
                viewHolder.bindToPost(model)
            }
        }
        recyclerView.adapter = adapter
        return rootView
    }

    private fun onStarClicked(postReference: DatabaseReference) {
        postReference.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val post = mutableData.getValue(Post::class.java)
                    ?: return Transaction.success(mutableData)
                if (post.stars.containsKey(currentUid)) {
                    // unstar the post and remove self from stars
                    post.starCount = post.starCount - 1
                    post.stars.remove(currentUid)
                } else {
                    // star the post and add self to stars
                    post.starCount = post.starCount + 1
                    post.stars[currentUid] = true
                }
                // set value and report transaction success
                mutableData.value = post
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                b: Boolean,
                dataSnapshot: DataSnapshot?,
            ) {
                // Transaction complete
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (adapter != null) {
            adapter!!.cleanup()
        }
    }

    inner class PostViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imgHeart: ImageView
        val txtTitle: TextView
        val txtAuthor: TextView
        val txtBody: TextView
        val txtNumStar: TextView

        init {
            txtTitle = itemView.findViewById(R.id.card_post_title)
            txtAuthor = itemView.findViewById(R.id.card_post_author)
            txtBody = itemView.findViewById(R.id.card_post_body)
            imgHeart = itemView.findViewById(R.id.card_post_star)
            txtNumStar = itemView.findViewById(R.id.card_post_num_star)
        }

        fun bindToPost(post: Post) {
            txtTitle.text = post.title
            txtAuthor.text = post.author
            txtBody.text = post.body
            txtNumStar.text = post.starCount.toString()
            // imgHeart.setOnClickListener(clickListener);
        }
    }
}
