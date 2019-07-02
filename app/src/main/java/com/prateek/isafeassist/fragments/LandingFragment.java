package com.prateek.isafeassist.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.prateek.isafeassist.R;
import com.prateek.isafeassist.adapters.ServiceAdapter;
import com.prateek.isafeassist.model.service;

import java.util.ArrayList;
import java.util.List;

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

    public LandingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_landing, container, false);
        knowmore = view.findViewById(R.id.know_more_site);
        siteopen = view.findViewById(R.id.know_more_isafe_site);
        call = view.findViewById(R.id.callassistance);
        recyclerView = view.findViewById(R.id.service_recycler);
        adapter = new ServiceAdapter(getActivity(), list);
        preparedata();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public void preparedata() {
        service serve = new service("Call out Service", "₹ 500 Only*");
        list.add(serve);
        service serve2 = new service("Towing upto 25 kms", "₹ 1200 Only*");
        list.add(serve2);

    }

}
