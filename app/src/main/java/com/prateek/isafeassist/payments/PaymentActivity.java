package com.prateek.isafeassist.payments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prateek.isafeassist.MainActivity;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.fragments.BikesFragment;
import com.prateek.isafeassist.fragments.CarFragment;
import com.prateek.isafeassist.fragments.DefaultFragment;
import com.prateek.isafeassist.payments.paymentdao.PayDao;
import com.prateek.isafeassist.payments.paymentdao.memcount;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private Button buttonConfirmOrder;
    public static TextView cfname, clname, cvehno, ctotalamt, cregono;
    DatabaseReference databaseReference, reference, reff;
    FirebaseAuth auth;
    String state, vehname;
    static String memno;
    public static String count;

//    public static long count = 110000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        auth = FirebaseAuth.getInstance();
        Window window = PaymentActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(PaymentActivity.this, R.color.mystatus));
         //PaymentActivity.count++;


        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getUid());
        reference = FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getUid());
        reff= FirebaseDatabase.getInstance().getReference();
        reff.child("member").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count=dataSnapshot.child("count").getValue(String.class);
                System.out.println(count);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        findViews();
        listeners();



        Intent intent = this.getIntent();

        String first = intent.getExtras().getString("fname");
        String last = intent.getExtras().getString("lname");
        String regster = intent.getExtras().getString("regno");
        String amt = intent.getExtras().getString("amt");
        vehname = intent.getExtras().getString("vehname");
        state = intent.getExtras().getString("state");
        cfname.setText(first);
        clname.setText(last);
        cvehno.setText(vehname);
        ctotalamt.setText(amt);
        cregono.setText(regster);
    }

    public void findViews() {
        buttonConfirmOrder = (Button) findViewById(R.id.buttonConfirmOrder);
        //editTextPayment = (EditText) findViewById(R.id.editTextPayment);
        cfname = findViewById(R.id.cfname);
        clname = findViewById(R.id.clname);
        cvehno = findViewById(R.id.cvehname);
        ctotalamt = findViewById(R.id.ctotalamt);
        cregono = findViewById(R.id.cregno);
    }

    public void listeners() {


        buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });
    }


    public void startPayment() {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        Intent intent = this.getIntent();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "SOLVE");
            options.put("description", "Membership Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");


            String std = intent.getExtras().getString("Uniqueid");
            if (std.equals("Car")) {
                String payment = "849.60";
                double total = Double.parseDouble(payment);
                total = total * 100;
                options.put("amount", total);

            } else {
                String payment = "283.20";
                double total = Double.parseDouble(payment);
                total = total * 100;
                options.put("amount", total);

            }


            JSONObject preFill = new JSONObject();
            preFill.put("email", " ");
            preFill.put("contact", " ");

            options.put("prefill", preFill);

            //System.out.println("email"+preFill.get("email"));
            //System.out.println("phone"+preFill.get("contact"));
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        //count = count++;
       try {
           String expdate;
           long cc = Integer.parseInt(count);
           cc++;

           System.out.println(cc);
           memcount memc= new memcount();
           memc.setCount(String.valueOf(cc));
           reff.child("member").setValue(memc);
           PayDao payDao = new PayDao();
           String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
           Calendar c = Calendar.getInstance();
           try {
               c.setTime(sdf.parse(date));
           } catch (ParseException e) {
               e.printStackTrace();
           }
           c.add(Calendar.DATE, 367);
           sdf = new SimpleDateFormat("dd/MM/yyyy");
           Date resultdate = new Date(c.getTimeInMillis());
           expdate = sdf.format(resultdate);
           payDao.setTransactid(razorpayPaymentID);
           payDao.setPurchasedate(date);
           payDao.setExpirydate(expdate);

           if (DefaultFragment.var == 1) {

           }

           Intent intent = new Intent(PaymentActivity.this, TransactionActivity.class);
           if (DefaultFragment.var == 1) {
               memno = "iSAFEAssist-RSA-C-" + state + String.valueOf(cc);
               payDao.setMembershipid("iSAFEAssist-RSA-C-" + state + String.valueOf(cc));
               databaseReference.child("Car Package").child(CarFragment.carkey).child("Payments").setValue(payDao);
               intent.putExtra("Transactionid", razorpayPaymentID);
               intent.putExtra("CType", "C");
               intent.putExtra("Cat", "1");
               intent.putExtra("count", String.valueOf(count));
               intent.putExtra("vehicle", vehname);
               startActivity(intent);


           } else if (DefaultFragment.var == 0) {
               memno = "iSAFEAssist-RSA-B-" + state + String.valueOf(cc);
               payDao.setMembershipid("iSAFEAssist-RSA-B-" + state + String.valueOf(cc));

               databaseReference.child("Bike Package").child(BikesFragment.key).child("Payments").setValue(payDao);
               intent.putExtra("Transactionid", razorpayPaymentID);
               intent.putExtra("CType", "B");
               intent.putExtra("Cat", "0");
               intent.putExtra("vehicle", vehname);

               intent.putExtra("count", String.valueOf(cc));
               startActivity(intent);


           }

           Toast.makeText(this, "Payment successfully done! " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
       }catch (Exception e){
           System.out.println("error"+ e);
       }

    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment error. Please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    @Override
    public void onBackPressed() {
        final Intent inte = this.getIntent();

        new AlertDialog.Builder(this)
                .setTitle("Sure to Cancel Payment?")
                .setMessage("You will have to fill Details again! ")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        String kk = inte.getExtras().getString("key");
                        String id = inte.getExtras().getString("Uniqueid");

                        if (id.equals("Car")) {
                            databaseReference.child("Car Package").child(kk).removeValue();
                        } else {
                            databaseReference.child("Bike Package").child(kk).removeValue();
                        }

                        Intent intent2 = new Intent(PaymentActivity.this, MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent2);
                        finish();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}


//rzp_test_n1n6FybSsiMOHW