package com.prateek.isafeassist.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.prateek.isafeassist.MainActivity;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.adapters.ServiceAdapter;
import com.prateek.isafeassist.model.service;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission_group.CAMERA;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class LandingFragment extends android.app.Fragment {
    Button knowmore, siteopen, call;
    final int REQUEST_CALL = 1;
    boolean ispermissiongranted;
    RecyclerView recyclerView;
    ServiceAdapter adapter;
    private List list = new ArrayList<>();
    LocationManager locationManager;
    String provider;
    private static final int PERMISSION_REQUEST_CODE = 200;

    public LandingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_landing, container, false);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        knowmore = view.findViewById(R.id.know_more_site);
        siteopen = view.findViewById(R.id.know_more_isafe_site);
        call = view.findViewById(R.id.callassistance);
        recyclerView = view.findViewById(R.id.service_recycler);
        adapter = new ServiceAdapter(getActivity(), list);
        preparedata();
        //checkLocationPermission();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if(!checkPermission()){
            requestPermission();
        }


        knowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://www.isafeassist.org/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        siteopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.road-safety.co.in/isafe19/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //makecall();
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED) {
/*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
*/

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

                } else {
//                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("1234567890")));
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "18004197779"));//change the number
                    startActivity(callIntent);
                }
            }
        });
        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //makecall();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "1234567890"));//change the number
                startActivity(callIntent);
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted) {
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                    //Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {
                        Toast.makeText(getActivity(), "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_SHORT).show();

                        //Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }


        /*switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        *//*&& grantResults[0] == PackageManager.PERMISSION_GRANTED*//*) {
                    boolean permgranted= grantResults[0]== PackageManager.PERMISSION_GRANTED;

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    *//*if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {


                        //Request location updates:
                        //locationManager.requestLocationUpdates(provider, 400, 1,this);
                    }*//*

                    if(permgranted){
                        Toast.makeText(getActivity(),"Permission Granted",Toast.LENGTH_SHORT).show();
                    }else{
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, REQUEST_CALL);
                        }
                    }

                } else {

                    *//*new AlertDialog.Builder(getActivity())
                            .setTitle("Permission Required")
                            .setMessage("You need to allow Location permission to Proceed.")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    checkLocationPermission();
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();*//*


                }
                return;
            }

        }
*/

        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);

    }


    public void preparedata() {
        service serve = new service("Call out Service", "₹ 500 Only*");
        list.add(serve);
        service serve2 = new service("Towing upto 25 kms", "₹ 1200 Only*");
        list.add(serve2);

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    /*public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission")
                        .setMessage("Allow iSAFE Assist to access your location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
*/
    @Override
    public void onResume() {
        super.onResume();

    }
}
