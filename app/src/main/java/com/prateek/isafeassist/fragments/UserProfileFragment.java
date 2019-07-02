package com.prateek.isafeassist.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.model.UserDetails;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends android.app.Fragment {

    private EditText mobilenumber, email;
    UserDetails details;
    private Button edit;
    CircleImageView profile;
    FirebaseAuth firebaseAuth;
    TextView username;
    DatabaseReference databaseReference;
    FirebaseUser fuser;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    private Uri filepath;
    StorageTask uploadtask;
    private static final int PICK_IMAGE_REQ = 71;


    public UserProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mobilenumber = view.findViewById(R.id.user_phone_no);
        email = view.findViewById(R.id.user_emailid);
        edit = view.findViewById(R.id.edit_profile_btn);
        username = view.findViewById(R.id.userprofile_username);
        edit.setText("Edit");
        details = new UserDetails();
        storageReference = FirebaseStorage.getInstance().getReference().child("ImageFolder");
        profile = view.findViewById(R.id.user_circular_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(firebaseAuth.getCurrentUser().getUid());
        mobilenumber.setEnabled(false);
        email.setEnabled(false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Profile Info");
        progressDialog.show();
        progressDialog.setCancelable(false);
        loadinfo();


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateDp();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText().toString().equals("Edit")) {
                    mobilenumber.setEnabled(true);
                    email.setEnabled(true);
                    edit.setText("Save");
                } else {

                    if (TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(mobilenumber.getText().toString())) {
                        Toast.makeText(getActivity(), "Fields cannot be Empty", Toast.LENGTH_SHORT).show();

                    } else if(mobilenumber.length()<10 || mobilenumber.length()>10){
                        mobilenumber.setError("Invalid Number");
                        mobilenumber.requestFocus();

                    } else
                        {


                        DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference().child("User");
                        databaseref.child(firebaseAuth.getCurrentUser().getUid()).child("email").setValue(email.getText().toString());
                        databaseref.child(firebaseAuth.getCurrentUser().getUid()).child("contactNo").setValue(mobilenumber.getText().toString());
                        mobilenumber.setEnabled(false);
                        // uploadPicture();
                        email.setEnabled(false);
                        edit.setText("Edit");
                        Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    private void uploadPicture() {

        if (filepath != null) {
            final StorageReference ref = storageReference.child("userimages" + filepath.getLastPathSegment());
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("imageurl", String.valueOf(uri));
                            databaseReference.updateChildren(hashMap);
                            Toast.makeText(getActivity(), "Successfully saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateDp() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {}
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE_REQ);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQ && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadinfo() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue(String.class);
                String mail = dataSnapshot.child("email").getValue(String.class);
                String contact = dataSnapshot.child("contactNo").getValue(String.class);
                String url = dataSnapshot.child("imageurl").getValue(String.class);
                if (url == null) {
                    Glide.with(getActivity()).load(R.drawable.userprof).into(profile);
                } else {
                    Glide.with(getActivity()).load(url).into(profile);

                }


                email.setText(mail);
                mobilenumber.setText(contact);
                username.setText(name);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
