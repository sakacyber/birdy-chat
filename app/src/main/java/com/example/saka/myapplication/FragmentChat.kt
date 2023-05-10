package com.example.saka.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.saka.myapplication.adaper.UserAdapter
import com.example.saka.myapplication.adaper.UserAdapter.OnRecyclerItemClick
import com.example.saka.myapplication.adaper.UserAdapter.UserViewHolder
import com.example.saka.myapplication.model.MyFirebase.myUid
import com.example.saka.myapplication.model.MyFirebase.queryFriend
import com.example.saka.myapplication.model.User
import com.example.saka.myapplication.ui.ChatActivity
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
