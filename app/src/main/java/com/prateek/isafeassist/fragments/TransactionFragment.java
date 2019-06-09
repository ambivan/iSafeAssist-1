package com.prateek.isafeassist.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.prateek.isafeassist.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends android.app.Fragment {

    Button button;
    Toolbar toolbar;

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_transaction, container, false);


        button = view.findViewById(R.id.receipt_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(),"Nothing to Download currently.",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
