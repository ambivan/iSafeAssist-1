package com.prateek.isafeassist.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prateek.isafeassist.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarFragment extends android.app.Fragment {

    Button carbtn;
    Spinner s1, s2, s3, s4;
    final String str1[] = {"City*"};
    final String str2[] = {"State/Province/Region*"};
    final String str3[] = {"India*"};
    final String str4[] = {"Bike"};
    boolean status;
    Toolbar toolbar;

    EditText fname, lname, email, mobnumber, add, land, zip, city, state;

    String firstname, lastname, emailid, mobile, address, landmark, usercity, userstate, postal;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;


    public CarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car, container, false);

        fname = view.findViewById(R.id.fname);
        lname = view.findViewById(R.id.lname);
        email = view.findViewById(R.id.mail);
        mobnumber = view.findViewById(R.id.mobnumber);
        add = view.findViewById(R.id.add);
        land = view.findViewById(R.id.land);
        zip = view.findViewById(R.id.zip);

        city = view.findViewById(R.id.car_city);
        state = view.findViewById(R.id.car_state);
        carbtn = view.findViewById(R.id.car_buynow_btn);
        /*s1 = view.findViewById(R.id.car_city_spinner);
        s2 = view.findViewById(R.id.car_state_spinner);
        */
        s3 = view.findViewById(R.id.car_india_spinner);
        s4 = view.findViewById(R.id.car_bike_spinner);

        allotfields();
/*        toolbar = view.findViewById(R.id.toolbar_back);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });*/
        carbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkfields();
                if (status) {
                    loadFragment(new PaymentFragment());

                } else {
                    Toast.makeText(getActivity(), "Enter All fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

/*        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str1);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s1.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str2);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s2.setAdapter(adapter2);*/
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str3);
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s3.setAdapter(adapter3);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str4);
        adapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s4.setAdapter(adapter4);

        return view;
    }

    private void loadFragment(android.app.Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getActivity().getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_container, fragment, "tage");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); // save the changes
    }

    public void checkfields() {
        if (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastname) || TextUtils.isEmpty(emailid) || TextUtils.isEmpty(mobile) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(landmark) || TextUtils.isEmpty(usercity) ||
                TextUtils.isEmpty(userstate) || TextUtils.isEmpty(postal)) {

            status = false;
        } else {
            status = true;
        }

    }

    public void allotfields() {
        firstname = fname.getText().toString();
        lastname = lname.getText().toString();
        emailid = email.getText().toString();
        mobile = mobnumber.getText().toString();
        address = add.getText().toString();
        landmark = land.getText().toString();
        usercity = city.getText().toString();
        userstate = state.getText().toString();
        postal = zip.getText().toString();
    }
}
