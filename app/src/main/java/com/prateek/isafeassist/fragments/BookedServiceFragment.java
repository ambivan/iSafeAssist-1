package com.prateek.isafeassist.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.adapters.CallServiceAdapter;
import com.prateek.isafeassist.model.servicemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookedServiceFragment extends android.app.Fragment {

    RecyclerView recyclerView;
    CallServiceAdapter adapter;
    ProgressDialog dialog;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    TextView textView;
    ImageView imageView;
    LinearLayout layout;
    List<servicemodel> list= new ArrayList<>();


    public BookedServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_booked, container, false);
        recyclerView= view.findViewById(R.id.service_list_recycler);
        layout= view.findViewById(R.id.sericelayouttohide);
        textView= view.findViewById(R.id.tttt);
        imageView= view.findViewById(R.id.dizzz);
        auth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading Service Requests");
        dialog.setCancelable(false);
        dialog.show();

        loadServicelist(recyclerView);
        return view;
    }

    private void loadServicelist(final RecyclerView recyclerView) {
        //recyclerView.removeAllViewsInLayout();

        databaseReference.child("Requests").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //System.out.println(dataSnapshot.getKey());

                String dname= dataSnapshot.child("drivername").getValue(String.class);
                String dcontact= dataSnapshot.child("driverphone").getValue(String.class);
                String dotp= dataSnapshot.child("OTP").getValue(String.class);

                String price="₹ 500/-";
                servicemodel model= new servicemodel();

                if(dname!= null && dcontact !=null && dname.length()>0 && dcontact.length()>0 ){
                    model.setDname(dname);
                    model.setDcontact(dcontact);
                    model.setDotp(dotp);
                    model.setPrice(price);
                    list.add(model);

                }

                if(list==null){

                    layout.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    dialog.dismiss();

                }else{
                    imageView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    System.out.println(dname+ dcontact);
                    System.out.println(list);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    adapter= new CallServiceAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                    dialog.dismiss();



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        databaseReference.child("Towing Requests").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //System.out.println(dataSnapshot.getKey());

                String dname= dataSnapshot.child("drivername").getValue(String.class);
                String dcontact= dataSnapshot.child("driverphone").getValue(String.class);
                String dotp= dataSnapshot.child("OTP").getValue(String.class);

                servicemodel model= new servicemodel();
                if(dname!= null && dcontact !=null && dname.length()>0 && dcontact.length()>0 ){
                    model.setDname(dname);
                    model.setDcontact(dcontact);
                    model.setDotp(dotp);
                    model.setPrice("₹ 1200/-");
                    list.add(model);

                }
                if(list==null){

                    layout.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    dialog.dismiss();

                }else{
                    imageView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    System.out.println(dname+ dcontact);
                    System.out.println(list);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    adapter= new CallServiceAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                    dialog.dismiss();



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
