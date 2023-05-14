package com.blockdev.birdy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blockdev.birdy.adaper.UserAdapter
import com.blockdev.birdy.adaper.UserAdapter.OnRecyclerItemClick
import com.blockdev.birdy.adaper.UserAdapter.UserViewHolder
import com.blockdev.birdy.model.MyFirebase.myUid
import com.blockdev.birdy.model.MyFirebase.queryFriend
import com.blockdev.birdy.model.User
import com.blockdev.birdy.ui.ChatActivity
import com.google.firebase.auth.FirebaseAuth

class FragmentChat : Fragment() {
    private var adapter: UserAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_chat, container, false)
        val recyclerView = rootView.findViewById<View>(R.id.chat_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        adapter = UserAdapter(
            User::class.java,
            R.layout.viewholder_user_chat,
            UserViewHolder::class.java,
            queryFriend(myUid),
        )
        recyclerView.adapter = adapter
        return rootView
    }

    override fun onStart() {
        super.onStart()
        adapter!!.setOnItemClickListener(object : OnRecyclerItemClick {
            override fun onItemClick(v: View?, position: Int) {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    // go to chat place
                    startActivity(
                        Intent(activity, ChatActivity::class.java)
                            .putExtra("chatWith", adapter!!.getRef(position).key),
                    )
                }
            }
        })
    }
}
