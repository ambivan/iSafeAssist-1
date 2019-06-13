package com.prateek.isafeassist.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.prateek.isafeassist.MainActivity;
import com.prateek.isafeassist.R;

public class SignUpActivity extends AppCompatActivity {

    EditText name, mailid, pass, confirmpass;
    Button signup;
    ProgressDialog progressBar;
    FirebaseAuth firebaseAuth;
    Boolean checkstatus;
    private  String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.signup_name);
        mailid = findViewById(R.id.signup_email);
        pass = findViewById(R.id.signup_pass);
        confirmpass = findViewById(R.id.signup_confirm_pass);
        signup= findViewById(R.id.signup);
        firebaseAuth= FirebaseAuth.getInstance();
        progressBar= new ProgressDialog(SignUpActivity.this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckFieldStatus();
                if(checkstatus){
/*

*/

                    UserRegistrationFunction();
                }else{
                    Toast.makeText(SignUpActivity.this, "Enter All the fields", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void CheckFieldStatus(){

        email= mailid.getText().toString();
        password= pass.getText().toString();

        if (name.getText().toString() == null || mailid.getText().toString() == null || pass.getText().toString() == null
                || confirmpass.getText().toString() == null || name.getText().toString().length() <= 0 || mailid.getText().toString().length() < 0 || pass.getText().toString().length() <= 0
                || confirmpass.getText().toString().length() <= 0) {
            checkstatus= false;
            mailid.setError("Check all Fields");
            mailid.requestFocus();
        } else {
            if (!pass.getText().toString().equals(confirmpass.getText().toString())) {
                Toast.makeText(SignUpActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();

            } else {
                checkstatus= true;
            }
        }

    }

    public void UserRegistrationFunction(){



        // Showing progress dialog at user registration time.
        progressBar.setMessage("Please Wait, We are Registering Your Data on Server");
        progressBar.show();

        // Creating createUserWithEmailAndPassword method and pass email and password inside it.
        firebaseAuth.createUserWithEmailAndPassword(mailid.getText().toString(), pass.getText().toString()).
                addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.dismiss();

                        // Checking if user is registered successfully.
                        if(task.isSuccessful()){


                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            // If user registered successfully then show this toast message.
                            Toast.makeText(SignUpActivity.this,"User Registration Successfully",Toast.LENGTH_LONG).show();

                        }else{

                            // If something goes wrong.
                            Toast.makeText(SignUpActivity.this,"Something Went Wrong.",Toast.LENGTH_LONG).show();
                        }

                        // Hiding the progress dialog after all task complete.

                    }
                });

    }



}
