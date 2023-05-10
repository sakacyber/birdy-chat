package com.example.saka.myapplication.db

import android.app.Application
import com.google.firebase.FirebaseApp

class BirdyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}
