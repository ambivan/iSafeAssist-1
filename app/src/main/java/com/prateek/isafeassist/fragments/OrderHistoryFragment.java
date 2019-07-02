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
import com.prateek.isafeassist.adapters.MembershipAdapter;
import com.prateek.isafeassist.adapters.OrderHistoryAdapter;
import com.prateek.isafeassist.model.membermodel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryFragment extends android.app.Fragment {

    OrderHistoryAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog dialog;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    LinearLayout layout;
    ImageView imageView;
    TextView textView;

    List<membermodel> list = new ArrayList<>();


    public OrderHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_order_history, container, false);
        auth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getUid());

        textView= view.findViewById(R.id.emptyordertext);
        imageView= view.findViewById(R.id.orderimg);
        recyclerView= view.findViewById(R.id.orderhistdetails);
        layout = view.findViewById(R.id.histlayout);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading Order History");
        dialog.setCancelable(false);
        dialog.show();
        loadMembership(recyclerView);

        return view;
    }

    private void loadMembership(final RecyclerView recyclerView) {
        databaseReference.child("Bike Package").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    System.out.println(key);
                    membermodel model = new membermodel();
                    String buydate = ds.child("Payments").child("purchasedate").getValue(String.class);
                    String expdate = ds.child("Payments").child("expirydate").getValue(String.class);
                    String price = "₹283.20/-";
                    String transacid = ds.child("Payments").child("transactid").getValue(String.class);
                    if(buydate!=null && expdate!=null && transacid!=null && buydate.length()>0 && expdate.length()>0
                            && transacid.length()>0) {
                        model.setPurchased(buydate);
                        model.setExpiry(expdate);
                        model.setAvailed(price);
                        model.setMid(transacid);
                        list.add(model);
                    }
//                    list2.addAll(list);
                    System.out.println("hala " + list);
                    //System.out.println("halaaa " + buydate + expdate);
                    if (list == null) {
                        layout.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        dialog.dismiss();

                    } else {
                        imageView.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);


                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        adapter = new OrderHistoryAdapter(getActivity(), list);
                        recyclerView.setAdapter(adapter);
                        dialog.dismiss();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Car Package").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    System.out.println(key);
                    membermodel model = new membermodel();
                    String buydate = ds.child("Payments").child("purchasedate").getValue(String.class);
                    String expdate = ds.child("Payments").child("expirydate").getValue(String.class);
                    String transacid = ds.child("Payments").child("transactid").getValue(String.class);
                    String price = "₹849.60/-";

                    if(buydate!=null && expdate!=null && transacid!=null && buydate.length()>0 && expdate.length()>0
                            && transacid.length()>0){
                        model.setPurchased(buydate);
                        model.setExpiry(expdate);
                        model.setAvailed(price);
                        model.setMid(transacid);
                        list.add(model);

                    }
                    System.out.println("hala " + list);
                }
                if (list == null) {
                    layout.setVisibility(View.VISIBLE);

                    dialog.dismiss();

                } else {
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    adapter = new OrderHistoryAdapter(getActivity(), list);
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
