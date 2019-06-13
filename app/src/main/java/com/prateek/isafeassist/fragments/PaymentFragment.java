package com.prateek.isafeassist.fragments;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.prateek.isafeassist.R;

/**
 *
 */
public class PaymentFragment extends android.app.Fragment implements View.OnClickListener {

    LinearLayout paytm, gpay, card, netbanking;

    Toolbar toolbar;
    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        paytm = view.findViewById(R.id.paytm);
        gpay = view.findViewById(R.id.gpay);
        card = view.findViewById(R.id.creditcard);
        netbanking = view.findViewById(R.id.netbanking);
        paytm.setOnClickListener(this);
        gpay.setOnClickListener(this);
        card.setOnClickListener(this);
        netbanking.setOnClickListener(this);
        /*toolbar= view.findViewById(R.id.toolbar_back);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });*/
        return view;
    }

    @Override
    public void onClick(View v) {
        android.app.Fragment fragment = null;
        switch (v.getId()) {
            case R.id.paytm:

                fragment = new TransactionFragment();
                replaceFragment(fragment);

                break;
            case R.id.gpay:
                fragment = new TransactionFragment();
                replaceFragment(fragment);

                break;
            case R.id.netbanking:
                fragment = new TransactionFragment();
                replaceFragment(fragment);

                break;
            case R.id.creditcard:
                fragment = new TransactionFragment();
                replaceFragment(fragment);

                break;
        }
    }

    public void replaceFragment(android.app.Fragment fragment) {
        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
