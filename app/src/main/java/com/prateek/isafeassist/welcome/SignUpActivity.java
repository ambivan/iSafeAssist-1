package com.prateek.isafeassist.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prateek.isafeassist.MainActivity;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.fragments.PaymentFragment;
import com.prateek.isafeassist.model.UserDetails;

public class SignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText name, mailid, pass, confirmpass, contact;
    Button signup;
    ProgressDialog progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Boolean checkstatus;
    private String email, password;
    private SignInButton button;
    private GoogleApiClient apiClient;
    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.signup_name);
        mailid = findViewById(R.id.signup_email);
        pass = findViewById(R.id.signup_pass);
        confirmpass = findViewById(R.id.signup_confirm_pass);
        contact = findViewById(R.id.signup_contact);
        signup = findViewById(R.id.signup);
        button = findViewById(R.id.google_signup);
        Window window = SignUpActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(SignUpActivity.this, R.color.mystatus));
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        apiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();


        progressBar = new ProgressDialog(SignUpActivity.this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //CheckFieldStatus();
                if (name.getText().toString() == null || mailid.getText().toString() == null || pass.getText().toString() == null
                        || confirmpass.getText().toString() == null || name.getText().toString().length() <= 0 || mailid.getText().toString().length() < 0 || pass.getText().toString().length() <= 0
                        || confirmpass.getText().toString().length() <= 0) {
                    checkstatus = false;
                    mailid.setError("Check all Fields");
                    mailid.requestFocus();
                } else {
                    if (!pass.getText().toString().equals(confirmpass.getText().toString())) {
                        Toast.makeText(SignUpActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();

                    } else if (contact.length() < 10 || contact.length() > 10) {
                        contact.setError("Invalid Number");

                    } else {
                        checkstatus = true;
                    }
                }

                if (checkstatus) {

                    UserRegistrationFunction();
                } else {
                    Toast.makeText(SignUpActivity.this, "Enter All the fields correctly", Toast.LENGTH_SHORT).show();

                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SignIn();
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    public void UserRegistrationFunction() {


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
                        if (task.isSuccessful()) {

                            final UserDetails details = new UserDetails();
                            details.setEmail(mailid.getText().toString());
                            details.setPassword(pass.getText().toString());
                            details.setName(name.getText().toString());
                            details.setContactNo(contact.getText().toString());
                            databaseReference.child("User").child(firebaseAuth.getCurrentUser().getUid())/*.push()*/.setValue(details, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError == null) {

                                        Toast.makeText(SignUpActivity.this, "Data saved Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            //safeinfo();


                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            // If user registered successfully then show this toast message.
                            Toast.makeText(SignUpActivity.this, "User Registration Successful", Toast.LENGTH_LONG).show();

                        } else {

                            // If something goes wrong.
                            Toast.makeText(SignUpActivity.this, "Something Went Wrong.", Toast.LENGTH_LONG).show();
                        }

                        // Hiding the progress dialog after all task complete.

                    }
                });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void handleresult(GoogleSignInResult result) {
        if (!result.isSuccess()) {


            Toast.makeText(SignUpActivity.this, "User SignUp Failed", Toast.LENGTH_SHORT).show();

/*
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
*/
//            finish();
        } else {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            /*GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);*/
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleresult(result);
        }
    }
}
