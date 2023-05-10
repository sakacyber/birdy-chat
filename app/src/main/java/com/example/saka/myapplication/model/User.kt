package com.example.saka.myapplication.model

/**
 * Created by Saka on 02-May-17.
 */
class User {
    var uid: String? = null
    var name: String? = null
    var email: String? = null
    var friendCount = 0
    var requestToCount = 0
    var requestFromCount = 0
    var requestTo: MutableMap<String, Any> = HashMap()
    var friend: MutableMap<String, Any> = HashMap()
    var requestFrom: MutableMap<String, Any> = HashMap()

    constructor() {
        //  Default constructor
    }

    constructor(uid: String?, name: String?, email: String?) {
        this.uid = uid
        this.name = name
        this.email = email
    }

    fun toMap(): Map<String, Any?> {
        val map: MutableMap<String, Any?> = HashMap()
        map["uid"] = uid
        map["name"] = name
        map["email"] = email
        map["friendCount"] = friendCount
        map["requestFromCount"] = requestFromCount
        return map
    }
}
