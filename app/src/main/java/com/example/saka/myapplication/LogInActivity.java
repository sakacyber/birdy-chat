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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView txtSignUp;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        edtEmail = (EditText) findViewById(R.id.li_edt_name);
        edtPassword = (EditText) findViewById(R.id.li_edt_password);
        txtSignUp = (TextView) findViewById(R.id.li_txt_register);
        btnLogin = (Button) findViewById(R.id.li_btn);
        txtSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            signIn();
        }
    }

    private void signIn(){
        if (validateForm()) {
            String enterEmail = edtEmail.getText().toString();
            String enterPassword = edtPassword.getText().toString();
            auth.signInWithEmailAndPassword(enterEmail, enterPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Log.d("bidy: logInActivity", "sign in failed");
                                Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean validateForm(){
        boolean valid = true;
        //Validate Password
        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(password)){
            edtPassword.setError("Enter password");
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        //Validate Email
        String email = edtEmail.getText().toString();
        if (TextUtils.isEmpty(email)){
            edtEmail.setError("Enter email");
            valid = false;
        } else {
            edtEmail.setError(null);
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.li_txt_register){
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        } else if (id == R.id.li_btn){
            signIn();
            Log.d("birdy: logInActivity", "signed in");
        }
    }
}
