package com.example.saka.myapplication.model

class Post {

    var uid: String? = null
    var author: String? = null
    var title: String? = null
    var body: String? = null
    var starCount = 0
    var stars: MutableMap<String, Boolean> = HashMap()

    constructor() {
        //  Default constructor
    }

    constructor(uid: String?, author: String?, title: String?, body: String?) {
        this.uid = uid
        this.author = author
        this.title = title
        this.body = body
    }

    fun toMap(): Map<String, Any?> {
        val map: MutableMap<String, Any?> = HashMap()
        map["uid"] = uid
        map["author"] = author
        map["title"] = title
        map["body"] = body
        map["starCount"] = starCount
        // map.put("stars", stars);
        return map
    }
}
