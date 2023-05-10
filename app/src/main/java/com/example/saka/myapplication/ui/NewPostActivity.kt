package com.example.saka.myapplication.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.saka.myapplication.R
import com.example.saka.myapplication.model.MyFirebase.getUser
import com.example.saka.myapplication.model.MyFirebase.myUid
import com.example.saka.myapplication.model.MyFirebase.newPushKey
import com.example.saka.myapplication.model.MyFirebase.updateMyChildren
import com.example.saka.myapplication.model.Post
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NewPostActivity : AppCompatActivity() {
    private var edtTitle: EditText? = null
    private var edtBody: EditText? = null
    private var fab: FloatingActionButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        edtTitle = findViewById<View>(R.id.np_edt_title) as EditText
        edtBody = findViewById<View>(R.id.np_edt_body) as EditText
        fab = findViewById<View>(R.id.np_fab) as FloatingActionButton
        fab!!.setOnClickListener {
            submitPost()
            finish()
        }
    }

    private fun submitPost() {
        if (validatePost()) {
            // Disable button so there are no multi-post
            setEditingEnabled(false)
            Toast.makeText(applicationContext, "Posting...", Toast.LENGTH_SHORT).show()

            // Start write new post
            val uid = myUid
            val username = getUser(myUid).name ?: "name"
            val title = edtTitle!!.text.toString()
            val body = edtBody!!.text.toString()
            writeNewPost(uid, username, title, body)
        }
    }

    private fun writeNewPost(uid: String?, author: String?, title: String, body: String) {
        // Create new post at /user-posts/$userId/$postId and at
        // /posts/$postId simultaneously
        val key = newPushKey
        val post = Post(uid, author, title, body)
        val newPost = post.toMap()
        val childUpdates: MutableMap<String?, Any?> = HashMap()
        childUpdates["/POSTS/$key"] = newPost
        childUpdates["/USER-POSTS/$uid/$key"] = newPost
        updateMyChildren(childUpdates)
    }

    private fun setEditingEnabled(enabled: Boolean) {
        edtTitle!!.isEnabled = enabled
        edtBody!!.isEnabled = enabled
        if (enabled) {
            fab!!.visibility = View.VISIBLE
        } else {
            fab!!.visibility = View.GONE
        }
    }

    private fun validatePost(): Boolean {
        val title = edtTitle!!.text.toString()
        val body = edtBody!!.text.toString()
        if (TextUtils.isEmpty(title)) {
            edtTitle!!.error = "Required"
            return false
        }
        if (TextUtils.isEmpty(body)) {
            edtBody!!.error = "Required"
            return false
        }
        return true
    }
}
