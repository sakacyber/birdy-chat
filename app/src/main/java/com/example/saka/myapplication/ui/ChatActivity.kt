package com.example.saka.myapplication.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.saka.myapplication.R
import com.example.saka.myapplication.adaper.ChatAdapter
import com.example.saka.myapplication.model.Message
import com.example.saka.myapplication.model.MyConstant
import com.example.saka.myapplication.model.MyFirebase
import com.example.saka.myapplication.model.MyFirebase.getUser
import com.example.saka.myapplication.model.MyFirebase.newPushKey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class ChatActivity : AppCompatActivity(), View.OnClickListener {
    private var edtMessage: EditText? = null
    private var messageContainer: ListView? = null
    private var adapter: ChatAdapter? = null
    private var databaseReference: DatabaseReference? = null
    private val chatHistory: MutableList<Message> = ArrayList()
    private val myUid = MyFirebase.myUid
    private var chatWith: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        messageContainer = findViewById<View>(R.id.chat_messageContainer) as ListView
        edtMessage = findViewById<View>(R.id.chat_edt_msg) as EditText
        val btnSend = findViewById<View>(R.id.chat_btn_send) as ImageButton
        databaseReference = FirebaseDatabase.getInstance().reference
        messageContainer!!.divider = null
        chatWith = intent.getStringExtra("chatWith")
        title = getUser(chatWith).name
        adapter = ChatAdapter(applicationContext, chatHistory)
        messageContainer!!.adapter = adapter
        btnSend.setOnClickListener(this)
        databaseReference!!.child(MyConstant.USERS).child(myUid!!).child("zChat").child(chatWith!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    loadConversation(dataSnapshot)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun scroll() {
        messageContainer!!.setSelection(messageContainer!!.lastVisiblePosition)
    }

    private fun loadConversation(dataSnapshot: DataSnapshot) {
        val tempMessages: MutableList<Message> = ArrayList()
        for (`object` in dataSnapshot.children) {
            val msg = (`object` as DataSnapshot).getValue(
                Message::class.java,
            )
            if (msg != null) {
                tempMessages.add(msg)
            }
        }
        chatHistory.clear()
        chatHistory.addAll(tempMessages)
        adapter!!.notifyDataSetChanged()
        tempMessages.clear()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.chat_btn_send) {
            val messageText = edtMessage!!.text.toString()
            if (TextUtils.isEmpty(messageText)) {
                return
            }
            sendMessage(messageText)
        }
    }

    private fun sendMessage(messageText: String) {
        val key = newPushKey
        val message = Message()
        message.id = key
        message.message = messageText
        message.timeStamp = SimpleDateFormat.getDateTimeInstance().format(Date())
        message.senderUid = myUid
        databaseReference!!.child(MyConstant.USERS).child(myUid!!).child("zChat")
            .child(chatWith!!).child(key!!).setValue(message)
        databaseReference!!.child(MyConstant.USERS).child(chatWith!!).child("zChat")
            .child(myUid).child(key).setValue(message)
        edtMessage!!.setText("")
        scroll()
    }
}
