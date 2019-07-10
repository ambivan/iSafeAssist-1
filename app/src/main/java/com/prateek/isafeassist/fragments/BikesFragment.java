package com.prateek.isafeassist.fragments;


import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.DatePicker;
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
import com.prateek.isafeassist.TermsConditionsActivity;
import com.prateek.isafeassist.model.Bike;
import com.prateek.isafeassist.model.User;
import com.prateek.isafeassist.payments.PaymentActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class BikesFragment extends android.app.Fragment implements View.OnClickListener {

    TextView text1, text2, termsbike;
    Button button;
    Spinner s1, s2, s3, s4;
    EditText fname, lname, email, mobnumber, add, land, zip, city;
    public static String key;
    boolean status;
    CheckBox checkBox;
    EditText bike, bikemake, bikemodel, bikeregno, bikeinsurance, bikeexpiry, bikeyear;
    View view;
    String firstname, lastname, emailid, mobile, address, landmark, usercity, userstate, postal;
    String bikebike, bikebikemake, bikebikemodel, bikebikeregno, bikebikeinsurance, bikebikeexpiry, bikebikeyear;
    Calendar myCalendar = Calendar.getInstance();

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth auth;

    ProgressDialog dialog;

    Toolbar toolbar;
    final String str1[] = {"City*"};
    final String str2[] = {"State*", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "" +
            "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh",
            "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan",
            "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar",
            "Chandigarh", "Dadar and Nagar", "Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"};
    final String str3[] = {"India*"};
    final String str4[] = {"Bike"};

    public BikesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bikes, container, false);

        text1 = container.findViewById(R.id.text1);
        text2 = container.findViewById(R.id.text2);
        button = view.findViewById(R.id.bikes_buynow_btn);
        termsbike = view.findViewById(R.id.termsbike);
        /*s1 = view.findViewById(R.id.bike_city_spinner);
        */
        s2 = view.findViewById(R.id.bike_state_spinner);

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
        //state = view.findViewById(R.id.bike_user_state);
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
        dialog = new ProgressDialog(getActivity());
        termsbike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TermsConditionsActivity.class);
                startActivity(intent);
            }
        });

        final DatePickerDialog.OnDateSetListener datee = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        bikeexpiry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), datee, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



/*        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str1);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s1.setAdapter(adapter);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str4);
        adapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s4.setAdapter(adapter4);*/
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str2);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s2.setAdapter(adapter2);

        return view;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        bikeexpiry = view.findViewById(R.id.bike_bike_expiry);

        bikeexpiry.setText(sdf.format(myCalendar.getTime()));
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
                /*TextUtils.isEmpty(userstate)*/userstate.equals("State*") || TextUtils.isEmpty(postal) || TextUtils.isEmpty(bikebikeyear) || TextUtils.isEmpty(bikebikeregno)) {
            Toast.makeText(getActivity(), "Enter All Required fields Correctly", Toast.LENGTH_SHORT).show();

        } else {
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();
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
                dialog.dismiss();
                checkBox.setError("Please accept terms before proceeding");
                Toast.makeText(getActivity(), "Accept terms and conditions ", Toast.LENGTH_SHORT).show();

            } else {
                if (auth.getCurrentUser() != null) {
                    DatabaseReference ii = mFirebaseDatabase.child("User").child(auth.getCurrentUser().getUid()).child("Bike Package").push();
                    System.out.println(ii.getKey());

                    key = ii.getKey();

                    ii.setValue(bike, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {

                                Toast.makeText(getActivity(), "Data saved Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), PaymentActivity.class);
                                intent.putExtra("Uniqueid", "Bike");
                                intent.putExtra("fname", firstname);
                                intent.putExtra("lname", lastname);
                                intent.putExtra("regno", bikebikeregno);
                                intent.putExtra("amt", "283.20/-");
                                intent.putExtra("key", key);
                                intent.putExtra("state", userstate);

                                intent.putExtra("vehname", bikebike);
                                dialog.dismiss();
                                getActivity().startActivity(intent);
//                                startActivity(new Intent(getActivity(), PaymentActivity.class));
/*
                                loadFragment(new PaymentFragment());
*/

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
                TextUtils.isEmpty(userstate) || TextUtils.isEmpty(postal) || TextUtils.isEmpty(bikebikeregno)) {

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
        userstate = s2.getSelectedItem().toString();
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
                //state.setText(user.getState());
                zip.setText(user.getZip());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
