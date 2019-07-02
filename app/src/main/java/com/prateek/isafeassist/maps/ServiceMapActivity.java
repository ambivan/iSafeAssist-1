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
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.SearchDriversActivity;
import com.prateek.isafeassist.driverdetails.DriverListActivity;
import com.prateek.isafeassist.model.UserLocationService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ServiceMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    MarkerOptions markerOptions;
    LatLng latLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    Toolbar toolbar;
    ProgressDialog dialog;
    Button callservice;
    DatabaseReference reference;
    FirebaseAuth auth;
    EditText etLocation;
    static String loc;
    Button btn_find;

    String addr = "";
    static int m = 0;


    public FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_map);
        checkLocationPermission();

//        HomePageActivity.frag = 3;

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        etLocation = (EditText) findViewById(R.id.editplace);
        btn_find = (Button) findViewById(R.id.btnsearch);
        callservice = findViewById(R.id.call_out_service_btn);
        dialog = new ProgressDialog(ServiceMapActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Fetching Location..");
        dialog.show();

        client = LocationServices.getFusedLocationProviderClient(ServiceMapActivity.this);


        client.getLastLocation().addOnSuccessListener(ServiceMapActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                String addr1 = "";

                Geocoder geocoder = new Geocoder(ServiceMapActivity.this, Locale.getDefault());

                List<Address> addressList1 = null;
                try {
                    addressList1 = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (addressList1 != null && addressList1.size() > 0) {
                        Log.i("PlaceInfo", addressList1.get(0).toString());

                        for (int i = 0; i < 7; i++) {

                            if (addressList1.get(0).getAddressLine(i) != null) {
                                addr1 += addressList1.get(0).getAddressLine(i) + " ";

                                etLocation.setText(addr1);
                                loc = etLocation.getText().toString();
                                System.out.println("loc" + loc);

                                etLocation.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        loc = etLocation.getText().toString();

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


        mGoogleApiClient = new GoogleApiClient.Builder(ServiceMapActivity.this)
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
                loc = etLocation.getText().toString();

                if (loc != null && !loc.equals("")) {
                    new GeocoderTask().execute(loc);
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
                    if (ContextCompat.checkSelfPermission(ServiceMapActivity.this,
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
                    Toast.makeText(ServiceMapActivity.this, "permission denied", Toast.LENGTH_LONG).show();
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
            if (ContextCompat.checkSelfPermission(ServiceMapActivity.this,
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

                Geocoder geocoder = new Geocoder(ServiceMapActivity.this, Locale.getDefault());

                List<Address> addressList = null;

                try {
                    addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    if (addressList != null && addressList.size() > 0) {
                        Log.i("PlaceInfo", addressList.get(0).toString());

                        for (int i = 0; i < 7; i++) {

                            if (addressList.get(0).getAddressLine(i) != null) {
                                addr += addressList.get(0).getAddressLine(i) + " ";

                                etLocation.setText(addr);
                                loc = etLocation.getText().toString();

                                etLocation.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        loc = etLocation.getText().toString();

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
        if (ContextCompat.checkSelfPermission(ServiceMapActivity.this,
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
    public void onLocationChanged(final Location location) {

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
        dialog.dismiss();
        final Double l1, l2;
        l1 = latLng.latitude;
        l2 = latLng.longitude;
        System.out.println(latLng);
        callservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth != null) {
                    UserLocationService locationService = new UserLocationService();
                    locationService.setLongitude(l2.toString());
                    locationService.setLatitude(l1.toString());

                    reference.child("User").child(auth.getCurrentUser().getUid()).child("UserLocation").setValue(locationService, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(ServiceMapActivity.this, "Looking for Drivers", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    reference.child("UserRequest").child(auth.getCurrentUser().getUid()).setValue(locationService);
                    startActivity(new Intent(ServiceMapActivity.this, SearchDriversActivity.class));

                }


            }
        });

        //UploadUserLocation(l1.toString(), l2.toString());

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


        @Override
        protected void onPostExecute(List<Address> addresses) {

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            mGoogleMap.clear();

            // Adding Markers on Google Map for each matching address
            for (int i = 0; i < addresses.size(); i++) {

                Address address = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(etLocation.getText().toString());


                mGoogleMap.addMarker(markerOptions);

                // Locate the first location
                if (i == 0)
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            }
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(ServiceMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ServiceMapActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(ServiceMapActivity.this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ServiceMapActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ServiceMapActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private void UploadUserLocation(String l1, String l2) {

        UserLocationService userLocationService = new UserLocationService();

        System.out.println("LATI"+userLocationService.getLatitude());
        System.out.println("Longi"+userLocationService.getLongitude());

        userLocationService.setLatitude(l1);
        userLocationService.setLongitude(l2);


    }
}
