package com.blockdev.birdy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.blockdev.birdy.base.activityViewModels
import com.blockdev.birdy.model.MyFirebase.myUid
import com.blockdev.birdy.model.Post
import com.blockdev.birdy.ui.main.FirebaseViewModel
import com.blockdev.birdy.ui.profile.NewsScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction

class FragmentNews : Fragment() {

    private val currentUid = myUid ?: "uid"
    private var click = 0

    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

//
//        // Set up FirebaseRecyclerAdapter
//        databaseReference = FirebaseDatabase.getInstance().reference
//        val postsQuery = databaseReference!!.child(MyConstant.POSTS).limitToFirst(100)
//        adapter = object : FirebaseRecyclerAdapter<Post, PostViewHolder>(
//            Post::class.java,
//            R.layout.viewholder_post,
//            PostViewHolder::class.java,
//            postsQuery,
//        ) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
//                val view = LayoutInflater.from(parent.context).inflate(mModelLayout, parent, false)
//                return PostViewHolder(view)
//            }
//
//            override fun populateViewHolder(
//                viewHolder: PostViewHolder,
//                model: Post,
//                position: Int,
//            ) {
//                val postReference = getRef(position)
//                // Set click listener for the whole post view
//                viewHolder.itemView.setOnClickListener {
//                    click++
//                    val handler = Handler()
//                    handler.postDelayed({
//                        if (click == 2) {
//                            // need to write to both place the post is stored
//                            val globalPostRef = databaseReference!!.child(MyConstant.POSTS)
//                                .child(postReference.key!!)
//                            val userPostRef = databaseReference!!.child(MyConstant.USER_POSTS)
//                                .child(model.uid!!).child(postReference.key!!)
//                            // run two transaction
//                            onStarClicked(globalPostRef)
//                            onStarClicked(userPostRef)
//                        }
//                        click = 0
//                    }, 500)
//                }
//
//                // Determine if the current user has like this post and UI accordingly
//                if (model.stars.containsKey(currentUid)) {
//                    viewHolder.imgHeart.setImageResource(R.drawable.ic_heart_filled)
//                } else {
//                    viewHolder.imgHeart.setImageResource(R.drawable.ic_heart_outline)
//                }
//                viewHolder.bindToPost(model)
//            }
//        }
//        recyclerView.adapter = adapter
//        return rootView


        return ComposeView(requireContext()).apply {
            setContent {
                val posts = viewModel.getData().observeAsState()
                NewsScreen(listPosts = posts.value ?: emptyList())
            }
        }
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

}
