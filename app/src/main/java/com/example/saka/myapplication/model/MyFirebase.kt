package com.example.saka.myapplication.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

/**
 * Created by Saka on 04-Jun-17.
 */
object MyFirebase {

    private var auth = FirebaseAuth.getInstance()
    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var user = User()

    @JvmStatic
    val myUid: String?
        get() {
            auth = FirebaseAuth.getInstance()
            return if (auth.currentUser != null) {
                auth.currentUser!!.uid
            } else {
                null
            }
        }

    @JvmStatic
    val newPushKey: String?
        get() {
            databaseReference = FirebaseDatabase.getInstance().reference
            return databaseReference.push().key
        }

    fun signMeIn(email: String?, password: String?) {
        auth.signInWithEmailAndPassword(email!!, password!!)
    }

    fun signMeOut() {
        auth.signOut()
    }

    @JvmStatic
    fun queryUser(): Query {
        return databaseReference.child(MyConstant.USERS).limitToFirst(50)
    }

    @JvmStatic
    fun queryFriend(uid: String?): Query {
        return databaseReference.child(MyConstant.USERS).child(uid!!)
            .child(MyConstant.FRIEND).limitToFirst(50)
    }

    fun queryRecentPost(): Query {
        return databaseReference.child(MyConstant.POSTS).limitToFirst(50)
    }

    @JvmStatic
    fun updateMyChildren(childUpdate: Map<String?, Any?>?) {
        databaseReference.updateChildren(childUpdate!!)
    }

    @JvmStatic
    fun getUser(uid: String?): User {
        databaseReference.child(MyConstant.USERS).child(uid!!).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val u = dataSnapshot.getValue(
                        User::class.java,
                    )
                    if (u != null) {
                        user = u
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("birdy", "can not get user: " + databaseError.details)
                }
            },
        )
        return user
    }

    @JvmStatic
    fun writeNewUser(uid: String?, userValue: User?) {
        databaseReference.child(MyConstant.USERS).child(uid!!).setValue(userValue)
    }
}
