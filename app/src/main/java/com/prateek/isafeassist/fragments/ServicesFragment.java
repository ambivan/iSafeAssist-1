package com.prateek.isafeassist.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.prateek.isafeassist.R;
import com.prateek.isafeassist.maps.ServiceMapActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends android.app.Fragment {

    Button callservice, towing;

    public ServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_services, container, false);

        callservice = view.findViewById(R.id.book_callservice);
        towing = view.findViewById(R.id.book_towing);

        callservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                loadFragment(new CallServiceFragment());
*/
                Intent intent= new Intent(getActivity(), ServiceMapActivity.class);
                startActivity(intent);
            }
        });

        towing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CallTowingFragment());
            }
        });
        return view;
    }

    private void loadFragment(android.app.Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); // save the changes
    }

}
