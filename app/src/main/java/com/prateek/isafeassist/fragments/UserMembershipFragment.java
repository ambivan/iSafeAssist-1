package com.prateek.isafeassist.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.Button;
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
import com.prateek.isafeassist.model.membermodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserMembershipFragment extends android.app.Fragment {

    Button buynow;
    RecyclerView recyclerView;
    MembershipAdapter adapter;
    ProgressDialog dialog;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ImageView image, pimg;
    TextView txt;
    LinearLayout layout;
    List<membermodel> list = new ArrayList<>();
    List<membermodel> list2 = new ArrayList<>();
    public static int ckey;
    public static int bkey;

    public UserMembershipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_membership, container, false);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getUid());
        recyclerView = view.findViewById(R.id.membershipdetails);
        buynow = view.findViewById(R.id.buy_now_membership);
        txt = view.findViewById(R.id.nomembershipyet);
        image = view.findViewById(R.id.dizzyface);
        pimg = view.findViewById(R.id.pimg);

        layout = view.findViewById(R.id.layouttohide);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading Membership Details");
        dialog.setCancelable(false);
        dialog.show();

/*
        image.setVisibility(View.VISIBLE);
        txt.setVisibility(View.VISIBLE);
        buynow.setVisibility(View.VISIBLE);
        pimg.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
*/

        loadMembership(recyclerView);


        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// create a FragmentManager
                FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
                fragmentTransaction.replace(R.id.frame_container, new DefaultFragment());
                fragmentTransaction.commit(); // save the changes
            }
        });
        return view;
    }

    private void loadMembership(final RecyclerView recyclerView) {

        databaseReference.child("Bike Package").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bkey = 1;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    System.out.println(key);
                    membermodel model = new membermodel();
                    String buydate = ds.child("Payments").child("purchasedate").getValue(String.class);
                    String expdate = ds.child("Payments").child("expirydate").getValue(String.class);
                    String price = "₹ 240/-";
                    String transacid = ds.child("Payments").child("membershipid").getValue(String.class);
                    if(buydate!=null && expdate!=null && transacid!=null && buydate.length()>0 && expdate.length()>0
                            && transacid.length()>0) {
                        model.setPurchased(buydate);
                        model.setExpiry(expdate);
                        model.setAvailed(price);
                        model.setMid(transacid);
                        list.add(model);

                    }
                    if (list==null) {
                        layout.setVisibility(View.VISIBLE);
                        image.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.VISIBLE);
                        buynow.setVisibility(View.VISIBLE);
                        pimg.setVisibility(View.GONE);
                        dialog.dismiss();

                    } else {
                        image.setVisibility(View.GONE);
                        txt.setVisibility(View.GONE);
                        buynow.setVisibility(View.GONE);
                        pimg.setVisibility(View.VISIBLE);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        adapter = new MembershipAdapter(getActivity(), list);
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
                ckey = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    System.out.println(key);
                    membermodel model = new membermodel();
                    String buydate = ds.child("Payments").child("purchasedate").getValue(String.class);
                    String expdate = ds.child("Payments").child("expirydate").getValue(String.class);
                    String transacid = ds.child("Payments").child("membershipid").getValue(String.class);
                    String price = "₹ 720/-";

                    if(buydate!=null && expdate!=null && transacid!=null && buydate.length()>0 && expdate.length()>0
                    && transacid.length()>0){
                        model.setPurchased(buydate);
                        model.setExpiry(expdate);
                        model.setAvailed(price);
                        model.setMid(transacid);
                        list.add(model);
                        System.out.println("hala " + list);

                    }
                }
                if (list == null) {
                    layout.setVisibility(View.VISIBLE);

                    /*image.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.VISIBLE);
                    buynow.setVisibility(View.VISIBLE);
                    */dialog.dismiss();

                } else {
                    /*layout.setVisibility(View.GONE);

                    image.setVisibility(View.GONE);
                    txt.setVisibility(View.GONE);
                    buynow.setVisibility(View.GONE);
                    */
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    adapter = new MembershipAdapter(getActivity(), list);
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
