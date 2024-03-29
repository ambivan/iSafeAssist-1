package com.prateek.isafeassist.maps;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.SearchDriversActivity;
import com.prateek.isafeassist.driverdetails.DriverListActivity;
import com.prateek.isafeassist.driverdetails.dao.Driver;
import com.prateek.isafeassist.model.UserDetails;
import com.prateek.isafeassist.model.UserLocationService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    ProgressDialog dialog, dialog2;
    Button callservice;
    DatabaseReference reference;
    FirebaseAuth auth;
    EditText etLocation;
    static String loc;
    Button btn_find;
    List<Address> addresses;
    Geocoder geocoder;
    private BottomSheetBehavior bottomSheetBehavior;

    String addr = "";
    static int m = 0;


    public FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_map);
        checkLocationPermission();
        View bottom = findViewById(R.id.bottom_sheet);

//        HomePageActivity.frag = 3;
        bottomSheetBehavior = BottomSheetBehavior.from(bottom);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        etLocation = (EditText) findViewById(R.id.editplace);
        btn_find = (Button) findViewById(R.id.btnsearch);
        callservice = findViewById(R.id.call_out_service_btn);
        dialog = new ProgressDialog(ServiceMapActivity.this);
        dialog2 = new ProgressDialog(ServiceMapActivity.this);
        dialog.setCancelable(false);
        dialog2.setCancelable(false);
        dialog.setMessage("Fetching Location..");
        dialog2.setMessage("Looking for Drivers..");
        dialog.show();

        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                callservice.setVisibility(View.GONE);

            }
        });

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
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                callservice.setVisibility(View.VISIBLE);
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

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
                    reference.child("User").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String contact = dataSnapshot.child("contactNo").getValue(String.class);
                            UserDetails details = new UserDetails();
                            /*Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                            String strDate =  mdformat.format(calendar.getTime());*/
                            String currentDateTimeString = DateFormat.getDateTimeInstance()
                                    .format(new Date());
                            System.out.println(currentDateTimeString);

                            details.setName(name);
                            details.setContactNo(contact);
                            details.setLat(l1.toString());
                            details.setLongi(l2.toString());
                            details.setRequesting("1");
                            details.setTime(currentDateTimeString);
                            details.setEndrequest("0");
                            reference.child("Requests").child(auth.getCurrentUser().getUid()).setValue(details);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    geocoder = new Geocoder(ServiceMapActivity.this, Locale.getDefault());

                    reference.child("User").child(auth.getCurrentUser().getUid()).child("UserLocation").setValue(locationService, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                dialog2.show();
                                Toast.makeText(ServiceMapActivity.this, "Looking for Drivers", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    reference.child("Requests").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {


                                String dname = ds.child("drivername").getValue(String.class);
                                String dcontact = ds.child("driverphone").getValue(String.class);
                                String dlat = ds.child("driverlat").getValue(String.class);
                                String dlong = ds.child("driverlong").getValue(String.class);
                                String ulat = ds.child("lat").getValue(String.class);
                                String ulong = ds.child("longi").getValue(String.class);
                                String ot= ds.child("OTP").getValue(String.class);


                                System.out.println(dname + " " + dcontact + " ");
                                if (dname != null) {
                                    try {
                                        addresses = geocoder.getFromLocation(Double.parseDouble(ulat), Double.parseDouble(ulong), 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String add = addresses.get(0).getAddressLine(0);
                                    final TextView uname, phone, ulocation, otpp;
                                    final Button accept, decline;
                                    final AlertDialog dialogBuilder = new AlertDialog.Builder(ServiceMapActivity.this).create();
                                    LayoutInflater inflater = getLayoutInflater();
                                    final View dialogView = inflater.inflate(R.layout.customdialog, null);
                                    dialogBuilder.setCancelable(true);
                                    uname = dialogView.findViewById(R.id.dname);
                                    phone = dialogView.findViewById(R.id.dphone);
                                    otpp= dialogView.findViewById(R.id.startotp);
                                    ulocation = dialogView.findViewById(R.id.dloc);
                                    uname.setText(dname);
                                    phone.setText(dcontact);
                                    ulocation.setText(add);
                                    otpp.setText(ot);
                                    dialogBuilder.setView(dialogView);
                                    dialogBuilder.show();

                                    dialog2.dismiss();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    reference.child("UserRequest").child(auth.getCurrentUser().getUid()).setValue(locationService);
                    //startActivity(new Intent(ServiceMapActivity.this, SearchDriversActivity.class));

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

    /*private void UploadUserLocation(String l1, String l2) {

        UserLocationService userLocationService = new UserLocationService();

        System.out.println("LATI" + userLocationService.getLatitude());
        System.out.println("Longi" + userLocationService.getLongitude());

        userLocationService.setLatitude(l1);
        userLocationService.setLongitude(l2);


    }*/
}
