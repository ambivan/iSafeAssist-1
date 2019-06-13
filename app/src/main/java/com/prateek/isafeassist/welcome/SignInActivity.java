package com.prateek.isafeassist.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.SigningInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.prateek.isafeassist.MainActivity;
import com.prateek.isafeassist.R;

public class SignInActivity extends AppCompatActivity {

    EditText email, pass;
    Button signIn;
    FirebaseAuth auth;

    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = findViewById(R.id.login_email);
        pass = findViewById(R.id.login_pass);
        signIn = findViewById(R.id.signin);
        auth = FirebaseAuth.getInstance();
        progressBar = new ProgressDialog(SignInActivity.this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginemail = email.getText().toString();
                final String password = pass.getText().toString();

                if (TextUtils.isEmpty(loginemail)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Showing progress dialog at user registration time.
                progressBar.setMessage("Please Wait.Logging In...");
                progressBar.show();
                progressBar.setCancelable(false);
                auth.signInWithEmailAndPassword(loginemail, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.dismiss();
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        pass.setError("Enter Correct password");
                                    } else {
                                        Toast.makeText(SignInActivity.this, "SignIn failed", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
