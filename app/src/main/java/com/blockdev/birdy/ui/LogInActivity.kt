package com.blockdev.birdy.ui

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
import com.blockdev.birdy.R
import com.blockdev.birdy.ui.main.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity(), View.OnClickListener {
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
        setContentView(R.layout.activity_log_in)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val actionBar = supportActionBar
        actionBar?.hide()
        edtEmail = findViewById(R.id.li_edt_name)
        edtPassword = findViewById(R.id.li_edt_password)
        val txtSignUp = findViewById<TextView>(R.id.li_txt_register)
        val btnLogin = findViewById<Button>(R.id.li_btn)
        txtSignUp.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        if (auth!!.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        } else {
            signIn()
        }
    }

    private fun signIn() {
        if (validateForm()) {
            val enterEmail = edtEmail!!.text.toString()
            val enterPassword = edtPassword!!.text.toString()
            auth!!.signInWithEmailAndPassword(enterEmail, enterPassword)
                .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    } else {
                        Log.d("birdy: logInActivity", "sign in failed")
                        Toast.makeText(
                            applicationContext,
                            "Authentication Failed",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        // Validate Password
        val password = edtPassword!!.text.toString()
        if (TextUtils.isEmpty(password)) {
            edtPassword!!.error = "Enter password"
            valid = false
        } else {
            edtPassword!!.error = null
        }
        // Validate Email
        val email = edtEmail!!.text.toString()
        if (TextUtils.isEmpty(email)) {
            edtEmail!!.error = "Enter email"
            valid = false
        } else {
            edtEmail!!.error = null
        }
        return valid
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.li_txt_register) {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
            finish()
        } else if (id == R.id.li_btn) {
            signIn()
            Log.d("birdy: logInActivity", "signed in")
        }
    }
}
