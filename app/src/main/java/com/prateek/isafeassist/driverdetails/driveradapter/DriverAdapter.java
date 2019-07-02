package com.prateek.isafeassist.driverdetails.driveradapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.driverdetails.dao.Driver;
import com.prateek.isafeassist.driverdetails.dao.DriverDetails;
import com.prateek.isafeassist.model.UserLocationService;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.MyViewHolder> {


    DatabaseReference databaseReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    ArrayList<String> arrayList = new ArrayList<>();
    DriverDetails details = new DriverDetails();
    Context context;
    Geocoder geocoder;
    List<Address> addresses;
    List<Driver> list;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    List<UserLocationService> locationServices = new ArrayList<>();

    ArrayList<String> list2 = new ArrayList<>();

    public DriverAdapter(Context context, List<Driver> list) {
        this.context = context;
        this.list = list;
        getUserLoc();

    }

    @NonNull
    @Override
    public DriverAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.driver_binder, viewGroup, false);

        geocoder = new Geocoder(context, Locale.getDefault());

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DriverAdapter.MyViewHolder holder, int i) {

        final Driver driver = list.get(i);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String lat = dataSnapshot.child(auth.getCurrentUser().getUid()).child("UserLocation").child("latitude").getValue(String.class);

                String longitude = dataSnapshot.child(auth.getCurrentUser().getUid()).child("UserLocation").child("longitude").getValue(String.class);
                //System.out.println(lat);
                UserLocationService userLocationServ = new UserLocationService();
                userLocationServ.setLatitude(lat);
                userLocationServ.setLongitude(longitude);
                System.out.println("User longi" + userLocationServ.getLongitude());
                System.out.println("User lati" + userLocationServ.getLatitude());

                Location location1 = new Location("");
                location1.setLatitude(Double.parseDouble(lat));
                location1.setLongitude(Double.parseDouble(longitude));

                Location location2 = new Location("");
                location2.setLatitude(Double.parseDouble(driver.getLatitude()));
                location2.setLongitude(Double.parseDouble(driver.getLongitude()));

                float distanceInMeters = location1.distanceTo(location2);
                double speed= 11.111;
                System.out.println("Distance  "+distanceInMeters);
                double time=(distanceInMeters/(speed*60));
                if(time<60){
                    holder.estimatedarrival.setText(df2.format(time) +" minutes away");

                }else {
                    holder.estimatedarrival.setText(df2.format(time/60) +" hours away");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(driver.getLatitude()), Double.parseDouble(driver.getLongitude()), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String add = addresses.get(0).getAddressLine(0);


        UserLocationService userLocationService = new UserLocationService();
        System.out.println(list);
        holder.dname.setText(driver.getName());
        holder.dcontact.setText(driver.getContact());
        holder.dloc.setText(add);
        System.out.println("Driver latitude" + driver.getLatitude());
        System.out.println("Driver longi" + driver.getLongitude());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dname, dcontact, dloc, estimatedarrival;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dname = itemView.findViewById(R.id.driver_name);
            dcontact = itemView.findViewById(R.id.driver_number);
            dloc = itemView.findViewById(R.id.driver_location);
            estimatedarrival = itemView.findViewById(R.id.driver_arrivaltime);
        }
    }

    private void getUserLoc() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String lat = dataSnapshot.child(auth.getCurrentUser().getUid()).child("UserLocation").child("latitude").getValue(String.class);

                String longitude = dataSnapshot.child(auth.getCurrentUser().getUid()).child("UserLocation").child("longitude").getValue(String.class);
                //System.out.println(lat);
                UserLocationService userLocationServ = new UserLocationService();
                userLocationServ.setLatitude(lat);
                userLocationServ.setLongitude(longitude);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
