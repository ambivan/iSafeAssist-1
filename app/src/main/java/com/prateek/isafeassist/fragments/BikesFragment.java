package com.prateek.isafeassist.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.model.Bike;
import com.prateek.isafeassist.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class BikesFragment extends android.app.Fragment implements View.OnClickListener {

    TextView text1, text2;
    Button button;
    Spinner s1, s2, s3, s4;
    EditText fname, lname, email, mobnumber, add, land, zip, city, state;

    boolean status;
    CheckBox checkBox;
    EditText bike, bikemake, bikemodel, bikeregno, bikeinsurance, bikeexpiry, bikeyear;

    String firstname, lastname, emailid, mobile, address, landmark, usercity, userstate, postal;
    String bikebike, bikebikemake, bikebikemodel, bikebikeregno, bikebikeinsurance, bikebikeexpiry, bikebikeyear;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth auth;


    Toolbar toolbar;
    final String str1[] = {"City*"};
    final String str2[] = {"State/Province/Region*"};
    final String str3[] = {"India*"};
    final String str4[] = {"Bike"};

    public BikesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bikes, container, false);

        text1 = container.findViewById(R.id.text1);
        text2 = container.findViewById(R.id.text2);
        button = view.findViewById(R.id.bikes_buynow_btn);
        /*s1 = view.findViewById(R.id.bike_city_spinner);
        s2 = view.findViewById(R.id.bike_state_spinner);
        */
        s3 = view.findViewById(R.id.bike_india_spinner);
        //s4 = view.findViewById(R.id.bike_bike_spinner);
        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        fname = view.findViewById(R.id.bike_user_firstname);
        lname = view.findViewById(R.id.bike_user_lastname);
        email = view.findViewById(R.id.bike_user_emailid);
        mobnumber = view.findViewById(R.id.bike_user_mobile);
        add = view.findViewById(R.id.bike_user_address);
        land = view.findViewById(R.id.bike_user_landmark);
        zip = view.findViewById(R.id.bike_user_postal);
        city = view.findViewById(R.id.bike_user_city);
        state = view.findViewById(R.id.bike_user_state);
        bike = view.findViewById(R.id.bike_bike_bike);
        bikemake = view.findViewById(R.id.bike_bike_make);
        bikemodel = view.findViewById(R.id.bike_bike_model);
        bikeregno = view.findViewById(R.id.bike_bike_regno);
        bikeinsurance = view.findViewById(R.id.bike_bike_insurance);
        bikeexpiry = view.findViewById(R.id.bike_bike_expiry);
        bikeyear = view.findViewById(R.id.bike_bike_year);
        checkBox = view.findViewById(R.id.bike_package_checkbox);
        button.setOnClickListener(this);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str3);
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s3.setAdapter(adapter3);


/*        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str1);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s1.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str2);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s2.setAdapter(adapter2);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str4);
        adapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s4.setAdapter(adapter4);*/
        return view;
    }


    private void loadFragment(android.app.Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getActivity().getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_container, fragment, "tag");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onClick(View v) {
        allotfields();
        if (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastname) || TextUtils.isEmpty(emailid) || TextUtils.isEmpty(mobile) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(landmark) || TextUtils.isEmpty(usercity) ||
                TextUtils.isEmpty(userstate) || TextUtils.isEmpty(postal) || TextUtils.isEmpty(bikebikeyear)) {
            Toast.makeText(getActivity(), "Enter All Required fields", Toast.LENGTH_SHORT).show();

        } else {
            final Bike bike = new Bike();
            bike.setFirstname(firstname);
            bike.setLastname(lastname);
            bike.setMobno(mobile);
            bike.setEmail(emailid);
            bike.setAddress(address);
            bike.setLandmark(landmark);
            bike.setCity(usercity);
            bike.setState(userstate);
            bike.setZip(postal);
            bike.setBike(bikebike);
            bike.setBikemake(bikebikemake);
            bike.setBikemodel(bikebikemodel);
            bike.setYear(bikebikeyear);
            bike.setRegno(bikebike);
            bike.setInsuranceco(bikebikeinsurance);
            bike.setInsuranceexp(bikebikeexpiry);
            bike.setBikeid("Bike Package" + auth.getCurrentUser().getUid());


            if (!(checkBox).isChecked()) {
                checkBox.setError("Please accept terms before proceeding");
                Toast.makeText(getActivity(), "Accept terms and conditions ", Toast.LENGTH_SHORT).show();

            } else {
                if (auth.getCurrentUser() != null) {
                    mFirebaseDatabase.child(auth.getCurrentUser().getUid()).child("Bike Package" + auth.getCurrentUser().getUid()).push().setValue(bike, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {

                                Toast.makeText(getActivity(), "Data saved Successfully", Toast.LENGTH_SHORT).show();
                                loadFragment(new PaymentFragment());
                            } else {
                                Toast.makeText(getActivity(), "" + databaseError, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }


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
        bikebike = bike.getText().toString();
        bikebikemake = bikemake.getText().toString();
        bikebikemodel = bikemodel.getText().toString();
        bikebikeyear = bikeyear.getText().toString();
        bikebikeregno = bikeregno.getText().toString();
        bikebikeinsurance = bikeinsurance.getText().toString();
        bikebikeexpiry = bikeexpiry.getText().toString();

    }

    public void getallData() {
        final User user = new User();
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        firebaseDatabase.child("User").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                fname.setText(user.getFirstname());
                lname.setText(user.getLastname());
                email.setText(user.getEmail());
                mobnumber.setText(user.getMobno());
                add.setText(user.getAddress());
                land.setText(user.getLandmark());
                city.setText(user.getCity());
                state.setText(user.getState());
                zip.setText(user.getZip());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
