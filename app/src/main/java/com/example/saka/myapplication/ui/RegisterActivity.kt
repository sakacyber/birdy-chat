package com.example.saka.myapplication.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.saka.myapplication.R
import com.example.saka.myapplication.model.MyFirebase.writeNewUser
import com.example.saka.myapplication.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    private var edtName: EditText? = null
    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val actionBar = supportActionBar
        actionBar?.hide()
        edtName = findViewById<View>(R.id.su_edt_name) as EditText
        edtEmail = findViewById<View>(R.id.su_edt_mail) as EditText
        edtPassword = findViewById<View>(R.id.su_edt_password) as EditText
        val btnSignUp = findViewById<View>(R.id.su_btn_sign_up) as Button
        val txtLogIn = findViewById<View>(R.id.su_txt_login) as TextView
        auth = FirebaseAuth.getInstance()
        btnSignUp.setOnClickListener {
            val enterEmail = edtEmail!!.text.toString()
            val enterPassword = edtPassword!!.text.toString()
            createNewUser(enterEmail, enterPassword)
        }
        txtLogIn.setOnClickListener {
            startActivity(Intent(applicationContext, LogInActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth!!.currentUser != null) {
            onAuthSuccess(auth!!.currentUser)
        }
    }

    private fun createNewUser(email: String, password: String) {
        if (!validateForm()) {
            return
        }
        auth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(applicationContext, "Create account failed", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    onAuthSuccess(task.result.user)
                }
            }
    }

    private fun onAuthSuccess(user: FirebaseUser?) {
        // write new user
        val username = edtName!!.text.toString()
        val newUser = User(
            user!!.uid,
            username,
            user.email,
        )
        writeNewUser(user.uid, newUser)
        Log.d("REGISTER", "write new user success")
        startActivity(
            Intent(applicationContext, MainActivity::class.java)
                .putExtra("name", edtName!!.text.toString()),
        )
        finish()
    }

    private fun validateForm(): Boolean {
        var valid = true
        // Validate Password
        val password = edtPassword!!.text.toString()
        if (TextUtils.isEmpty(password)) {
            edtPassword!!.error = "Create your new password"
            valid = false
        } else {
            edtPassword!!.error = null
        }
        // Validate Email
        val phone = edtEmail!!.text.toString()
        if (TextUtils.isEmpty(phone)) {
            edtEmail!!.error = "Enter your email address"
            valid = false
        } else {
            edtEmail!!.error = null
        }
        // Validate Email
        val name = edtName!!.text.toString()
        if (TextUtils.isEmpty(name)) {
            edtName!!.error = "Enter your real name"
            valid = false
        } else {
            edtName!!.error = null
        }
        return valid
    }
}
