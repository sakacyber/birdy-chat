package com.example.saka.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView txtSignUp;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

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

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        txtSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Main2.class));
            finish();
        } else {
            signIn();
        }
    }

    private void signIn(){
        if (!validateForm()){
            return;
        }
        showProgressDialog();
        String enterEmail = edtEmail.getText().toString();
        String enterPassword = edtPassword.getText().toString();
        auth.signInWithEmailAndPassword(enterEmail, enterPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if(task.isSuccessful()){
                            //onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user){
        String username = usernameFromEmail(user.getEmail());
        // write new user
        User newUser = new User(user.getUid(), username, user.getEmail());
        databaseReference.child("users").child(user.getUid()).setValue(newUser);

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private String usernameFromEmail(String email){
        if (email.contains("@")){
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm(){
        boolean valid = true;
        //Validate Password
        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(password)){
            edtPassword.setError("Enter your password to log in");
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        //Validate Email
        String email = edtEmail.getText().toString();
        if (TextUtils.isEmpty(email)){
            edtEmail.setError("Enter your Email to log in");
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
            Log.d("sign in", "passed sign in");
        }
    }
}
