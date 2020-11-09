package com.example.project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_signUp;
//    private EditText edt_username;
    private EditText edt_email;
    private EditText edt_password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        btn_signUp = findViewById(R.id.btn_register);
        btn_signUp.setOnClickListener(this);
        edt_email = findViewById(R.id.reg_email);
        edt_password = findViewById(R.id.reg_password);
    }

    private void sendEmailVerification(final FirebaseUser user) {
        // Disable button
//        mBinding.verifyEmailButton.setEnabled(false);

        // Send verification email
        // [START send_email_verification]
//        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
//                        mBinding.verifyEmailButton.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("SignUpActivity", "sendEmailVerification", task.getException());
                            Toast.makeText(SignUpActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }
    private void createAccount() {
//        final String username = edt_username.getText().toString().trim();
        final String email = edt_email.getText().toString().trim();
        final String password = edt_password.getText().toString().trim();
        Log.d("SignUpActivity", "createAccount:" + email);
//        if(username.isEmpty()){
//            edt_username.setError("Username is required!");
//            edt_username.requestFocus();
//            return;
//        }
        if(email.isEmpty()){
            edt_email.setError("Email is required!");
            edt_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_email.setError("Please provide valid email!");
            edt_email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            edt_password.setError("Password is required!");
            edt_password.requestFocus();
            return;
        }
        if(password.length()<6){
            edt_password.setError("Min password length should be 6 characters!");
            edt_password.requestFocus();
            return;
        }

//        showProgressBar();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignUpActivity", "createUserWithEmail:success");

                            FirebaseUser account = mAuth.getCurrentUser();
                            sendEmailVerification(account);
                            mAuth.signOut();
                            Intent toSignIn = new Intent(SignUpActivity.this, LogInActivity.class);
                            startActivity(toSignIn);
//                            User user = new User(account.getUid(),username,email,password);
//                            updateUI(user);
//                            sendEmailVerification(account);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

//    private boolean

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                createAccount();
//                Intent toSignIn = new Intent(SignUpActivity.this, LogInActivity.class);
//                startActivity(toSignIn);

        }
    }



}