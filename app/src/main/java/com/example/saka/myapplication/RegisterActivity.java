package com.example.saka.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saka.myapplication.model.User;
import com.example.saka.myapplication.model.myFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        edtName = (EditText) findViewById(R.id.su_edt_name);
        edtEmail = (EditText) findViewById(R.id.su_edt_mail);
        edtPassword = (EditText) findViewById(R.id.su_edt_password);
        Button btnSignUp = (Button) findViewById(R.id.su_btn_sign_up);
        TextView txtLogIn = (TextView) findViewById(R.id.su_txt_login);

        auth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enterEmail = edtEmail.getText().toString();
                String enterPassword= edtPassword.getText().toString();
                createNewUser(enterEmail, enterPassword);
            }
        });

        txtLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null){
            onAuthSuccess(auth.getCurrentUser());
        }
    }

    private void createNewUser(String email, String password){
        if (!validateForm()){
            return;
        }
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Create account failed", Toast.LENGTH_SHORT).show();
                        } else {
                            onAuthSuccess(task.getResult().getUser());
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user){
        // write new user
        String username = edtName.getText().toString();
        User newUser = new User(user.getUid(), username, user.getEmail());
        myFirebase.writeNewUser(user.getUid(), newUser);
        Log.d("REGISTER", "write new user success");

        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                .putExtra("name", edtName.getText().toString()));
        finish();
    }

    private boolean validateForm(){
        boolean valid = true;
        //Validate Password
        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(password)){
            edtPassword.setError("Create your new password");
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        //Validate Email
        String phone = edtEmail.getText().toString();
        if (TextUtils.isEmpty(phone)){
            edtEmail.setError("Enter your email address");
            valid = false;
        } else {
            edtEmail.setError(null);
        }
        //Validate Email
        String name = edtName.getText().toString();
        if (TextUtils.isEmpty(name)){
            edtName.setError("Enter your real name");
            valid = false;
        } else {
            edtName.setError(null);
        }
        return valid;
    }
}
