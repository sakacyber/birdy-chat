package com.blockdev.birdy.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blockdev.birdy.model.MyConstant
import com.blockdev.birdy.model.Post
import com.google.firebase.database.FirebaseDatabase

class FirebaseViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val reference = database.reference.child(MyConstant.POSTS)

    fun getData(): LiveData<List<Post>> {
        val liveData = MutableLiveData<List<Post>>()
        reference.get()
            .addOnSuccessListener { snapshot ->
                val list = mutableListOf<Post>()
                for (doc in snapshot.children) {
                    val post = doc.getValue(Post::class.java)
                    if (post != null) {
                        list.add(post)
                    }
                }
                liveData.postValue(list)
            }.addOnFailureListener { error ->
                Log.e("FirebaseViewModel", "Error fetching data from Firebase", error)
            }
        return liveData
    }
}
