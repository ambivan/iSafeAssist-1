package com.prateek.isafeassist.driverdetails;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.driverdetails.dao.Driver;
import com.prateek.isafeassist.driverdetails.driveradapter.DriverAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DriverListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DriverAdapter adapter;
    DatabaseReference databaseReference;
    List<Driver> list = new ArrayList<>();
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);

        recyclerView = findViewById(R.id.driverlist);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dialog = new ProgressDialog(DriverListActivity.this);
        dialog.setMessage("Fetching Drivers");
        dialog.setCancelable(false);
        dialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Driver");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();
                    System.out.println(key);
                    Driver driver = ds.getValue(Driver.class);
                    list.add(driver);

                }
                adapter = new DriverAdapter(DriverListActivity.this, list);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
