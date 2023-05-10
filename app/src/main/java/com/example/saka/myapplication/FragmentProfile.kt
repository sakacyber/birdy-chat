package com.example.saka.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.saka.myapplication.adaper.OptionAdapter
import com.example.saka.myapplication.model.MyFirebase.getUser
import com.example.saka.myapplication.model.MyFirebase.myUid
import com.example.saka.myapplication.ui.AddFriendActivity
import com.example.saka.myapplication.ui.LogInActivity
import com.example.saka.myapplication.ui.NewPostActivity
import com.google.firebase.auth.FirebaseAuth

class FragmentProfile : Fragment(), View.OnClickListener, OnItemClickListener {

    private val options = arrayOf(
        "New post",
        "Friend request",
        "Friends",
        "Settings",
        "About",
        "Log out"
    )
    private val icons = intArrayOf(
        R.drawable.ic_new_post,
        R.drawable.ic_add_circle,
        R.drawable.ic_friend,
        R.drawable.ic_settings,
        R.drawable.ic_about,
        R.drawable.ic_log_out,
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        val profileLayout = rootView.findViewById<View>(R.id.pf_bar) as RelativeLayout
        val listView = rootView.findViewById<RecyclerView>(R.id.pf_list_view)
        val myName = rootView.findViewById<View>(R.id.pf_txt_name) as TextView
        val email = rootView.findViewById<View>(R.id.pf_txt_email) as TextView
        profileLayout.setOnClickListener(this)
        myName.text = getUser(myUid).name
        email.text = FirebaseAuth.getInstance().currentUser!!.email
        val adapter = OptionAdapter(options, icons)
        listView.adapter = adapter
        return rootView
    }

    override fun onClick(v: View) {
        if (v.id == R.id.pf_bar) {
            // // TODO: 05-Jun-17 create activity profile
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        when (position) {
            0 -> startActivity(Intent(activity, NewPostActivity::class.java))
            1 -> startActivity(Intent(activity, AddFriendActivity::class.java))
            2 -> {}
            5 -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(activity, LogInActivity::class.java))
                requireActivity().finish()
            }
        }
    }
} // /////////////////////////////////////////////////////////////////////////////////////////////
