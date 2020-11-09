package com.example.project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.utils.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_logIn;
    private Button btn_signUp;
    private TextView mEmail;
    private TextView mPassword;
    FirebaseAuth mAuth;
    DatabaseReference mFdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        btn_signUp = findViewById(R.id.btn_sign_up);
        btn_signUp.setOnClickListener(this);
        btn_logIn = findViewById(R.id.btn_login);
        btn_logIn.setOnClickListener(this);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mAuth = Database.mAuth;
        mFdatabase = FirebaseDatabase.getInstance().getReference();

    }

    public void onStart() {
        super.onStart();
    }

    private void signIn() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        FirebaseUser user = mAuth.getCurrentUser();

        if(email.isEmpty()){
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }
        else{
            mEmail.setError(null);
        }

        if(password.isEmpty()){
            mPassword.setError("Password is required!");
            mPassword.requestFocus();
            return;
        }else{
            mPassword.setError(null);
        }
        if(user!=null){
            Toast.makeText(LogInActivity.this, "User has been signed in!", Toast.LENGTH_SHORT).show();
            return;

        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LogIn", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(!user.isEmailVerified()){
                                mAuth.signOut();
                                mEmail.setError("Invalid Email");
                                mEmail.requestFocus();
                                return;
                            }else{
                                mEmail.setError(null);
                            }
                            //check if the user has been store into the database;
                            Database.createUser();
                            Intent toMain = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(toMain);
//                            Toast.makeText(LogInActivity.this,user.toString(),Toast.LENGTH_SHORT).show();

//
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LogIn", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_up:
                Intent toSignUp = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(toSignUp);
                return;
            case R.id.btn_login:
                signIn();
//                Intent toMain = new Intent(LogInActivity.this, MainActivity.class);
//                startActivity(toMain);
                return;


        }
    }
}