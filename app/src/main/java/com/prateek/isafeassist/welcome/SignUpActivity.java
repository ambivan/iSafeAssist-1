package com.prateek.isafeassist.welcome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.prateek.isafeassist.MainActivity;
import com.prateek.isafeassist.R;

public class SignUpActivity extends AppCompatActivity {

    EditText name, mailid, pass, confirmpass;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.signup_name);
        mailid = findViewById(R.id.signup_email);
        pass = findViewById(R.id.signup_pass);
        confirmpass = findViewById(R.id.signup_confirm_pass);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString() == null || mailid.getText().toString() == null || pass.getText().toString() == null
                        || confirmpass.getText().toString() == null || name.getText().toString().length() <= 0 || mailid.getText().toString().length() < 0 || pass.getText().toString().length() <= 0
                        || confirmpass.getText().toString().length() <= 0) {
                    Toast.makeText(SignUpActivity.this, "Enter All the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (!pass.getText().toString().equals(confirmpass.getText().toString())) {
                        Toast.makeText(SignUpActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}
