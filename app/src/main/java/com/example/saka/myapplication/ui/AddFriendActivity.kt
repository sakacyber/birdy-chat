package com.example.saka.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.saka.myapplication.R
import com.example.saka.myapplication.model.MyConstant
import com.example.saka.myapplication.model.MyFirebase
import com.example.saka.myapplication.model.MyFirebase.getUser
import com.example.saka.myapplication.model.MyFirebase.queryUser
import com.example.saka.myapplication.model.User
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener

class AddFriendActivity : AppCompatActivity() {

    private var databaseReference: DatabaseReference? = null
    var myUid = MyFirebase.myUid ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        val layout = findViewById<View>(R.id.af_layout) as LinearLayout
        val recyclerView = findViewById<View>(R.id.af_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)
        databaseReference = FirebaseDatabase.getInstance().reference
        val adapter: FirebaseRecyclerAdapter<User, FriendViewHolder> =
            object : FirebaseRecyclerAdapter<User, FriendViewHolder>(
                User::class.java,
                R.layout.viewholder_add_friend,
                FriendViewHolder::class.java,
                queryUser(),
            ) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): FriendViewHolder {
                    val view =
                        LayoutInflater.from(parent.context).inflate(mModelLayout, parent, false)
                    return FriendViewHolder(view)
                }

                override fun populateViewHolder(
                    viewHolder: FriendViewHolder,
                    model: User,
                    position: Int,
                ) {
                    val userRef = getRef(position)
                    viewHolder.itemView.setOnClickListener { // // TODO: 05-Jun-17 go to profile activity
                        val yourRef = databaseReference!!.child(MyConstant.USERS)
                            .child(userRef.key!!)
                        val myRef = databaseReference!!.child(MyConstant.USERS)
                            .child(myUid!!)
                        when (viewHolder.request.text) {
                            "requested" -> {
                                checkRequestU(yourRef)
                                checkRequestMe(myRef, yourRef)
                            }
                            "Click to accept" -> {
                                accept(myRef, yourRef)
                            }
                            "Click to send request" -> {
                                checkRequestU(yourRef)
                                checkRequestMe(myRef, yourRef)
                            }
                        }
                    }
                    checkUser(viewHolder, model)
                    viewHolder.bindToFriend(model)
                }
            }
        recyclerView.adapter = adapter
    }

    private fun checkUser(viewHolder: FriendViewHolder, model: User) {
        // check if you
        if (model.uid.contentEquals(myUid)) {
            viewHolder.name.setTextColor(resources.getColor(R.color.colorAccent))
            viewHolder.request.setTextColor(resources.getColor(R.color.colorAccent))
            viewHolder.request.text = "You"
        }

        // check if you already request
        if (model.requestFrom.containsKey(myUid)) {
            viewHolder.request.text = "requested"
        }

        // check if they request you
        if (model.requestTo.containsKey(myUid)) {
            viewHolder.request.text = "Click to accept"
        }

        // check if friend
        if (model.friend.containsKey(myUid)) {
            viewHolder.request.text = "Friend"
            viewHolder.name.setTextColor(resources.getColor(R.color.colorPrimary))
            viewHolder.request.setTextColor(resources.getColor(R.color.colorPrimary))
        }
    }

    private fun checkRequestU(yourRef: DatabaseReference) {
        yourRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val user = mutableData.getValue(
                    User::class.java,
                )
                    ?: return Transaction.success(mutableData)
                if (user.requestFrom.containsKey(myUid)) {
                    // cancel request and remove request from user
                    user.requestFromCount = user.requestFromCount - 1
                    user.requestFrom.remove(myUid)
                } else {
                    // send request to user and add request
                    user.requestFromCount = user.requestFromCount + 1
                    user.requestFrom[myUid] = true
                }
                // set value and report transaction success
                mutableData.value = user
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                b: Boolean,
                dataSnapshot: DataSnapshot?,
            ) {
            }
        })
    }

    private fun checkRequestMe(myRef: DatabaseReference, yourRef: DatabaseReference) {
        myRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val user = mutableData.getValue(
                    User::class.java,
                )
                    ?: return Transaction.success(mutableData)
                if (user.requestTo.containsKey(yourRef.key)) {
                    // cancel request and remove request from user
                    user.requestToCount = user.requestToCount - 1
                    user.requestTo.remove(yourRef.key)
                } else {
                    // send request to user and add request
                    user.requestToCount = user.requestToCount + 1
                    user.requestTo[yourRef.key ?: "key"] = true
                }
                // set value and report transaction success
                mutableData.value = user
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                b: Boolean,
                dataSnapshot: DataSnapshot?,
            ) {
            }
        })
    }

    private fun accept(myRef: DatabaseReference, yourRef: DatabaseReference) {
        myRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(
                        User::class.java,
                    )
                    val friend: MutableMap<String, Any> = HashMap()
                    if (user!!.requestFrom.containsKey(yourRef.key)) {
                        user.requestFromCount = user.requestFromCount - 1
                        user.requestFrom.remove(yourRef.key)
                    }
                    myRef.setValue(user)
                    friend["name"] = getUser(yourRef.key).name ?: "name"
                    databaseReference!!.child(MyConstant.USERS).child(myUid!!).child("friend")
                        .child(yourRef.key!!).setValue(friend)
                    databaseReference!!.child(MyConstant.USERS).child(myUid!!).child("zChat")
                        .child(yourRef.key!!).setValue("")
                    Log.d("onAccept:onDataChange", "update myRef success")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("onAccept", databaseError.details)
                }
            },
        )
        yourRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(
                        User::class.java,
                    )
                    val friend: MutableMap<String, Any> = HashMap()
                    if (user!!.requestTo.containsKey(myRef.key)) {
                        user.requestToCount = user.requestToCount - 1
                        user.requestTo.remove(myRef.key)
                    }
                    yourRef.setValue(user)
                    friend["name"] = getUser(myUid).name ?: "name"
                    databaseReference!!.child(MyConstant.USERS).child(yourRef.key!!).child("friend")
                        .child(myUid!!).setValue(friend)
                    databaseReference!!.child(MyConstant.USERS).child(yourRef.key!!).child("zChat")
                        .child(myUid!!).setValue("")
                    Log.d("onAccept:onDataChange", "update yourRef success")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("onAccept", databaseError.details)
                }
            },
        )
        Log.d("onAccept", "accept success")
    }

    private inner class FriendViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imgProfile: ImageView
        val name: TextView
        val request: TextView

        init {
            imgProfile = itemView.findViewById<View>(R.id.vh_add_friend_img) as ImageView
            name = itemView.findViewById<View>(R.id.vh_add_friend_txt_name) as TextView
            request = itemView.findViewById<View>(R.id.vh_add_friend_txt_request) as TextView
        }

        fun bindToFriend(user: User) {
            name.text = user.name
        }
    }
}
