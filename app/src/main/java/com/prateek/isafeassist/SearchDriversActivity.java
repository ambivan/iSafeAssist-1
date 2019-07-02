package com.prateek.isafeassist;

import android.app.ProgressDialog;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prateek.isafeassist.driverdetails.dao.Driver;

public class SearchDriversActivity extends AppCompatActivity {

    ProgressDialog dialog;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_drivers);
        auth= FirebaseAuth.getInstance();
        dialog= new ProgressDialog(SearchDriversActivity.this);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Driver");
        dialog.setMessage("Searching for Drivers");
        dialog.setCancelable(false);
        dialog.show();

        NotifyDriver();
    }

    private void NotifyDriver() {

        DatabaseReference dd= FirebaseDatabase.getInstance().getReference().child("UserRequest");
        dd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String lat= dataSnapshot.child(auth.getCurrentUser().getUid()).child("latitude").getValue(String.class);
                final String longitude= dataSnapshot.child(auth.getCurrentUser().getUid()).child("longitude").getValue(String.class);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()){

                            Driver driver= ds.getValue(Driver.class);
                            Location location1 = new Location("");
                            location1.setLatitude(Double.parseDouble(lat));
                            location1.setLongitude(Double.parseDouble(longitude));

                            Location location2 = new Location("");
                            location2.setLatitude(Double.parseDouble(driver.getLatitude()));
                            location2.setLongitude(Double.parseDouble(driver.getLongitude()));

                            float distanceInMeters = location1.distanceTo(location2);
                            System.out.println(distanceInMeters);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
