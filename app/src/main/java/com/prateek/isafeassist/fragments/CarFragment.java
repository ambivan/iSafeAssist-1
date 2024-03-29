package com.prateek.isafeassist.fragments;


import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.prateek.isafeassist.model.User;
import com.prateek.isafeassist.model.UserDetails;
import com.prateek.isafeassist.payments.PaymentActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarFragment extends android.app.Fragment {

    Button carbtn;
    Spinner s1, s2, s3, s4;
    final String str1[] = {"City*"};
    final String str2[] = {"State*", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "" +
            "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh",
            "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan",
            "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar",
            "Chandigarh", "Dadar and Nagar", "Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"};
    final String str3[] = {"India*"};
    final String str4[] = {"Bike"};
    boolean status;
    Toolbar toolbar;
    TextView termscar;
    public static String carkey;

    EditText fname, lname, email, mobnumber, add, land, zip, city;

    CheckBox checkBox;
    EditText car, carmake, carmodel, carregno, carinsurance, carexpiry, caryear;

    String firstname, lastname, emailid, mobile, address, landmark, usercity, userstate, postal;
    String carcar, carcarmake, carcarmodel, carcarregno, carcarinsurance, carcarexpiry, carcaryear;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth auth;
    ProgressDialog dialog;
    Calendar myCalendar = Calendar.getInstance();

    View view;

    public CarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_car, container, false);

        fname = view.findViewById(R.id.fname);
        lname = view.findViewById(R.id.lname);
        email = view.findViewById(R.id.mail);
        mobnumber = view.findViewById(R.id.mobnumber);
        add = view.findViewById(R.id.add);
        land = view.findViewById(R.id.land);
        termscar= view.findViewById(R.id.termscar);

        zip = view.findViewById(R.id.zip);
        checkBox = view.findViewById(R.id.termscheckforcar);
        city = view.findViewById(R.id.car_city);
        //state = view.findViewById(R.id.car_state);
        car = view.findViewById(R.id.car_car);
        carmake = view.findViewById(R.id.car_carmake);
        carmodel = view.findViewById(R.id.car_carmodel);
        caryear = view.findViewById(R.id.car_year);
        carregno = view.findViewById(R.id.car_regno);
        carinsurance = view.findViewById(R.id.car_insurance);
        carexpiry = view.findViewById(R.id.car_expiry);
        carbtn = view.findViewById(R.id.car_buynow_btn);
        s2= view.findViewById(R.id.car_state_spinner);
        s3 = view.findViewById(R.id.car_india_spinner);
        dialog= new ProgressDialog(getActivity());



        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

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
        carexpiry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), datee, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //getallData();
        termscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), TermsConditionsActivity.class);
                startActivity(intent);
            }
        });

        carbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkfields();
                allotfields();

                if (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastname) || TextUtils.isEmpty(emailid) || TextUtils.isEmpty(mobile) ||
                        TextUtils.isEmpty(address) || TextUtils.isEmpty(landmark) || TextUtils.isEmpty(usercity) ||
                        /*TextUtils.isEmpty(userstate)*/userstate.equals("State*") || TextUtils.isEmpty(postal) || TextUtils.isEmpty(carcaryear)|| TextUtils.isEmpty(carcarregno)) {

                    Toast.makeText(getActivity(), "Enter All fields correctly", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.setMessage("Loading..");
                    dialog.setCancelable(false);
                    dialog.show();

                    final User user = new User();
                    user.setFirstname(firstname);
                    user.setLastname(lastname);
                    user.setEmail(emailid);
                    user.setMobno(mobile);
                    user.setAddress(address);
                    user.setLandmark(landmark);
                    user.setZip(postal);
                    user.setCity(usercity);
                    user.setState(userstate);
                    user.setCar(carcar);
                    user.setCarmake(carcarmake);
                    user.setCarmodel(carcarmodel);
                    user.setYear(carcaryear);
                    user.setInsuranceco(carcarinsurance);
                    user.setRegno(carcarregno);
                    user.setInsuranceexp(carcarexpiry);
                    user.setCid("Car" + auth.getCurrentUser().getUid());

                    UserDetails details = new UserDetails();

                    if (!(checkBox).isChecked()) {
                        dialog.dismiss();

                        checkBox.setError("Please accept terms before proceeding");
                    } else {
                        if (auth.getCurrentUser() != null) {
                            DatabaseReference ii = mFirebaseDatabase.child("User").child(auth.getCurrentUser().getUid()).child("Car Package").push();
                            carkey = ii.getKey();
                            ii.setValue(user, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError == null) {


                                        Toast.makeText(getActivity(), "Data saved Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(), PaymentActivity.class);
                                        intent.putExtra("Uniqueid", "Car");
                                        intent.putExtra("fname", firstname);
                                        intent.putExtra("lname", lastname);
                                        intent.putExtra("regno", carcarregno);
                                        intent.putExtra("amt", "843.60/-");
                                        intent.putExtra("key",carkey);
                                        intent.putExtra("state",userstate);

                                        intent.putExtra("vehname", carcar);
                                        dialog.dismiss();

                                        getActivity().startActivity(intent);
                                        //startActivity(new Intent(getActivity(), PaymentActivity.class));
                                    } else {
                                        Toast.makeText(getActivity(), "" + databaseError, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                }
            }
        });


        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str3);
        adapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s3.setAdapter(adapter4);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, str2);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s2.setAdapter(adapter2);

        return view;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        carexpiry = view.findViewById(R.id.car_expiry);

        carexpiry.setText(sdf.format(myCalendar.getTime()));
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
                /*TextUtils.isEmpty(userstate)*/userstate.equals("State*") || TextUtils.isEmpty(postal)) {

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
        carcar = car.getText().toString();
        carcarmodel = carmodel.getText().toString();
        carcarmake = carmake.getText().toString();
        carcaryear = caryear.getText().toString();
        carcarregno = carregno.getText().toString();
        carcarinsurance = carinsurance.getText().toString();
        carcarexpiry = carexpiry.getText().toString();

    }

    /*public void getallData() {
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

    }*/
}
