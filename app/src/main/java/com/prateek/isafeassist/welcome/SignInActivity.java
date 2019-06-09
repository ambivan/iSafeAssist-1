package com.prateek.isafeassist.welcome;

import android.content.Intent;
import android.content.pm.SigningInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.prateek.isafeassist.MainActivity;
import com.prateek.isafeassist.R;

public class SignInActivity extends AppCompatActivity {

    EditText email, pass;
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = findViewById(R.id.login_email);
        pass = findViewById(R.id.login_pass);
        signIn = findViewById(R.id.signin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString() == null || pass.getText().toString() == null || email.getText().toString().length() <= 0 ||
                        pass.getText().toString().length() <= 0) {
                    Toast.makeText(SignInActivity.this, "Enter both the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
