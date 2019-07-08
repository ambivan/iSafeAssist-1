package com.prateek.isafeassist.maps;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prateek.isafeassist.MainActivity;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.driverdetails.dao.Driver;
import com.prateek.isafeassist.model.UserLocationService;
import com.prateek.isafeassist.model.towingloc;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TowingMapActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    /*    private GoogleMap mMap;*/
    MarkerOptions markerOptions;
    LatLng latLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    Toolbar toolbar;
    EditText etstart, etend;
    static String loc;
    Button btn_find, btn_book;
    String addr = "";
    static int m = 0;
    public FusedLocationProviderClient client;
    static double lati, longi;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    Geocoder geocoder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towing_map);
        checkLocationPermission();
        auth = FirebaseAuth.getInstance();
        etstart = findViewById(R.id.towingstart);
        etend = findViewById(R.id.towingend);
        btn_find = findViewById(R.id.gotowing);

        btn_book = findViewById(R.id.calltowingbtn);

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add = etend.getText().toString();
                if (add == null || add.length() < 0) {
                    etend.setError("Enter Drop Location");
                    etend.requestFocus();
                } else {

                    new GeocoderTask().execute(add);

                }
            }
        });
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////System.out.println();
            }
        });

        client = LocationServices.getFusedLocationProviderClient(TowingMapActivity.this);

        client.getLastLocation().addOnSuccessListener(TowingMapActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                String addr1 = "";

                Geocoder geocoder = new Geocoder(TowingMapActivity.this, Locale.getDefault());

                List<Address> addressList1 = null;
                try {
                    addressList1 = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    lati = location.getLatitude();
                    longi = location.getLongitude();
                    if (addressList1 != null && addressList1.size() > 0) {
                        Log.i("PlaceInfo", addressList1.get(0).toString());

                        for (int i = 0; i < 7; i++) {

                            if (addressList1.get(0).getAddressLine(i) != null) {
                                addr1 += addressList1.get(0).getAddressLine(i) + " ";

                                etstart.setText(addr1);
                                System.out.println("addr1 " + addr1);
                                loc = etstart.getText().toString();


                                etstart.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        loc = etstart.getText().toString();

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("nuh", "uh");
                }


            }
        });


        checkLocationPermission();

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        // Getting a reference to the map
        if (mGoogleMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }


        mGoogleApiClient = new GoogleApiClient.Builder(TowingMapActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        // Setting button click event listener for the find button
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location

                // Getting user input location
                loc = etstart.getText().toString();

                if (loc != null && !loc.equals("")) {
                    new TowingMapActivity.GeocoderTask().execute(loc);
                }
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(TowingMapActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(TowingMapActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }

    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(TowingMapActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {

            mGoogleMap.setMyLocationEnabled(true);
        }


        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                addr = "";

                Geocoder geocoder = new Geocoder(TowingMapActivity.this, Locale.getDefault());

                List<Address> addressList = null;

                try {
                    addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    if (addressList != null && addressList.size() > 0) {
                        Log.i("PlaceInfo", addressList.get(0).toString());

                        for (int i = 0; i < 7; i++) {

                            if (addressList.get(0).getAddressLine(i) != null) {
                                addr += addressList.get(0).getAddressLine(i) + " ";

/*
                                etstart.setText(addr);
*/
                                System.out.println("addr " + addr);
                                loc = etstart.getText().toString();

                                etstart.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        loc = etstart.getText().toString();

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("nuh", "uh");
                }

            }
        });

    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(TowingMapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Location mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(latLng);
        markerOptions1.title(addr);

        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions1);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        List<Address> addre;

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            mGoogleMap.clear();

            // Adding Markers on Google Map for each matching address
            for (int i = 0; i < addresses.size(); i++) {

                Address address1 = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address1.getLatitude(), address1.getLongitude());

                String addressText = String.format("%s, %s",
                        address1.getMaxAddressLineIndex() > 0 ? address1.getAddressLine(0) : "",
                        address1.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                System.out.println("latlng" + latLng);
                markerOptions.title(etstart.getText().toString());

                System.out.println("distance " + CalculationByDistance(lati, longi, latLng.latitude, latLng.longitude));
                geocoder1 = new Geocoder(TowingMapActivity.this, Locale.getDefault());

                final double distance = CalculationByDistance(lati, longi, latLng.latitude, latLng.longitude);
                if (distance > 25000) {
                    Toast.makeText(TowingMapActivity.this, "Distance Limit Exceed! " + distance / 1000 + " Towing Not Possible", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(TowingMapActivity.this)
                            .setTitle("Route Verification")
                            .setMessage("Do you want to check this Route? If No, your request for towing will be processed.")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    Intent intent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://maps.google.com/maps?saddr=" + lati + "," + longi + "&daddr=" + latLng.latitude +
                                                    "," + latLng.longitude));
                                    startActivity(intent);

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Driver");
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                String key = ds.getKey();
                                                Driver driver = new Driver();
                                                driver.setRequestfound("1");
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("request", "1");
                                                databaseReference.child(key).updateChildren(hashMap);
                                                System.out.println("keyyy" + key);

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    final ProgressDialog dialog1 = new ProgressDialog(TowingMapActivity.this);
                                    dialog1.setMessage("Checking for Availability of Drivers..");
                                    dialog1.setCancelable(false);
                                    dialog1.show();
                                    drawRoute(latLng, new LatLng(lati, longi), distance);

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    reference.child("Towing Requests").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {


                                                String dname = ds.child("drivername").getValue(String.class);
                                                String dcontact = ds.child("driverphone").getValue(String.class);
                                                String dlat = ds.child("driverlat").getValue(String.class);
                                                String dlong = ds.child("driverlong").getValue(String.class);
                                                String ulat = ds.child("startlat").getValue(String.class);
                                                String ulong = ds.child("startlong").getValue(String.class);
                                                //List<Address> addresses = null;

                                                System.out.println(dname + " " + dcontact + " " + ulat + " " + ulong);
                                                if (dname != null) {
                                                    try {
                                                        addre = geocoder1.getFromLocation(Double.parseDouble(ulat), Double.parseDouble(ulong), 1);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    String add = addre.get(0).getAddressLine(0);
                                                    final TextView uname, phone, ulocation;
                                                    final Button accept, decline;
                                                    final AlertDialog dialogBuilder = new AlertDialog.Builder(TowingMapActivity.this).create();
                                                    LayoutInflater inflater = getLayoutInflater();
                                                    final View dialogView = inflater.inflate(R.layout.customdialog, null);
                                                    dialogBuilder.setCancelable(true);
                                                    uname = dialogView.findViewById(R.id.dname);
                                                    phone = dialogView.findViewById(R.id.dphone);
                                                    ulocation = dialogView.findViewById(R.id.dloc);
                                                    uname.setText(dname);
                                                    phone.setText(dcontact);
                                                    ulocation.setText(add);
                                                    dialogBuilder.setView(dialogView);
                                                    dialogBuilder.show();

                                                    dialog1.dismiss();
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    //drawPolylines(latLng, new LatLng(lati, longi));


                }
                mGoogleMap.addMarker(markerOptions);

                // Locate the first location
                if (i == 0)
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            }
        }
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(TowingMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(TowingMapActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(TowingMapActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission to get better results.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(TowingMapActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(TowingMapActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    public double CalculationByDistance(double initialLat, double initialLong,
                                        double finalLat, double finalLong) {
        int R = 6371; // km (Earth radius)
        double dLat = toRadians(finalLat - initialLat);
        double dLon = toRadians(finalLong - initialLong);
        initialLat = toRadians(initialLat);
        finalLat = toRadians(finalLat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(initialLat) * Math.cos(finalLat);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000;


    }

    public double toRadians(double deg) {
        return deg * (Math.PI / 180);
    }

    private void drawRoute(final LatLng latLng, final LatLng latLng1, final double distance) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String uname = dataSnapshot.child("name").getValue(String.class);
                String ucontact = dataSnapshot.child("contactNo").getValue(String.class);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Towing Requests").child(auth.getCurrentUser().getUid());
                String currentDateTimeString = DateFormat.getDateTimeInstance()
                        .format(new Date());
                System.out.println(currentDateTimeString);

                towingloc locationService = new towingloc();
                locationService.setStartlat(String.valueOf(latLng1.latitude));
                locationService.setStartlong(String.valueOf(latLng1.longitude));
                locationService.setEndlat(String.valueOf(latLng.latitude));
                locationService.setEndlong(String.valueOf(latLng.longitude));
                locationService.setDistance(String.valueOf(distance));
                locationService.setRequesting("1");
                locationService.setUname(uname);
                locationService.setUcontact(ucontact);
                locationService.setTime(currentDateTimeString);
                databaseReference.setValue(locationService);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Driver");


    }

}
